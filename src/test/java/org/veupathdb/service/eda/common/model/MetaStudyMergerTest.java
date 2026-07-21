package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.BinUnits;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetaStudyMergerTest {

  // Helpers build minimal APIStudyDetail graphs. Prefer the generated *Impl classes
  // (e.g. APIStudyDetailImpl, APIEntityImpl, APIStringVariableImpl). See TestStudies below.

  @Test
  void rejectsDifferentEntityTreeShape() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    APIStudyDetail b = TestStudies.twoEntities("studyB");
    StudyId union = StudyId.parse("union(studyA,studyB)");
    BadRequestException ex = assertThrows(BadRequestException.class,
      () -> MetaStudyMerger.merge(union, List.of(a, b)));
    assertTrue(ex.getMessage().toLowerCase().contains("entity") || ex.getMessage().toLowerCase().contains("structur"));
  }

  @Test
  void rejectsVariableTypeMismatch() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "age", List.of("1"));
    APIStudyDetail b = TestStudies.oneEntityOneIntVar("studyB", "EUPA_1", "age");
    StudyId union = StudyId.parse("union(studyA,studyB)");
    assertThrows(BadRequestException.class, () -> MetaStudyMerger.merge(union, List.of(a, b)));
  }

  @Test
  void mergesVocabularyAsDedupedUnion() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    APIStudyDetail b = TestStudies.oneEntityOneStringVar("studyB", "EUPA_1", "sex", List.of("M", "U"));
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    assertEquals("union(studyA,studyB)", merged.getId());
    List<String> vocab = TestStudies.firstVarVocabulary(merged);
    assertEquals(List.of("F", "M", "U"), vocab); // deduped, order-normalized (sorted)
  }

  @Test
  void mergesIntegerRangeAsMinOfMinsMaxOfMaxes() {
    APIStudyDetail a = TestStudies.oneEntityIntVarWithRange("studyA", "EUPA_1", "age", 5L, 40L);
    APIStudyDetail b = TestStudies.oneEntityIntVarWithRange("studyB", "EUPA_1", "age", 1L, 60L);
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    long[] range = TestStudies.firstVarIntRange(merged); // {rangeMin, rangeMax}
    assertEquals(1L, range[0]);
    assertEquals(60L, range[1]);
  }

  @Test
  void mergesIntegerBinWidthFromFirstMember() {
    // Members carry an integer binWidth (from curation). The merge MUST preserve it (as the
    // number/date merges do), otherwise the merged var advertises a null binWidth, the client
    // falls back to a fractional default (0.1), and integer binning fails with
    // "Bin width must be a positive integer" (0.1 truncates to 0).
    APIStudyDetail a = TestStudies.oneEntityIntVarWithRange("studyA", "EUPA_1", "age", 5L, 40L);
    APIStudyDetail b = TestStudies.oneEntityIntVarWithRange("studyB", "EUPA_1", "age", 1L, 60L);
    ((org.veupathdb.service.eda.generated.model.APIIntegerVariable) a.getRootEntity().getVariables().get(0))
        .getDistributionDefaults().setBinWidth(5L);
    ((org.veupathdb.service.eda.generated.model.APIIntegerVariable) b.getRootEntity().getVariables().get(0))
        .getDistributionDefaults().setBinWidth(10L);
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    var dd = ((org.veupathdb.service.eda.generated.model.APIIntegerVariable)
        merged.getRootEntity().getVariables().get(0)).getDistributionDefaults();
    assertEquals(5L, dd.getBinWidth()); // first member's binWidth, per scope-A "take first study's binSpec"
  }

  @Test
  void mergesNumberRangeAsMinOfMinsMaxOfMaxes() {
    APIStudyDetail a = TestStudies.oneEntityNumberVarWithRange("studyA", "EUPA_1", "weight", 5.0, 40.0);
    APIStudyDetail b = TestStudies.oneEntityNumberVarWithRange("studyB", "EUPA_1", "weight", 1.0, 60.0);
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    double[] range = TestStudies.firstVarNumberRange(merged); // {rangeMin, rangeMax}
    assertEquals(1.0, range[0]);
    assertEquals(60.0, range[1]);
  }

  @Test
  void mergesIntegerRangeWhenBothMembersHaveNullRange() {
    // Regression test: distributionDefaults present but rangeMin/rangeMax unset in both
    // members previously threw NPE in MetaStudyMerger.minL/maxL (ternary auto-unboxing bug).
    APIStudyDetail a = TestStudies.oneEntityIntVarWithNullRange("studyA", "EUPA_1", "age");
    APIStudyDetail b = TestStudies.oneEntityIntVarWithNullRange("studyB", "EUPA_1", "age");
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    var mergedVar = (org.veupathdb.service.eda.generated.model.APIIntegerVariable)
        merged.getRootEntity().getVariables().get(0);
    assertNull(mergedVar.getDistributionDefaults());
  }

  @Test
  void mergesDateRangeAndCarriesBinUnits() {
    APIStudyDetail a = TestStudies.oneEntityDateVarWithRange(
        "studyA", "EUPA_1", "collectionDate", "2001-01-01", "2010-01-01", BinUnits.MONTH);
    APIStudyDetail b = TestStudies.oneEntityDateVarWithRange(
        "studyB", "EUPA_1", "collectionDate", "1999-01-01", "2015-01-01", BinUnits.MONTH);
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    String[] range = TestStudies.firstVarDateRange(merged); // {rangeMin, rangeMax}
    assertEquals("1999-01-01", range[0]);
    assertEquals("2015-01-01", range[1]);
    assertEquals(BinUnits.MONTH, TestStudies.firstVarDateBinUnits(merged));
  }
}

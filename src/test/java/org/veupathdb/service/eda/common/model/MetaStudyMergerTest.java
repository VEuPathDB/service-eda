package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
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
}

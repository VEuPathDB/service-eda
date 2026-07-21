package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.distribution.AbstractDistribution;
import org.gusdb.fgputil.distribution.DistributionResult;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionDistributionTest {

  private static Map<String, String> row(String pk, String pkVal, String sexCol, String sex) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal);
    if (sex != null) {
      m.put(sexCol, sex);
    }
    return m;
  }

  @Test
  void tallyDedupesSharedInstancesThenCountsCategories() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    // rows are keyed the way the tabular iterator keys them: qualified "entityId.column"
    String pk = VariableDef.toDotNotation(entity.getIdColumnDef());
    String sexCol = TestStudies.ROOT_ID + ".sex"; // row-map key for the sex variable, qualified
    VariableSpec sexSpec = TestStudies.varSpec(TestStudies.ROOT_ID, "sex");

    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, sp) ->
      studyId.equals("studyA")
        ? CloseableIterator.of(List.of(row(pk, "P1", sexCol, "F"), row(pk, "P2", sexCol, "M")).iterator())
        : CloseableIterator.of(List.of(row(pk, "P2", sexCol, "M"), row(pk, "P3", sexCol, "F"),
            row(pk, "P4", sexCol, null)).iterator());

    UnionDistribution.Tally result = UnionDistribution.tally(md, sexSpec, List.of(), opener);
    assertEquals(2L, result.counts().get("F")); // P1, P3
    assertEquals(1L, result.counts().get("M")); // P2 (deduped)
    assertEquals(4L, result.totalSeen()); // P1, P2, P3, P4
    assertEquals(1L, result.missingSeen()); // P4 has no value
  }

  @Test
  void binsIntegerTallyWithMissingAndUnsortedInput() {
    // insertion order deliberately NOT ascending, to prove the sort step
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("5", 3L);
    counts.put("1", 2L);
    counts.put("3", 1L);
    var tally = new UnionDistribution.Tally(counts, /*totalSeen*/ 8, /*missingSeen*/ 2);

    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin(1).setDisplayRangeMax(6).setBinWidth(2);

    DistributionResult result = UnionDistribution.binnedResult(
        tally, APIVariableType.INTEGER, java.util.Optional.of(spec),
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT);

    // 6 non-missing values fall into width-2 bins starting at 1: [1,3)=2, [3,5)=1, [5,7)=3
    long binnedTotal = result.getHistogramData().stream()
        .mapToLong(b -> b.getValue().longValue()).sum();
    assertEquals(6L, binnedTotal);
    assertEquals(6L, result.getStatistics().getNumVarValues());
    assertEquals(2L, result.getStatistics().getNumMissingCases());
    assertEquals(8L, result.getStatistics().getSubsetSize());
  }

  @Test
  void binsNumberTallyAndComputesMean() {
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("0.5", 1L);
    counts.put("1.5", 2L);
    counts.put("2.5", 1L);
    var tally = new UnionDistribution.Tally(counts, 4, 0);

    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin(0.0).setDisplayRangeMax(3.0).setBinWidth(1.0);

    DistributionResult result = UnionDistribution.binnedResult(
        tally, APIVariableType.NUMBER, java.util.Optional.of(spec),
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT);

    assertEquals(4L, result.getStatistics().getNumVarValues());
    // mean = (0.5 + 1.5*2 + 2.5) / 4 = 1.5
    assertEquals(1.5, ((Number) result.getStatistics().getSubsetMean()).doubleValue(), 1e-9);
  }

  @Test
  void continuousBinningRequiresBinSpec() {
    var tally = new UnionDistribution.Tally(new java.util.HashMap<>(), 0, 0);
    assertThrows(jakarta.ws.rs.BadRequestException.class, () ->
        UnionDistribution.binnedResult(tally, APIVariableType.NUMBER,
            java.util.Optional.empty(),
            org.veupathdb.service.eda.generated.model.ValueSpec.COUNT));
  }
}

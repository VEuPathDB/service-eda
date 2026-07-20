package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.generated.model.VariableSpec;

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
    String pk = entity.getIdColumnDef().getVariableId();
    String sexCol = "sex"; // row-map key for the sex variable == bare variableId
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
}

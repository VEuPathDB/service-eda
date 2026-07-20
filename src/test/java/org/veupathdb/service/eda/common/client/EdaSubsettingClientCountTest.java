package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.common.model.VariableDef;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EdaSubsettingClientCountTest {

  private static Map<String, String> row(String pk, String pkVal) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal); return m;
  }

  @Test
  void unionCountDedupesSharedIds() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    // rows are keyed the way the tabular iterator keys them: qualified "entityId.column"
    String pk = VariableDef.toDotNotation(md.getEntity(TestStudies.ROOT_ID).orElseThrow().getIdColumnDef());

    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, sp) ->
      studyId.equals("studyA")
        ? CloseableIterator.of(List.of(row(pk, "P1"), row(pk, "P2")).iterator())
        : CloseableIterator.of(List.of(row(pk, "P2"), row(pk, "P3")).iterator());

    long count = EdaSubsettingClient.countUnionEntity(md, TestStudies.ROOT_ID, List.of(), opener);
    assertEquals(3L, count); // P2 counted once
  }
}

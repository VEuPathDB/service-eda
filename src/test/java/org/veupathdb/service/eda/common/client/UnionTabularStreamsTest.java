package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionTabularStreamsTest {

  private static Map<String, String> r(String pk, String pkVal, String v) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal); m.put("sex", v); return m;
  }
  private static CloseableIterator<Map<String, String>> s(List<Map<String, String>> rows) {
    return CloseableIterator.of(rows.iterator());
  }

  @Test
  void singleStudyUsesOneOpenerCall() {
    ReferenceMetadata md = new ReferenceMetadata(
      TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F")));
    StreamSpec spec = new StreamSpec("s", "EUPA_1");
    List<String> opened = new ArrayList<>();
    var opener = (UnionTabularStreams.TabularStreamOpener) (studyId, f, sp) -> {
      opened.add(studyId); return s(List.of());
    };
    UnionTabularStreams.open(md, List.of(), spec, opener);
    assertEquals(List.of("studyA"), opened);
  }

  @Test
  void unionMergesPerMemberStreamsWithDedup() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    // rows are keyed the way the tabular iterator keys them: qualified "entityId.column"
    String pk = VariableDef.toDotNotation(md.getEntity(TestStudies.ROOT_ID).orElseThrow().getIdColumnDef());
    StreamSpec spec = new StreamSpec("s", TestStudies.ROOT_ID);

    var opener = (UnionTabularStreams.TabularStreamOpener) (studyId, f, sp) ->
      studyId.equals("studyA")
        ? s(List.of(r(pk, "P1", "F"), r(pk, "P2", "M")))
        : s(List.of(r(pk, "P2", "M"), r(pk, "P3", "F")));

    CloseableIterator<Map<String, String>> merged = UnionTabularStreams.open(md, List.of(), spec, opener);
    List<String> ids = new ArrayList<>();
    while (merged.hasNext()) ids.add(merged.next().get(pk));
    merged.close();
    assertEquals(List.of("P1", "P2", "P3"), ids); // P2 deduped
  }
}

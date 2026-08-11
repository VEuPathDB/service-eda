package org.veupathdb.service.eda.merge;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.UnionTabularStreams;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionMergeIntegrationTest {

  @Test
  void twoStudyUnionMergesChildEntityAndInheritsUnionedAncestor() throws Exception {
    // household (root) shared across members; participant (child) unioned underneath.
    ReferenceMetadata md = TestStudies.householdParticipantUnion("union(studyA,studyB)", List.of("studyA", "studyB"));
    // rows are keyed the way the tabular iterator keys them: qualified "entityId.column"
    String householdPk = TestStudies.HOUSEHOLD_ID + ".household_id";
    String participantPk = TestStudies.PARTICIPANT_ID + ".participant_id";

    // studyA: H1 -> {PA1}; studyB: H1 (shared, agrees) -> {PB1}; H2 -> {PB2}
    // Fixture rows are ordered per-member by the PK sort key that
    // UnionTabularStreams.orderedIdColumnKeys produces (ancestor root-first, then entity PK),
    // since UnionTabularStreams.open k-way-merges assuming each member stream is already sorted.
    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, spec) -> {
      String entityId = spec.getEntityId();
      if (entityId.equals(TestStudies.HOUSEHOLD_ID)) {
        return studyId.equals("studyA")
          ? CloseableIterator.of(List.of(Map.of(householdPk, "H1", "region", "north")).iterator())
          : CloseableIterator.of(List.of(Map.of(householdPk, "H1", "region", "north"),
                                         Map.of(householdPk, "H2", "region", "south")).iterator());
      } else { // participant
        return studyId.equals("studyA")
          ? CloseableIterator.of(List.of(Map.of(householdPk, "H1", participantPk, "PA1")).iterator())
          : CloseableIterator.of(List.of(Map.of(householdPk, "H1", participantPk, "PB1"),
                                         Map.of(householdPk, "H2", participantPk, "PB2")).iterator());
      }
    };

    // merged household stream: H1 deduped, H2 once
    var households = UnionTabularStreams.open(md, List.of(),
      TestStudies.streamSpec(TestStudies.HOUSEHOLD_ID), opener);
    List<String> hIds = new ArrayList<>();
    while (households.hasNext()) hIds.add(households.next().get(householdPk));
    households.close();
    assertEquals(List.of("H1", "H2"), hIds);

    // merged participant stream: three distinct participants, PK-sorted, ancestor pk present
    var participants = UnionTabularStreams.open(md, List.of(),
      TestStudies.streamSpec(TestStudies.PARTICIPANT_ID), opener);
    List<String> pIds = new ArrayList<>();
    while (participants.hasNext()) {
      var r = participants.next();
      assertNotNull(r.get(householdPk)); // ancestor pk carried for join
      pIds.add(r.get(participantPk));
    }
    participants.close();
    assertEquals(List.of("PA1", "PB1", "PB2"), pIds);
  }
}

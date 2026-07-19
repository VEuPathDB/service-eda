package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDefMemberStudiesTest {

  @Test
  void defaultsToEmptyMemberList() {
    EntityDef e = new EntityDef("EUPA_1", "Participant", "participant_id", true);
    assertTrue(e.getMemberStudyIds().isEmpty());
  }

  @Test
  void carriesMemberStudyIds() {
    EntityDef e = new EntityDef("EUPA_1", "Participant", "participant_id", true);
    e.setMemberStudyIds(List.of("studyA", "studyB"));
    assertEquals(List.of("studyA", "studyB"), e.getMemberStudyIds());
  }
}

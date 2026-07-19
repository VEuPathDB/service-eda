package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReferenceMetadataUnionTest {

  @Test
  void unionConstructorTagsEveryEntityWithMembers() {
    // two-entity study so we assert tagging recurses
    var study = TestStudies.twoEntities("union(studyA,studyB)");
    var members = List.of("studyA", "studyB");

    ReferenceMetadata md = new ReferenceMetadata(study, members);

    assertEquals("union(studyA,studyB)", md.getStudyId());
    assertEquals(members, md.getMemberStudyIds());
    // root + child both tagged
    var root = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    var child = md.getEntity(TestStudies.CHILD_ID).orElseThrow();
    assertEquals(members, root.getMemberStudyIds());
    assertEquals(members, child.getMemberStudyIds());
  }

  @Test
  void singleStudyConstructorHasEmptyMembers() {
    var study = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    ReferenceMetadata md = new ReferenceMetadata(study);
    assertTrue(md.getMemberStudyIds().isEmpty());
    assertTrue(md.getEntity("EUPA_1").orElseThrow().getMemberStudyIds().isEmpty());
  }
}

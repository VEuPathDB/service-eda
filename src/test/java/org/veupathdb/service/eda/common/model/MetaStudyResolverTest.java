package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MetaStudyResolverTest {

  @Test
  void plainIdPassesThrough() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    Function<String, APIStudyDetail> fetch = Map.of("studyA", a)::get;

    ReferenceMetadata md = MetaStudyResolver.resolveReferenceMetadata(StudyId.parse("studyA"), fetch);

    assertEquals("studyA", md.getStudyId());
    assertTrue(md.getMemberStudyIds().isEmpty());
  }

  @Test
  void unionBuildsMergedMetadata() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F"));
    APIStudyDetail b = TestStudies.oneEntityOneStringVar("studyB", "EUPA_1", "sex", List.of("M"));
    Function<String, APIStudyDetail> fetch = Map.of("studyA", a, "studyB", b)::get;

    ReferenceMetadata md = MetaStudyResolver.resolveReferenceMetadata(StudyId.parse("union(studyA,studyB)"), fetch);

    assertEquals("union(studyA,studyB)", md.getStudyId());
    assertEquals(List.of("studyA", "studyB"), md.getMemberStudyIds());
  }
}

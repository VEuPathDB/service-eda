package org.veupathdb.service.eda.common.auth;

import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class StudyAccessUnionTest {

  // A tiny seam: confirmPermission delegates member lookups through a functional
  // resolver so the test can stub per-member StudyAccess without a live DB.
  @Test
  void unionRequiresEveryMemberAuthorized() {
    // predicate must actually inspect the resolved StudyAccess so that denyAll()
    // (all permission flags false) is distinguishable from grantAll()
    Predicate<StudyAccess> requiresResultsAll = StudyAccess::allowResultsAll;

    // studyA authorized, studyB not
    StudyAccess.PerStudyAccessResolver resolver = (studyId, userId) ->
      studyId.equals("studyA")
        ? java.util.Optional.of(StudyAccess.grantAll())
        : java.util.Optional.of(StudyAccess.denyAll());

    assertThrows(ForbiddenException.class, () ->
      StudyAccess.confirmPermission(1L, "union(studyA,studyB)", requiresResultsAll, resolver));
  }

  @Test
  void unionPassesWhenAllMembersAuthorized() {
    Predicate<StudyAccess> requiresResultsAll = StudyAccess::allowResultsAll;
    StudyAccess.PerStudyAccessResolver resolver =
      (studyId, userId) -> java.util.Optional.of(StudyAccess.grantAll());

    assertDoesNotThrow(() ->
      StudyAccess.confirmPermission(1L, "union(studyA,studyB)", requiresResultsAll, resolver));
  }

  @Test
  void plainIdUnchanged() {
    Predicate<StudyAccess> allowAll = sa -> true;
    StudyAccess.PerStudyAccessResolver resolver =
      (studyId, userId) -> studyId.equals("studyA")
        ? java.util.Optional.of(StudyAccess.grantAll())
        : java.util.Optional.empty();

    assertDoesNotThrow(() ->
      StudyAccess.confirmPermission(1L, "studyA", allowAll, resolver));
    assertThrows(ForbiddenException.class, () ->
      StudyAccess.confirmPermission(1L, "missing", allowAll, resolver));
  }
}

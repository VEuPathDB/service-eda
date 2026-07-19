package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudyIdTest {

  @Test
  void plainIdIsNotUnion() {
    StudyId id = StudyId.parse("studyA");
    assertFalse(id.isUnion());
    assertEquals(List.of("studyA"), id.getMembers());
    assertEquals("studyA", id.getRawToken());
  }

  @Test
  void plainIdStartingWithUnionPassesThrough() {
    // Only the exact "union(" prefix marks a union; a bare id starting with "union"
    // is an ordinary study id and must pass through unchanged (identity).
    StudyId id = StudyId.parse("union_of_states");
    assertFalse(id.isUnion());
    assertEquals(List.of("union_of_states"), id.getMembers());
  }

  @Test
  void parsesTwoMemberUnion() {
    StudyId id = StudyId.parse("union(studyA,studyB)");
    assertTrue(id.isUnion());
    assertEquals(List.of("studyA", "studyB"), id.getMembers());
    assertEquals("union(studyA,studyB)", id.getRawToken());
  }

  @Test
  void trimsWhitespaceBetweenMembers() {
    StudyId id = StudyId.parse("union( studyA , studyB , studyC )");
    assertEquals(List.of("studyA", "studyB", "studyC"), id.getMembers());
  }

  @Test
  void rejectsSingleMemberUnion() {
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA)"));
  }

  @Test
  void rejectsEmptyMember() {
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA,)"));
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(,studyB)"));
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA, ,studyC)"));
  }

  @Test
  void rejectsEmptyUnion() {
    // union() and whitespace-only inner: no members at all
    BadRequestException empty = assertThrows(BadRequestException.class, () -> StudyId.parse("union()"));
    assertTrue(empty.getMessage().toLowerCase().contains("empty"));
    assertThrows(BadRequestException.class, () -> StudyId.parse("union( )"));
  }

  @Test
  void rejectsUnbalancedParensAndTrailingJunk() {
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA,studyB"));
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA,studyB)x"));
  }

  @Test
  void rejectsDuplicateMembers() {
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(studyA,studyA)"));
  }

  @Test
  void rejectsNonIdentifierMemberChars() {
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(study A,studyB)"));
    assertThrows(BadRequestException.class, () -> StudyId.parse("union(study-A,studyB)"));
  }

  @Test
  void forSingleBypassesParsing() {
    StudyId id = StudyId.forSingle("union(literal)"); // treated as opaque plain id
    assertFalse(id.isUnion());
    assertEquals(List.of("union(literal)"), id.getMembers());
  }
}

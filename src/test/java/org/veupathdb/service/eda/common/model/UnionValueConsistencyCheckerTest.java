package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class UnionValueConsistencyCheckerTest {

  private static CloseableIterator<Map<String, String>> rows(List<Map<String, String>> rows) {
    return CloseableIterator.of(rows.iterator());
  }

  @Test
  void passesWhenSharedInstancesAgree() {
    var md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    // rows are keyed the way the tabular iterator keys them: qualified "entityId.column"
    String pk = VariableDef.toDotNotation(entity.getIdColumnDef());
    String sex = TestStudies.ROOT_ID + ".sex";

    BiFunction<String, String, CloseableIterator<Map<String, String>>> supplier = (studyId, entityId) ->
      studyId.equals("studyA")
        ? rows(List.of(Map.of(pk, "P1", sex, "F"), Map.of(pk, "P2", sex, "M")))
        : rows(List.of(Map.of(pk, "P2", sex, "M"), Map.of(pk, "P3", sex, "F"))); // P2 agrees

    assertDoesNotThrow(() -> UnionValueConsistencyChecker.check(md, entity, supplier));
  }

  @Test
  void passesWhenOneMemberHasManyDistinctInstances() {
    // regression: distinct sequential instances from a single member must not be mistaken for
    // the same instance. The tabular iterator qualifies the PK column key as "entityId.column";
    // if the checker looks it up by the bare column name, every row collapses to a null id and
    // adjacent rows falsely "conflict".
    var md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    String pk = VariableDef.toDotNotation(entity.getIdColumnDef());
    String sex = TestStudies.ROOT_ID + ".sex";

    BiFunction<String, String, CloseableIterator<Map<String, String>>> supplier = (studyId, entityId) ->
      studyId.equals("studyA")
        ? rows(List.of(Map.of(pk, "G10", sex, "F"), Map.of(pk, "G20", sex, "M")))
        : rows(List.of());

    assertDoesNotThrow(() -> UnionValueConsistencyChecker.check(md, entity, supplier));
  }

  @Test
  void rejectsWhenSharedInstanceValueConflicts() {
    var md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    String pk = VariableDef.toDotNotation(entity.getIdColumnDef());
    String sex = TestStudies.ROOT_ID + ".sex";

    BiFunction<String, String, CloseableIterator<Map<String, String>>> supplier = (studyId, entityId) ->
      studyId.equals("studyA")
        ? rows(List.of(Map.of(pk, "P2", sex, "M")))
        : rows(List.of(Map.of(pk, "P2", sex, "F"))); // conflict on P2.sex

    BadRequestException ex = assertThrows(BadRequestException.class,
      () -> UnionValueConsistencyChecker.check(md, entity, supplier));
    assertTrue(ex.getMessage().contains("P2"));
    assertTrue(ex.getMessage().contains(sex));
  }
}

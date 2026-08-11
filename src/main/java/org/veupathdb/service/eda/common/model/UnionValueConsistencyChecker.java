package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.gusdb.fgputil.iterator.CloseableIterator;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Tier 2: confirms that entity instances shared across union members (same entity ID)
 * carry identical native variable values. One-time cost, cached by the caller (see
 * {@link MetaStudyResolver#resolveStudyDetailCached}).
 */
public final class UnionValueConsistencyChecker {

  private UnionValueConsistencyChecker() {}

  public static void check(ReferenceMetadata unionMetadata, EntityDef sharedEntity,
                           BiFunction<String, String, CloseableIterator<Map<String, String>>> streamSupplier) {
    // The tabular stream keys columns in qualified "entityId.column" form (see
    // FilteredResultFactory.getTabularOutputColumns), so the PK must be looked up the same way --
    // a bare variable id never matches, collapsing every row to a null id and faking conflicts.
    String pkKey = VariableDef.toDotNotation(sharedEntity.getIdColumnDef());
    // first-seen values per entity id; a later member's differing value is a conflict
    Map<String, Map<String, String>> firstSeen = new HashMap<>();

    for (String studyId : unionMetadata.getMemberStudyIds()) {
      try (CloseableIterator<Map<String, String>> it = streamSupplier.apply(studyId, sharedEntity.getId())) {
        while (it.hasNext()) {
          Map<String, String> row = it.next();
          String entityId = row.get(pkKey);
          Map<String, String> prior = firstSeen.get(entityId);
          if (prior == null) {
            firstSeen.put(entityId, new HashMap<>(row));
          } else {
            for (Map.Entry<String, String> cell : row.entrySet()) {
              String col = cell.getKey();
              String priorVal = prior.get(col);
              if (priorVal != null && !Objects.equals(priorVal, cell.getValue())) {
                throw new BadRequestException(
                  "Union value conflict on entity '" + sharedEntity.getId() + "' instance '"
                  + entityId + "', variable/column '" + col + "': member studies disagree ('"
                  + priorVal + "' vs '" + cell.getValue() + "'). Studies: "
                  + unionMetadata.getMemberStudyIds());
              }
            }
          }
        }
      } catch (BadRequestException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException("Failed streaming member '" + studyId + "' for Tier-2 check", e);
      }
    }
  }
}

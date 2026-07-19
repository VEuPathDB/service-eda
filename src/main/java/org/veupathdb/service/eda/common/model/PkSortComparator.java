package org.veupathdb.service.eda.common.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Reproduces the DB ORDER BY collation for PK merge keys. The subset service orders
 * tabular output by ancestor PKs (root->leaf) then entity PK under a CASE-INSENSITIVE
 * collation (verified: FilteredResultFactory.generateTabularOrderByClause;
 * DifferentialExpressionPlugin comment). EDA PKs are ASCII stable-ids, so
 * String.CASE_INSENSITIVE_ORDER matches; a case tie-break keeps the order total.
 *
 * MUST-VERIFY (spec §8.4): confirm against the live DB collation and pin here.
 */
public final class PkSortComparator {

  private PkSortComparator() {}

  public static final Comparator<String> COLLATION =
    Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER);

  public static Comparator<Map<String, String>> forKeyColumns(List<String> orderedIdColumnKeys) {
    return (r1, r2) -> {
      for (String col : orderedIdColumnKeys) {
        int c = COLLATION.compare(r1.get(col), r2.get(col));
        if (c != 0) return c;
        // Deterministic tie-break: if case-insensitive comparison is equal, use case-sensitive ordering
        int tieBreak = Comparator.<String>nullsFirst(Comparator.naturalOrder()).compare(r1.get(col), r2.get(col));
        if (tieBreak != 0) return tieBreak;
      }
      return 0;
    };
  }
}

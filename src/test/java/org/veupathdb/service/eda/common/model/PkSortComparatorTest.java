package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PkSortComparatorTest {

  private static Map<String, String> row(String... kv) {
    Map<String, String> m = new LinkedHashMap<>();
    for (int i = 0; i < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
    return m;
  }

  @Test
  void ordersByAncestorThenEntityPk() {
    var cmp = PkSortComparator.forKeyColumns(List.of("household_id", "participant_id"));
    var a = row("household_id", "H1", "participant_id", "P2");
    var b = row("household_id", "H2", "participant_id", "P1");
    assertTrue(cmp.compare(a, b) < 0); // H1 < H2 regardless of participant pk
  }

  @Test
  void caseInsensitiveMatchesDbCollation() {
    var cmp = PkSortComparator.forKeyColumns(List.of("id"));
    // 'Customer' and 'customer' collate equal under case-insensitive ordering
    assertEquals(0, PkSortComparator.COLLATION.compare("Customer001", "customer001"));
    // ordering across case: 'a' vs 'B' -> 'a' before 'B' (case-insensitive)
    assertTrue(cmp.compare(row("id", "apple"), row("id", "Banana")) < 0);
  }

  @Test
  void deterministicTieBreakOnCaseDifference() {
    var cmp = PkSortComparator.forKeyColumns(List.of("id"));
    // equal under CI but different bytes -> stable, non-zero, and symmetric
    int ab = cmp.compare(row("id", "X1"), row("id", "x1"));
    int ba = cmp.compare(row("id", "x1"), row("id", "X1"));
    assertEquals(-Integer.signum(ba), Integer.signum(ab));
  }
}

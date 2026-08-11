package org.veupathdb.service.eda.common.model;

import org.gusdb.fgputil.iterator.CloseableIterator;

import java.util.*;
import java.util.function.Function;

/**
 * Merges N PK-sorted member row streams into one PK-sorted stream, deduplicating by
 * entity id at the merge frontier (first member wins). Values are trusted (Tier 2).
 */
public final class KWayMergeIterator implements CloseableIterator<Map<String, String>> {

  private static final class Head {
    final int memberIndex;
    final CloseableIterator<Map<String, String>> it;
    Map<String, String> current;
    Head(int memberIndex, CloseableIterator<Map<String, String>> it) {
      this.memberIndex = memberIndex; this.it = it;
      this.current = it.hasNext() ? it.next() : null;
    }
    void advance() { current = it.hasNext() ? it.next() : null; }
  }

  private final List<Head> _heads = new ArrayList<>();
  private final Comparator<Map<String, String>> _cmp;
  private final Function<Map<String, String>, String> _dedupKey;
  private Map<String, String> _next;
  private String _lastEmittedKey; // dedup across successive emissions

  public KWayMergeIterator(List<CloseableIterator<Map<String, String>>> members,
                           Comparator<Map<String, String>> keyComparator,
                           Function<Map<String, String>, String> dedupKey) {
    _cmp = keyComparator;
    _dedupKey = dedupKey;
    // Priming a Head pulls that member's first row, which for a real subsetting iterator
    // opens a DB cursor / file stream and can THROW. This iterator owns the member iterators
    // (its close() closes them), so if any prime fails, close EVERY member we were handed —
    // the heads already built AND the members past the failure point that were opened but
    // never reached — otherwise those cursors leak (this ctor's result is never returned).
    try {
      for (int i = 0; i < members.size(); i++) _heads.add(new Head(i, members.get(i)));
      _next = computeNext();
    } catch (RuntimeException e) {
      closeAllQuietly(members);
      throw e;
    }
  }

  private static void closeAllQuietly(List<CloseableIterator<Map<String, String>>> its) {
    for (CloseableIterator<?> it : its) {
      try { it.close(); } catch (Exception ignored) {}
    }
  }

  private Map<String, String> computeNext() {
    while (true) {
      // pick the smallest head by comparator; tie -> lowest member index (first member wins)
      Head min = null;
      for (Head h : _heads) {
        if (h.current == null) continue;
        if (min == null) { min = h; continue; }
        int c = _cmp.compare(h.current, min.current);
        if (c < 0 || (c == 0 && h.memberIndex < min.memberIndex)) min = h;
      }
      if (min == null) return null; // all exhausted

      Map<String, String> row = min.current;
      String key = _dedupKey.apply(row);
      min.advance();

      if (key != null && key.equals(_lastEmittedKey)) {
        continue; // duplicate id already emitted (from this or another member) -> skip
      }
      _lastEmittedKey = key;
      return row;
    }
  }

  @Override public boolean hasNext() { return _next != null; }

  @Override public Map<String, String> next() {
    if (_next == null) throw new NoSuchElementException("No rows remain.");
    Map<String, String> out = _next;
    _next = computeNext();
    return out;
  }

  @Override public void close() throws Exception {
    Exception first = null;
    for (Head h : _heads) {
      try { h.it.close(); } catch (Exception e) { if (first == null) first = e; }
    }
    if (first != null) throw first;
  }
}

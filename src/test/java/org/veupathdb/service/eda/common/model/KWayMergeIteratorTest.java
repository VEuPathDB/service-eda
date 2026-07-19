package org.veupathdb.service.eda.common.model;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class KWayMergeIteratorTest {

  private static Map<String, String> r(String id, String v) {
    var m = new LinkedHashMap<String, String>(); m.put("id", id); m.put("v", v); return m;
  }
  private static CloseableIterator<Map<String, String>> s(Map<String, String>... rows) {
    return CloseableIterator.of(Arrays.asList(rows).iterator());
  }
  private static final Comparator<Map<String, String>> CMP = PkSortComparator.forKeyColumns(List.of("id"));
  private static final Function<Map<String, String>, String> KEY = row -> row.get("id");

  private static List<String> drain(CloseableIterator<Map<String, String>> it) throws Exception {
    List<String> ids = new ArrayList<>();
    while (it.hasNext()) ids.add(it.next().get("id"));
    it.close();
    return ids;
  }

  /** Wraps a delegate iterator, flipping `closed` when close() is called. */
  private static CloseableIterator<Map<String, String>> closeTracking(
      CloseableIterator<Map<String, String>> delegate, AtomicBoolean closed) {
    return new CloseableIterator<>() {
      public boolean hasNext() { return delegate.hasNext(); }
      public Map<String, String> next() { return delegate.next(); }
      public void close() throws Exception { closed.set(true); delegate.close(); }
    };
  }

  /** Simulates a member whose first-row pull fails (e.g. a cursor open that throws). */
  private static CloseableIterator<Map<String, String>> throwsOnFirstPull() {
    return new CloseableIterator<>() {
      public boolean hasNext() { throw new RuntimeException("boom on first pull"); }
      public Map<String, String> next() { throw new RuntimeException("boom"); }
      public void close() {}
    };
  }

  @Test
  void mergesDisjointIdsInSortOrder() throws Exception {
    var it = new KWayMergeIterator(List.of(s(r("A", "1"), r("C", "1")), s(r("B", "2"), r("D", "2"))), CMP, KEY);
    assertEquals(List.of("A", "B", "C", "D"), drain(it));
  }

  @Test
  void dedupsOverlappingIdsFirstMemberWins() throws Exception {
    var m0 = s(r("A", "fromA"), r("B", "fromA"));
    var m1 = s(r("B", "fromB"), r("C", "fromB"));
    var it = new KWayMergeIterator(List.of(m0, m1), CMP, KEY);
    var rows = new ArrayList<Map<String, String>>();
    while (it.hasNext()) rows.add(it.next());
    it.close();
    assertEquals(List.of("A", "B", "C"), rows.stream().map(x -> x.get("id")).collect(Collectors.toList()));
    // B emitted once, first member (m0) wins
    assertEquals("fromA", rows.get(1).get("v"));
  }

  @Test
  void handlesThreeWayOverlapAndEmptyMembers() throws Exception {
    var it = new KWayMergeIterator(List.of(
      s(r("A", "1"), r("B", "1")),
      s(),                         // empty member
      s(r("B", "2"), r("B", "2"))  // duplicate within a member too
    ), CMP, KEY);
    assertEquals(List.of("A", "B"), drain(it));
  }

  @Test
  void closesAllMembersWhenConstructionFails() {
    AtomicBoolean m0Closed = new AtomicBoolean(false);
    AtomicBoolean m2Closed = new AtomicBoolean(false);
    var m0 = closeTracking(s(r("A", "1")), m0Closed); // primed before the failure
    var m1 = throwsOnFirstPull();                     // fails while the ctor primes it
    var m2 = closeTracking(s(r("C", "1")), m2Closed); // opened, never reached by the ctor loop

    assertThrows(RuntimeException.class, () -> new KWayMergeIterator(List.of(m0, m1, m2), CMP, KEY));

    assertTrue(m0Closed.get(), "member primed before the failure must be closed");
    assertTrue(m2Closed.get(), "member opened but not yet primed must be closed");
  }
}

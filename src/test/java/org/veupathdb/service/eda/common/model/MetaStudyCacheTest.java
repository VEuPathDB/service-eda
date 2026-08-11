package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MetaStudyCacheTest {

  @Test
  void buildsOnceThenServesFromCache() {
    MetaStudyCache cache = new MetaStudyCache();
    StudyId id = StudyId.parse("union(studyA,studyB)");
    Function<String, Long> versions = Map.of("studyA", 10L, "studyB", 20L)::get;
    APIStudyDetail built = TestStudies.oneEntityOneStringVar("union(studyA,studyB)", "EUPA_1", "sex", List.of("F"));
    AtomicInteger calls = new AtomicInteger();

    APIStudyDetail r1 = cache.getOrBuild(id, versions, () -> { calls.incrementAndGet(); return built; });
    APIStudyDetail r2 = cache.getOrBuild(id, versions, () -> { calls.incrementAndGet(); return built; });

    assertSame(r1, r2);
    assertEquals(1, calls.get());
  }

  @Test
  void rebuildsWhenMemberVersionChanges() {
    MetaStudyCache cache = new MetaStudyCache();
    StudyId id = StudyId.parse("union(studyA,studyB)");
    APIStudyDetail built = TestStudies.oneEntityOneStringVar("union(studyA,studyB)", "EUPA_1", "sex", List.of("F"));
    AtomicInteger calls = new AtomicInteger();

    cache.getOrBuild(id, Map.of("studyA", 10L, "studyB", 20L)::get, () -> { calls.incrementAndGet(); return built; });
    cache.getOrBuild(id, Map.of("studyA", 10L, "studyB", 21L)::get, () -> { calls.incrementAndGet(); return built; });

    assertEquals(2, calls.get());
  }

  @Test
  void cachesKnownInvalidVerdict() {
    MetaStudyCache cache = new MetaStudyCache();
    StudyId id = StudyId.parse("union(studyA,studyB)");
    Function<String, Long> versions = Map.of("studyA", 10L, "studyB", 20L)::get;
    AtomicInteger calls = new AtomicInteger();

    for (int i = 0; i < 2; i++) {
      BadRequestException ex = assertThrows(BadRequestException.class, () ->
        cache.getOrBuild(id, versions, () -> {
          calls.incrementAndGet();
          throw new BadRequestException("incompatible");
        }));
      assertEquals("incompatible", ex.getMessage());
    }
    assertEquals(1, calls.get()); // builder invoked only once; second call served the cached 400
  }

  @Test
  void concurrentRequestsForSameTokenBuildOnce() throws Exception {
    MetaStudyCache cache = new MetaStudyCache();
    StudyId id = StudyId.parse("union(studyA,studyB)");
    Function<String, Long> versions = Map.of("studyA", 10L, "studyB", 20L)::get;
    APIStudyDetail built = TestStudies.oneEntityOneStringVar("union(studyA,studyB)", "EUPA_1", "sex", List.of("F"));
    AtomicInteger calls = new AtomicInteger();

    int threads = 8;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    CyclicBarrier startLine = new CyclicBarrier(threads); // release all callers simultaneously
    List<Future<APIStudyDetail>> results = new ArrayList<>();
    for (int i = 0; i < threads; i++) {
      results.add(pool.submit(() -> {
        startLine.await();
        return cache.getOrBuild(id, versions, () -> {
          calls.incrementAndGet();
          try { Thread.sleep(50); } catch (InterruptedException ignored) {}
          return built;
        });
      }));
    }
    for (Future<APIStudyDetail> f : results) assertSame(built, f.get());
    pool.shutdown();
    assertEquals(1, calls.get()); // the expensive build ran exactly once despite 8 concurrent callers
  }
}

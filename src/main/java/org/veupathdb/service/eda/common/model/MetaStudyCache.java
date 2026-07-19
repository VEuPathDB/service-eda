package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Caches merged union metadata (and known-invalid verdicts) keyed by the union token plus
 * each member's data-version. Concurrency model mirrors MetadataCache: a single monitor guards
 * the map AND the build runs under that lock, so the expensive Tier-1+Tier-2 build for a given
 * token runs at most once even under concurrent requests — no duplicated "spinner cost".
 *
 * No deadlock with MetadataCache: the builder calls down into MetadataCache.getStudyById (a
 * different monitor); MetadataCache never calls back into MetaStudyCache, so there is no cycle.
 */
public final class MetaStudyCache {

  private sealed interface Entry permits Valid, Invalid {}
  private record Valid(APIStudyDetail detail) implements Entry {}
  private record Invalid(String message) implements Entry {}

  // one slot per union token; replaced when the version-suffixed key changes.
  // guarded by this instance's monitor (all access is via synchronized methods).
  private final Map<String, KeyedEntry> _byToken = new HashMap<>();
  private record KeyedEntry(String key, Entry entry) {}

  public String cacheKey(StudyId id, Function<String, Long> versionOf) {
    StringBuilder sb = new StringBuilder(id.getRawToken()).append('@');
    for (String member : id.getMembers()) sb.append(member).append('=').append(versionOf.apply(member)).append(';');
    return sb.toString();
  }

  public synchronized APIStudyDetail getOrBuild(StudyId id, Function<String, Long> versionOf, Supplier<APIStudyDetail> builder) {
    String key = cacheKey(id, versionOf);
    KeyedEntry existing = _byToken.get(id.getRawToken());
    if (existing != null && existing.key().equals(key)) {
      return unwrap(existing.entry());   // fresh hit — valid result or cached 400
    }
    // miss or stale data-version → build exactly once under the lock
    Entry entry;
    try {
      entry = new Valid(builder.get());
    } catch (BadRequestException e) {
      entry = new Invalid(e.getMessage());   // cache the 400 so a repeated bad union doesn't re-scan
    }
    // NOTE: an unexpected (non-BadRequest) RuntimeException propagates and is NOT cached,
    // so a transient failure (e.g. DB blip) is retried on the next request.
    _byToken.put(id.getRawToken(), new KeyedEntry(key, entry));
    return unwrap(entry);
  }

  private APIStudyDetail unwrap(Entry entry) {
    if (entry instanceof Valid v) return v.detail();
    throw new BadRequestException(((Invalid) entry).message());
  }
}

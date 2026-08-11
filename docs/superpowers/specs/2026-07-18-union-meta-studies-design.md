# Union meta-studies — design spec

**Date:** 2026-07-18
**Branch:** `explore-study-merge`
**Status:** Approved for planning
**Related:** `docs/the-case-for-dynamic-runtime-meta-studies.md`

## 1. Summary

Allow two or more EDA studies with identical entity/variable structure to be
combined at runtime into a single "union meta-study" that behaves like an
ordinary study for metadata, subsetting, merging, and visualization. The
combination is dynamic (nothing is persisted as a new study). This spec covers
the **union** case only; the document's second case (**extension**) is out of
scope but the identity format and metadata model reserve room for it.

Motivating use case: user-uploaded longitudinal data arriving in separate
batches (e.g. one dataset per year). The same participants recur across batches,
so their root/ancestor records must be treated as one instance while their child
records (observations, visits, purchases) union together.

## 2. Union semantics (decided)

**Shared-key merge.** The same entity ID across member studies denotes the *same*
instance:

- Root/ancestor entity instances that appear in more than one member are
  **deduplicated by entity ID** (emitted once).
- Child entity instances of a deduped parent are **unioned** underneath it.
- If a deduplicated instance's variable values **disagree** between members, that
  is an error (see Tier 2 validation, §6).

This is *not* disjoint concatenation — IDs are a shared namespace across members,
not namespaced per study.

## 3. Identity: the `union(...)` token (decided)

A union is represented as a **scalar `studyId` string** of the form:

```
union(studyA,studyB,studyC)
```

- Valid anywhere a `studyId` appears today — URL path fragment *and* request-body
  field alike.
- A normal single-study request (`"studyA"`) is unchanged and takes today's exact
  code path.
- The `union(...)` wrapper is explicit (a bare `studyA` can never be mistaken for
  a union), reserves room for a future `extension(...)` type, and signals to
  server code which construction path to take.
- Members are alphanumeric/underscore study IDs; parentheses and commas are legal
  in URL path segments so no percent-encoding is required in practice.

### 3.1 `StudyId` parsing boundary

Introduce a small `StudyId` value-type / parser used wherever a raw `studyId`
string is first resolved to a study. Responsibilities:

- Detect the `union(...)` wrapper; split members on comma; trim whitespace.
- Reject malformed tokens (unbalanced parens, empty members, trailing junk) →
  `400`.
- Reject a single-member union `union(x)` → `400` (client should use the plain
  id).
- For a plain (non-union) id, behave as an identity pass-through.

Everything downstream that treats `studyId` as opaque is untouched; only the
sites that *resolve* a `studyId` to a study/schema become union-aware.

## 4. Access control (security-critical)

Every site that authorizes a `studyId` must **resolve the token and require
permission on all member studies** (deny if any member is unauthorized). No code
path may authorize the literal `union(...)` string as if it were one study.

- Audit all `@Authenticated` / permission-check sites that consume `studyId`.
- A union is authorized iff every member is authorized; otherwise the existing
  `403`/`404` convention applies.

## 5. Metadata model changes

Current state (verified):

- `ReferenceMetadata`
  (`src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java`)
  holds a single `_studyId` (line ~29) and a single `_entityTree`, built by one
  constructor `ReferenceMetadata(APIStudyDetail)` (lines ~32–39).
- `EntityDef`
  (`src/main/java/org/veupathdb/service/eda/common/model/EntityDef.java`) has
  **no** study field; study→schema/database resolution is driven entirely by the
  single request `studyId` in the subsetting layer.
- The raw metadata source is `EdaSubsettingClient.getStudy(studyId)` →
  `StudyResolver.getStudyById(studyId)` (single provider per study, chosen by
  `StudySourceType`).

Changes:

1. **`EntityDef` gains a member-study list** — the set of member studies that
   contribute rows to this entity. For a pure union that is all members; the
   field is a **list** so the deferred `extension(...)` case (a leaf entity from
   one member) needs no further model change.
2. **`ReferenceMetadata` gains a multi-study constructor** taking the parsed
   member list. It fetches each member's `APIStudyDetail`, verifies compatibility,
   and builds one merged tree (§6 Tier 1). The existing single-study constructor
   remains and is the common path.
3. **Audit single-study assumptions.** Every call site that assumes
   `metadata.getStudyId()` is one study and uses it to resolve a schema/provider
   (e.g. `EdaSubsettingClient.getTabularDataIterator`, `getSubsetCount`,
   `getCategoricalDistribution`, `getVocabByRootEntity`;
   `StudiesService.resolveSchema` and its callers) must be found and made
   union-aware or proven single-study-only.

## 6. Metadata merge — two-tier validation (decided)

Building merged metadata for a `union(...)` token performs **both** tiers and is
cached (§7). Both tiers run at metadata-build time so all union-specific failures
surface on the **study-metadata request** (the client's "Visualize and analyze"
click), before any visualization runs.

### Tier 1 — structural merge + summary merge (cheap, no row scan)

- **Structural compatibility:** entity-tree shape, entity IDs, and each entity's
  variable set / type / units / dataShape must match across all members. Any
  divergence → `400` naming what differs.
- **Vocabulary merge:** categorical variable vocabularies become the **union** of
  member value sets (deduplicated, order normalized).
- **Range merge:** continuous/date variable ranges become `min(mins)` /
  `max(maxes)`; distinct-value counts and display bounds recomputed.
- Cheap because it combines per-study *summaries* already present in each study's
  metadata — it does not read row data.

### Tier 2 — deep value-consistency check (expensive, once, cached)

- Stream every member's full data for shared ancestor/root entities, dedup by
  entity ID, and confirm that a deduplicated instance's variable values **agree**
  across members.
- Disagreement → `400` identifying the conflicting entity / variable / studies.
- This is the one-time "spinner cost"; cached thereafter (§7).

### Hot-path trust

Time-sensitive data calls (tabular, plots, counts, distributions) do **only cheap
ID-based dedup** in the k-way merge — drop a row whose ID already appeared — and
**trust** value identity because Tier 2 already vouched for it. No per-row value
comparison in the hot path.

## 7. Caching

- Merged metadata (Tier 1 + Tier 2 result) is stored in the existing
  `MetadataCache`, keyed by **the union token plus each member's data-version**
  (`lastModified`). It re-runs only when a member's data changes.
- Consider caching a **known-invalid** result (also keyed by data-version) so a
  repeatedly-requested incompatible union does not re-scan every time; on a hit
  return the same `400`.
- VDI/user-study `lastModified` must be a reliable invalidation signal, since
  user-uploaded longitudinal data is the primary use case. Verify its semantics.

## 8. Data path — path-agnostic k-way merge (decided)

### 8.1 The seam

The insertion point is where a single per-entity stream is produced:

- `merge/core/request/MergedTabularRequestResources.kt#getSubsettingTabularStream`
  (the only place the single `metadata.studyId` is used to open a stream), and/or
- `common/client/EdaSubsettingClient.java#getTabularDataIterator(studyId, filters,
  spec)` — the in-process subsetting fetch that already takes `studyId` as an
  explicit parameter.

`StreamSpec` carries only entity + variables (no studyId), so no downstream
signature changes are needed; the union logic loops over member studyIds.

### 8.2 The merge

For a union request, for the requested entity:

1. Open each member study's ordinary tabular iterator. Each member independently
   resolves to the **binary** or **SQL** path via existing logic
   (`StudiesService.shouldRunFileBasedSubsetting`) — **both paths emit rows sorted
   by PK** (ancestor PKs root→leaf, then entity string PK), verified below.
2. **K-way merge** the N iterators on that PK sort key, with **dedup**: when the
   same entity ID arrives from multiple members, emit once (first member wins;
   values are trusted per Tier 2).
3. The output is a single PK-sorted `CloseableIterator<Map<String,String>>` — the
   exact shape the merge layer already consumes.

Because the merged stream preserves PK ordering, **`StreamingEntityNode`'s
ancestor-inheritance and reduction joins keep working unchanged** — this is the
linchpin that lets the entire join / derived-variable / reduction layer stay
untouched.

### 8.3 Why both paths merge identically (verified)

The external `tool-eda-file-dumper` writes `ids_map`/`var_*` by running the
**same** `FilteredResultFactory.produceTabularSubset` →
`generateTabularOrderByClause` used by the SQL read path
(`StudyDumper.java:120`; `FilteredResultFactory.java:548,881-888`). The numeric
id-index is the row's position **after** that PK sort, so binary front-to-back
order == SQL `ORDER BY` order.

**Real PKs vs. serial integers:** the real string PKs live **only** in the
`ids_map` file. The `var_*` and `ancestors` files reference rows by the
**study-local serial id-index** (position). That index is *not* comparable across
studies, so **the merge must happen at or after the decode step** (on the string
PK in the decoded `Map<String,String>`), never on the raw integer indexes.

### 8.4 Comparator / collation (must-verify)

The Java merge comparator must reproduce the database's `ORDER BY` collation on
the PK columns. EDA PKs are ASCII stable-IDs (`Customer001`), so a plain
lexicographic compare almost certainly matches — but a mismatch would misalign
dedup and joins and corrupt output. The implementation must verify the DB
collation and pin the comparator to it (with a test asserting agreement).

### 8.5 Count / distribution / vocab

`count`, `distribution`, and `root-vocab` endpoints get union-aware behavior:
counts sum over the merged (deduped) stream; distributions and vocabularies
aggregate across members. These already funnel through the same subsetting client
seam.

## 9. Error handling (summary)

| Condition | Response | Where |
|---|---|---|
| Malformed `union(...)` token | `400` | Parse boundary (§3.1) |
| Single-member union | `400` | Parse boundary |
| Any member unauthorized | `403`/`404` | Access control (§4) |
| Structural incompatibility | `400` naming divergence | Tier 1 (§6) |
| Value conflict on dedup | `400` naming entity/variable/studies | Tier 2 (§6) |

All union-specific failures surface on the **study-metadata request**.

## 10. Backwards compatibility & API

- **No RAML field changes required** — `studyId` stays a string everywhere;
  codegen is untouched and no new generated types are needed for v1.
- Existing single-study requests are unchanged in behavior.
- Optional: document the `union(...)` grammar in the API spec prose.
- The study-metadata endpoint `GET /studies/{study-id}` returns the **merged**
  entity tree (with merged vocabularies/ranges) when given a `union(...)` token.

## 11. Explicitly deferred (designed-for, not built)

- **Compute** (`/computes/*`, async RabbitMQ + S3): the identity token and merged
  metadata already flow through, but job identity / cache-keying and the worker's
  own data access are out of v1 scope. The plan notes the extension points so
  compute can be added without rework.
- **`extension(...)` meta-studies:** the `EntityDef` member-list field and the
  token grammar reserve room; no extension logic is built.
- **Binary-specific cross-study acceleration:** unnecessary — the path-agnostic
  merge already uses the binary path whenever a member has files.

## 12. Testing strategy

- **Unit:** token parser (valid / malformed / single-member); merge comparator vs.
  DB collation; k-way merge + dedup (disjoint IDs, overlapping IDs, three-way
  overlap, empty members, one member empty).
- **Tier 1:** structural-mismatch rejections; vocabulary-union and range-merge
  correctness.
- **Tier 2:** value-agreement pass; value-conflict rejection; cache keying by
  data-version (invalidation on member change).
- **Integration:** a two-study union producing a known merged tabular result with
  one binary-backed and one SQL-backed member; parent-child join correctness
  across the union; count/distribution correctness over a union; access control on
  mixed-permission members.

## 13. Key risks / must-verifies

1. **Collation match** between the Java comparator and DB `ORDER BY` (corruption
   risk if wrong) — §8.4.
2. **Tier 2 cost** on very large unions — bounded by caching, but first-call
   latency should be measured; consider a size guard and telemetry.
3. **`getStudyId()` / single-study audit** — every resolve site must be found and
   made union-aware or proven safe — §5.3.
4. **VDI/user-study data-version semantics** for cache keying — §7.
5. **Access-control completeness** — no path may authorize the literal token — §4.

## 14. Key files (verified references)

- `src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java`
  — single→multi-study constructor; entity-tree builder.
- `src/main/java/org/veupathdb/service/eda/common/model/EntityDef.java`
  — add member-study list.
- `src/main/kotlin/org/veupathdb/service/eda/merge/core/request/RequestResources.kt`
  (`ReferenceMetadata(EdaSubsettingClient.getStudy(studyId))`) and
  `MergedTabularRequestResources.kt#getSubsettingTabularStream` — the merge seam.
- `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java`
  — `getTabularDataIterator` / `getSubsetCount` / `getCategoricalDistribution` /
  `getVocabByRootEntity` (per-request studyId resolution).
- `src/main/java/org/veupathdb/service/eda/merge/core/stream/StreamingEntityNode.java`
  — ancestor/reduction joins (rely on PK-sorted streams; unchanged).
- `lib-eda-subsetting`: `FilteredResultFactory.java`
  (`generateTabularOrderByClause`, `produceTabularSubset`), `StudiesService.java`
  (`resolveSchema`, `shouldRunFileBasedSubsetting`), `StudyResolver.java`,
  `BinaryFilesManager.java`.
- External (ordering evidence, not modified): `tool-eda-file-dumper`
  (`StudyDumper.java`, `IdFilesDumper*`).

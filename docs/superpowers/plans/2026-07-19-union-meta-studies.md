# Union Meta-Studies Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let two or more EDA studies with identical entity/variable structure be combined at runtime into a single `union(...)` meta-study that behaves like an ordinary study for metadata, access control, subsetting, merging, and visualization — with nothing persisted.

**Architecture:** A `union(studyA,studyB)` token is a scalar `studyId` string. A new `StudyId` value-type parses it at the resolve boundary. Access control resolves the token and requires permission on **every** member. Tier-1 metadata merge (structural compatibility + vocabulary-union + range-merge) runs **once** at the `APIStudyDetail` level, producing a merged `APIStudyDetail` whose `id` is the union token; that same merged detail feeds both the `GET /studies/{study-id}` endpoint and `ReferenceMetadata` construction. `ReferenceMetadata`/`EntityDef` gain a member-study list so the data path knows which studies to stream. The data path opens one ordinary per-member tabular iterator per member and **k-way merges** them on the PK sort key (case-insensitive collation) with **dedup by entity ID**, yielding the exact `CloseableIterator<Map<String,String>>` the merge layer already consumes — so the join/reduction/derived-variable layer stays untouched. Tier-2 deep value-consistency and the merged metadata are cached, keyed by the union token plus each member's `lastModified`.

**Tech Stack:** Java 21 + Kotlin, Gradle 8.7, JAX-RS/Jersey, JUnit 5 (Jupiter) + Mockito 5 + Awaitility, `org.gusdb.fgputil.iterator.CloseableIterator` (FgpUtil), lib-eda-subsetting (`FilteredResultFactory`, `StudyResolver`, `BinaryFilesManager`).

## Global Constraints

- **No RAML/API field changes.** `studyId` stays a `String` everywhere; no codegen changes, no new generated types. The `union(...)` grammar lives entirely in hand-written code.
- **Single-study requests unchanged.** A bare `studyA` must take today's exact code path with identical behavior. Every union branch is gated on "is this token a union?".
- **Security invariant:** no code path may authorize the literal `union(...)` string as one study. A union is authorized **iff every member is authorized**; otherwise the existing `403`/`404` convention applies.
- **Ordering invariant:** both the SQL and binary read paths emit rows sorted by ancestor PKs (root→leaf) then entity PK, under the DB's **case-insensitive** collation (verified: `FilteredResultFactory.generateTabularOrderByClause`; `DifferentialExpressionPlugin.java:128-130` comment). The Java merge comparator MUST reproduce that collation. Never use raw `String.compareTo` for PK merge keys.
- **All union-specific failures surface on the study-metadata request** (`GET /studies/{study-id}` or the merge-service metadata request), before any visualization runs.
- **Tests:** JUnit 5 (`org.junit.jupiter.api`), Mockito (`@ExtendWith(MockitoExtension.class)`), run with `./gradlew test`. Tests mirror main packages under `src/test/java/...`.
- **Build check between tasks:** `./gradlew compileJava compileKotlin` must pass before a task is considered done.

## Verified reference facts (do not re-derive)

- `ReferenceMetadata` (`common/model/ReferenceMetadata.java`) has exactly ONE constructor `ReferenceMetadata(APIStudyDetail)`; fields `private final String _studyId` (l.29), `private final TreeNode<EntityDef> _entityTree` (l.30); tree built by `private static TreeNode<EntityDef> buildEntityTree(APIEntity, List<VariableDef>)` (l.146-254). `getStudyId()` l.265. There is **no** `getEntityTree()` accessor. `getTabularIdColumns(EntityDef)` l.351-358 returns the entity's ordered id `VariableDef`s. `getEntity(String)` l.269-274 → `Optional<EntityDef>`. `getAncestors(EntityDef)` l.324-329.
- `EntityDef` (`common/model/EntityDef.java`) constructor `EntityDef(String id, String displayName, String idColumnName, Boolean isManyToOneWithParent)` (l.41-51); has `getId()`, `getIdColumnDef()` (returns `VariableDef`), `getVariables()`, `getCollections()`. No `_studyId` field today. No `getDisplayName()` accessor.
- `EdaSubsettingClient` (`common/client/EdaSubsettingClient.java`), all static:
  - `getStudy(String studyId)` l.37-39 → `APIStudyDetail`.
  - `getTabularDataIterator(String studyId, List<APIFilter> variableFilters, StreamSpec streamSpec)` l.53-81 → `CloseableIterator<Map<String,String>>`.
  - `getSubsetCount(ReferenceMetadata metadata, String entityId, List<APIFilter> subsetFilters)` l.90-92 → `long`, uses `metadata.getStudyId()`.
  - `getCategoricalDistribution(ReferenceMetadata, VariableSpec, List<APIFilter>, ValueSpec)` l.94-120, uses `metadata.getStudyId()`.
  - `getVocabByRootEntity(ReferenceMetadata, VariableSpec, List<APIFilter>)` l.122-140, uses `metadata.getStudyId()`.
- `StudyAccess.confirmPermission(long userId, String studyId, Predicate<StudyAccess> pred)` at `common/auth/StudyAccess.java:56-61` → `DatasetAccessClient.getStudyAccessByStudyId(studyId, userId).filter(pred).orElseThrow(ForbiddenException::new)`. This is the single central permission chokepoint used by `merge/ServiceExternal.java:98`, `data/service/AppsService.java:194`, and `subset/service/StudiesService.checkPerms` (l.493-496). `download/Service.java:94-104` uses a **separate** `checkPermsAndFetchDatasetInfo(String studyId, ...)` path.
- `MergedTabularRequestResources.getSubsettingTabularStream(spec)` (`merge/core/request/MergedTabularRequestResources.kt:61-74`) is the only place `metadata.studyId` is bound to an opened stream; it calls `EdaSubsettingClient.getTabularDataIterator(metadata.studyId, subsetFilters, spec)`. `StreamSpec` carries no studyId. Rows are `Map<String,String>` keyed by (dot-notation) column names.
- `RequestResources.kt:30`: `val metadata = ReferenceMetadata(EdaSubsettingClient.getStudy(studyId))`.
- `StudiesService.getStudiesByStudyId(String studyId)` (`subset/service/StudiesService.java:90-98`) = the `GET /studies/{study-id}` handler: `checkPerms` → `getStudyById` → `ApiConversionUtil.getApiStudyDetail(study)` → respond. `resolveSchema(Study)` l.498-503. `checkPerms(ContainerRequest, String, Predicate)` l.493-496.
- Per-member data version = `study.getLastModified().getTime()` (epoch millis) — same value `BinaryFilesManager` writes to the `DONE` file and validates in `studyHasCompatibleFiles`. `StudyOverview.getLastModified()` returns a `Date`. `MetadataCache` keys lib `Study` by studyId, refreshes every 5 min, invalidates via `isOutOfDate` (compares `getLastModified()`).
- `CloseableIterator<T>` (`FgpUtil .../iterator/CloseableIterator.java`) extends `AutoCloseable, Iterator<T>`: `hasNext()`, `next()`, `close() throws Exception`; static `CloseableIterator.of(Iterator<T>)` wraps with no-op close.
- `APIStudyDetail`: `getId()/setId`, `getIsUserStudy()`, `getRootEntity()/setRootEntity` (→ `APIEntity`). `APIEntity`: `getId`, `getIdColumnName`, `getIsManyToOneWithParent`, `getVariables():List<APIVariable>`, `getCollections()`, `getChildren():List<APIEntity>`. `APIVariableWithValues`: `getType()`, `getDataShape()`, `getVocabulary():List<String>`, `getDistinctValuesCount():Long`, `getHasStudyDependentVocabulary()`. Ranges via `getDistributionDefaults()` (e.g. `APIIntegerDistributionDefaults.getRangeMin/getRangeMax/getDisplayRangeMin/getDisplayRangeMax`).

---

## Task ordering & phases

- **Phase A — Identity:** Task 1 (`StudyId` parser).
- **Phase B — Access control:** Task 2 (union-aware permission checks).
- **Phase C — Metadata model & Tier 1:** Task 3 (`EntityDef` member list) → Task 4 (`MetaStudyMerger` Tier 1) → Task 5 (`ReferenceMetadata` member-list constructor) → Task 6 (union-aware `getStudiesByStudyId` + `EdaSubsettingClient.getReferenceMetadata`).
- **Phase D — Caching & Tier 2:** Task 7 (union metadata cache) → Task 8 (Tier 2 deep check, cached).
- **Phase E — Data path:** Task 9 (PK comparator) → Task 10 (k-way merge iterator) → Task 11 (wire union into the tabular seam).
- **Phase F — Count/distribution/vocab:** Task 12 (count) → Task 13 (distribution + root-vocab).
- **Phase G — Integration:** Task 14 (end-to-end union integration test).

---

### Task 1: `StudyId` value type + `union(...)` parser

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/StudyId.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/StudyIdTest.java`

**Interfaces:**
- Consumes: nothing.
- Produces:
  - `StudyId.parse(String raw) : StudyId` — throws `jakarta.ws.rs.BadRequestException` on malformed/single-member unions.
  - `StudyId.isUnion() : boolean`
  - `StudyId.getMembers() : List<String>` — for a plain id, a singleton `[rawId]`; for a union, the ordered member ids.
  - `StudyId.getRawToken() : String` — the original scalar string (`"studyA"` or `"union(a,b)"`); equals what `getStudyId()` returns.
  - `StudyId.forSingle(String id) : StudyId` — convenience for known-plain ids (no parsing).

- [ ] **Step 1: Write the failing test**

```java
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
    assertThrows(BadRequestException.class, () -> StudyId.parse("union studyA,studyB)"));
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.StudyIdTest'`
Expected: FAIL — `StudyId` does not exist / compilation error.

- [ ] **Step 3: Write minimal implementation**

```java
package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Parses and represents a study identifier that may be either a plain study id
 * ("studyA") or a union meta-study token ("union(studyA,studyB,studyC)").
 *
 * Member ids are alphanumeric/underscore. The union(...) wrapper is explicit so a
 * bare id can never be mistaken for a union. Reserves room for a future
 * extension(...) type by keeping members as an ordered list.
 */
public final class StudyId {

  private static final String UNION_PREFIX = "union(";
  private static final String UNION_SUFFIX = ")";
  private static final Pattern MEMBER_ID = Pattern.compile("[A-Za-z0-9_]+");

  private final String _rawToken;
  private final boolean _isUnion;
  private final List<String> _members;

  private StudyId(String rawToken, boolean isUnion, List<String> members) {
    _rawToken = rawToken;
    _isUnion = isUnion;
    _members = List.copyOf(members);
  }

  /** Bypass parsing for an id already known to be a plain (non-union) study id. */
  public static StudyId forSingle(String id) {
    return new StudyId(id, false, List.of(id));
  }

  public static StudyId parse(String raw) {
    if (raw == null || raw.isBlank())
      throw new BadRequestException("studyId must not be blank");
    String token = raw.trim();

    if (!token.startsWith(UNION_PREFIX)) {
      // plain id path: identity pass-through, no member validation (backwards compatible)
      return new StudyId(token, false, List.of(token));
    }

    if (!token.endsWith(UNION_SUFFIX))
      throw new BadRequestException("Malformed union token (missing closing paren): " + raw);

    String inner = token.substring(UNION_PREFIX.length(), token.length() - UNION_SUFFIX.length());
    // any further '(' or ')' inside is trailing junk / nesting → reject
    if (inner.contains("(") || inner.contains(")"))
      throw new BadRequestException("Malformed union token (nested or unbalanced parens): " + raw);

    // union() or union(   ) — no members at all
    if (inner.isBlank())
      throw new BadRequestException("Malformed union token (empty member list): " + raw);

    List<String> members = new ArrayList<>();
    LinkedHashSet<String> seen = new LinkedHashSet<>();
    for (String part : inner.split(",", -1)) {
      String member = part.trim();
      if (member.isEmpty())
        throw new BadRequestException("Malformed union token (empty member): " + raw);
      if (!MEMBER_ID.matcher(member).matches())
        throw new BadRequestException("Malformed union token (illegal member id '" + member + "'): " + raw);
      if (!seen.add(member))
        throw new BadRequestException("Malformed union token (duplicate member '" + member + "'): " + raw);
      members.add(member);
    }
    if (members.size() < 2)
      throw new BadRequestException("A union must have at least two members (use the plain id instead): " + raw);

    return new StudyId(token, true, members);
  }

  public boolean isUnion() { return _isUnion; }

  public List<String> getMembers() { return _members; }

  public String getRawToken() { return _rawToken; }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StudyId other)) return false;
    return _rawToken.equals(other._rawToken);
  }

  @Override public int hashCode() { return Objects.hash(_rawToken); }

  @Override public String toString() { return _rawToken; }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.StudyIdTest'`
Expected: PASS (all 9 tests).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/StudyId.java \
        src/test/java/org/veupathdb/service/eda/common/model/StudyIdTest.java
git commit -m "feat(union): add StudyId value type and union(...) token parser"
```

---

### Task 2: Union-aware access control

Every permission check must resolve the token and require permission on **all** members. Centralize in `StudyAccess.confirmPermission` (covers merge, data/viz, subset). Handle `download/Service.java`'s separate path explicitly.

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/auth/StudyAccess.java` (add union-aware overload/branch to `confirmPermission`, ~l.56-61)
- Modify: `src/main/java/org/veupathdb/service/eda/download/Service.java` (`checkPermsAndFetchDatasetInfo`, ~l.94-104)
- Test: `src/test/java/org/veupathdb/service/eda/common/auth/StudyAccessUnionTest.java`

**Interfaces:**
- Consumes: `StudyId.parse` (Task 1).
- Produces: `StudyAccess.confirmPermission(long userId, String studyId, Predicate<StudyAccess> pred)` now accepts a union token and confirms permission on every member (throws `ForbiddenException` if any member fails). Behavior for plain ids is byte-for-byte unchanged.

- [ ] **Step 1: Read the current `confirmPermission` and `DatasetAccessClient.getStudyAccessByStudyId`**

Run: `sed -n '40,75p' src/main/java/org/veupathdb/service/eda/common/auth/StudyAccess.java`
Confirm the exact current body before editing. Expected: a static method delegating to `DatasetAccessClient.getStudyAccessByStudyId(studyId, userId).filter(pred).orElseThrow(ForbiddenException::new)`.

- [ ] **Step 2: Write the failing test**

```java
package org.veupathdb.service.eda.common.auth;

import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class StudyAccessUnionTest {

  // A tiny seam: confirmPermission delegates member lookups through a functional
  // resolver so the test can stub per-member StudyAccess without a live DB.
  @Test
  void unionRequiresEveryMemberAuthorized() {
    Predicate<StudyAccess> allowAll = sa -> true;

    // studyA authorized, studyB not
    StudyAccess.PerStudyAccessResolver resolver = (studyId, userId) ->
      studyId.equals("studyA")
        ? java.util.Optional.of(StudyAccess.grantAll())
        : java.util.Optional.of(StudyAccess.denyAll());

    assertThrows(ForbiddenException.class, () ->
      StudyAccess.confirmPermission(1L, "union(studyA,studyB)", allowAll, resolver));
  }

  @Test
  void unionPassesWhenAllMembersAuthorized() {
    Predicate<StudyAccess> allowAll = sa -> true;
    StudyAccess.PerStudyAccessResolver resolver =
      (studyId, userId) -> java.util.Optional.of(StudyAccess.grantAll());

    assertDoesNotThrow(() ->
      StudyAccess.confirmPermission(1L, "union(studyA,studyB)", allowAll, resolver));
  }

  @Test
  void plainIdUnchanged() {
    Predicate<StudyAccess> allowAll = sa -> true;
    StudyAccess.PerStudyAccessResolver resolver =
      (studyId, userId) -> studyId.equals("studyA")
        ? java.util.Optional.of(StudyAccess.grantAll())
        : java.util.Optional.empty();

    assertDoesNotThrow(() ->
      StudyAccess.confirmPermission(1L, "studyA", allowAll, resolver));
    assertThrows(ForbiddenException.class, () ->
      StudyAccess.confirmPermission(1L, "missing", allowAll, resolver));
  }
}
```

> NOTE: `StudyAccess.grantAll()` / `denyAll()` are test-friendly factories for the flat 5-boolean record. If constants already exist (e.g. an all-true `StudyAccess` used when access restrictions are disabled), reuse them and adjust the test; otherwise add package-visible static factories in Step 3.

- [ ] **Step 3: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.auth.StudyAccessUnionTest'`
Expected: FAIL — new overload / `PerStudyAccessResolver` / factories don't exist.

- [ ] **Step 4: Implement the union branch**

Add to `StudyAccess.java`:

```java
  /** Test/composition seam: resolves a single member studyId to its StudyAccess. */
  @FunctionalInterface
  public interface PerStudyAccessResolver {
    java.util.Optional<StudyAccess> resolve(String studyId, long userId);
  }

  // The production resolver: existing single-study lookup.
  private static final PerStudyAccessResolver DEFAULT_RESOLVER =
    (studyId, userId) -> DatasetAccessClient.getStudyAccessByStudyId(studyId, userId);

  public static void confirmPermission(long userId, String studyId, Predicate<StudyAccess> accessPredicate) {
    confirmPermission(userId, studyId, accessPredicate, DEFAULT_RESOLVER);
  }

  // Package-visible for testing.
  static void confirmPermission(long userId, String studyId,
                                Predicate<StudyAccess> accessPredicate,
                                PerStudyAccessResolver resolver) {
    org.veupathdb.service.eda.common.model.StudyId parsed =
      org.veupathdb.service.eda.common.model.StudyId.parse(studyId);
    // A union is authorized iff EVERY member is authorized. Never authorize the literal token.
    for (String member : parsed.getMembers()) {
      resolver.resolve(member, userId)
        .filter(accessPredicate)
        .orElseThrow(ForbiddenException::new);
    }
  }
```

Add test factories if not already present (match the actual `StudyAccess` shape — flat booleans):

```java
  // Visible for testing / composition. Adjust field list to the real record components.
  static StudyAccess grantAll() { /* return an all-true StudyAccess */ }
  static StudyAccess denyAll()  { /* return an all-false StudyAccess */ }
```

> IMPLEMENTATION NOTE: If the existing single-arg `confirmPermission` body differs from the delegation shown, preserve its exact predicate/exception semantics inside the per-member loop. The only behavioral change for a **plain** id must be: it still resolves exactly one member (the id itself).

- [ ] **Step 5: Make `download/Service.checkPermsAndFetchDatasetInfo` union-aware**

Read `sed -n '90,110p' src/main/java/org/veupathdb/service/eda/download/Service.java`. This path fetches a single dataset info and applies `accessGranter.apply(study.getStudyAccess())`. Wrap it:

```java
  // pseudo-diff intent: before fetching a single dataset info, if the token is a union,
  // resolve each member's dataset info and require the granter on all; deny (404/403 per
  // existing convention) if any member is missing/unauthorized. For a plain id, keep the
  // current single-study body verbatim.
  var parsed = org.veupathdb.service.eda.common.model.StudyId.parse(studyId);
  if (parsed.isUnion()) {
    parsed.getMembers().forEach(member -> checkPermsAndFetchDatasetInfoSingle(member, accessGranter));
    // download of union file bundles is out of v1 data-file scope: return/act per the
    // deferred convention (e.g. the merged-metadata path handles union; raw file download
    // of a union token should 400). See NOTE below.
  }
```

> SCOPE NOTE: Raw `GET /download/{project}/{study-id}` of file bundles for a union is **not** a v1 data deliverable (union is a runtime merge, not a persisted bundle). The required change here is only that the **access check** never authorizes the literal union token as one study. Simplest correct v1: `checkPermsAndFetchDatasetInfo` parses the token, and for a union either (a) enforces per-member perms then throws `BadRequestException("union download not supported")`, or (b) enforces per-member perms and streams nothing. Choose (a). Keep the plain-id path unchanged.

- [ ] **Step 6: Run tests to verify they pass**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.auth.StudyAccessUnionTest'`
Expected: PASS. Then `./gradlew compileJava` → BUILD SUCCESSFUL.

- [ ] **Step 7: Audit remaining permission sites**

Run: `grep -rn "confirmPermission\|getStudyAccessByStudyId" src/main`
For each hit, confirm it now flows through the union-aware `confirmPermission`, OR is a deferred/compute path. Record findings in the commit message. Compute sites (`ComputeController.java:346`) are **deferred** (spec §11) — leave them single-study; they will reject a union token by failing to resolve it, which is acceptable for v1.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/auth/StudyAccess.java \
        src/main/java/org/veupathdb/service/eda/download/Service.java \
        src/test/java/org/veupathdb/service/eda/common/auth/StudyAccessUnionTest.java
git commit -m "feat(union): require permission on every member study; deny literal union token"
```

---

### Task 3: `EntityDef` gains a member-study list

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/model/EntityDef.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/EntityDefMemberStudiesTest.java`

**Interfaces:**
- Consumes: nothing.
- Produces:
  - `EntityDef` retains its existing constructor `EntityDef(String id, String displayName, String idColumnName, Boolean isManyToOneWithParent)` (defaults member list to empty, meaning "single-study / not a union").
  - New: `EntityDef.setMemberStudyIds(List<String>)` and `EntityDef.getMemberStudyIds() : List<String>` (empty list = single-study).

Rationale for a **list** (not a set/flag): the deferred `extension(...)` case needs a leaf entity contributed by one member — a list on `EntityDef` already models that with no further change.

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDefMemberStudiesTest {

  @Test
  void defaultsToEmptyMemberList() {
    EntityDef e = new EntityDef("EUPA_1", "Participant", "participant_id", true);
    assertTrue(e.getMemberStudyIds().isEmpty());
  }

  @Test
  void carriesMemberStudyIds() {
    EntityDef e = new EntityDef("EUPA_1", "Participant", "participant_id", true);
    e.setMemberStudyIds(List.of("studyA", "studyB"));
    assertEquals(List.of("studyA", "studyB"), e.getMemberStudyIds());
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.EntityDefMemberStudiesTest'`
Expected: FAIL — `getMemberStudyIds`/`setMemberStudyIds` undefined.

- [ ] **Step 3: Implement the field + accessors**

Add to `EntityDef.java` (near the other fields, e.g. after `_collections`):

```java
  // Member studies that contribute rows to this entity. Empty = ordinary single study.
  // A list (not a set) so the deferred extension(...) case (leaf from one member) fits.
  private List<String> _memberStudyIds = java.util.List.of();

  public void setMemberStudyIds(List<String> memberStudyIds) {
    _memberStudyIds = java.util.List.copyOf(memberStudyIds);
  }

  public List<String> getMemberStudyIds() {
    return _memberStudyIds;
  }
```

Ensure `import java.util.List;` is present (it is used by existing methods; verify).

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.EntityDefMemberStudiesTest'`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/EntityDef.java \
        src/test/java/org/veupathdb/service/eda/common/model/EntityDefMemberStudiesTest.java
git commit -m "feat(union): add member-study list to EntityDef"
```

---

### Shared Test Fixture: `TestStudies` (create as the first step of Task 4)

`TestStudies` is a **net-new** JUnit test helper used by Tasks 4, 5, 7, 8, 11, 12, 13, and 14. It is not a production class and has no test of its own. Create it in full when you write Task 4's first test (it must compile before any test that references it). Later tasks only *use* its methods — they do not extend it.

**File:** `src/test/java/org/veupathdb/service/eda/common/model/TestStudies.java`

**Why the field values below are load-bearing (verified against the generated `*Impl` classes):**
- `APIEntityImpl.variables`/`collections`/`children` default to **`null`**, and `ReferenceMetadata.buildEntityTree` streams all three — so every entity MUST set them to (possibly empty) lists or construction NPEs.
- `buildEntityTree` auto-unboxes `getIsMultiValued()`, `getImputeZero()`, `getHasStudyDependentVocabulary()` to `boolean` — these MUST be non-null.
- `DataRanges.getDataRanges` returns empty for any non-`CONTINUOUS` dataShape, but for a `CONTINUOUS` integer/date var it dereferences `distributionDefaults.getRangeMin()/getRangeMax()` — so a CONTINUOUS var MUST carry a `distributionDefaults` with non-null min/max. String vars use `CATEGORICAL` (safe).
- `APIStringVariableImpl`/`APIIntegerVariableImpl` have a **`final` `type`** field (no `setType`); `new APIStringVariableImpl()` is already `STRING`, `new APIIntegerVariableImpl()` already `INTEGER`.
- An entity's PK **row-map key** equals its `idColumnName`, which equals `entityDef.getIdColumnDef().getVariableId()` — so tests key PK columns by the `idColumnName` the fixture sets.

```java
package org.veupathdb.service.eda.common.model;

import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.UUID;

/** Builds minimal generated-model study graphs that survive ReferenceMetadata.buildEntityTree. */
public final class TestStudies {

  private TestStudies() {}

  public static final String ROOT_ID = "EUPA_ROOT";
  public static final String CHILD_ID = "EUPA_CHILD";
  public static final String HOUSEHOLD_ID = "EUPA_HH";
  public static final String PARTICIPANT_ID = "EUPA_PART";

  // ---- variable builders ----

  /** STRING categorical variable (getDataRanges returns empty → safe). */
  public static APIStringVariable stringVar(String varId, List<String> vocab) {
    APIStringVariableImpl v = new APIStringVariableImpl(); // type is final STRING
    v.setId(varId);
    v.setDisplayName(varId);
    v.setDataShape(APIVariableDataShape.CATEGORICAL);
    v.setVocabulary(vocab);
    v.setDistinctValuesCount(vocab == null ? null : (long) vocab.size());
    v.setIsMultiValued(false);               // unboxed → must be non-null
    v.setImputeZero(false);                  // unboxed → must be non-null
    v.setHasStudyDependentVocabulary(false); // unboxed → must be non-null
    return v;
  }

  /** INTEGER CONTINUOUS variable with a populated range (safe through buildEntityTree). */
  public static APIIntegerVariable intVar(String varId, long rangeMin, long rangeMax) {
    APIIntegerVariableImpl v = new APIIntegerVariableImpl(); // type is final INTEGER
    v.setId(varId);
    v.setDisplayName(varId);
    v.setUnits("years");
    v.setDataShape(APIVariableDataShape.CONTINUOUS);
    v.setIsMultiValued(false);
    v.setImputeZero(false);
    v.setHasStudyDependentVocabulary(false);
    APIIntegerDistributionDefaultsImpl dd = new APIIntegerDistributionDefaultsImpl();
    dd.setRangeMin(rangeMin);
    dd.setRangeMax(rangeMax);
    dd.setDisplayRangeMin(rangeMin);
    dd.setDisplayRangeMax(rangeMax);
    v.setDistributionDefaults(dd);
    return v;
  }

  // ---- entity / study builders ----

  public static APIEntity entity(String id, String idColumnName, boolean manyToOne,
                                 List<APIVariable> vars, List<APIEntity> children) {
    APIEntityImpl e = new APIEntityImpl();
    e.setId(id);
    e.setIdColumnName(idColumnName);
    e.setDisplayName(id);
    e.setDisplayNamePlural(id + "s");
    e.setDescription(id);
    e.setIsManyToOneWithParent(manyToOne);
    e.setVariables(vars);           // must be non-null
    e.setCollections(List.of());    // must be non-null
    e.setChildren(children);        // must be non-null
    return e;
  }

  public static APIStudyDetail study(String id, APIEntity root) {
    APIStudyDetailImpl s = new APIStudyDetailImpl();
    s.setId(id);
    s.setIsUserStudy(false);
    s.setRootEntity(root);
    return s;
  }

  // ---- named single-study fixtures ----

  public static APIStudyDetail oneEntityOneStringVar(String studyId, String entityId, String varId, List<String> vocab) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(stringVar(varId, vocab)), List.of()));
  }

  public static APIStudyDetail oneEntityOneIntVar(String studyId, String entityId, String varId) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(intVar(varId, 0L, 100L)), List.of()));
  }

  public static APIStudyDetail oneEntityIntVarWithRange(String studyId, String entityId, String varId, long min, long max) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(intVar(varId, min, max)), List.of()));
  }

  /** Root + one child; distinct entity ids so it is structurally different from the single-entity fixtures. */
  public static APIStudyDetail twoEntities(String studyId) {
    APIEntity child = entity(CHILD_ID, "child_id", true,
        List.<APIVariable>of(stringVar("cvar", List.of("x", "y"))), List.of());
    APIEntity root = entity(ROOT_ID, "root_id", false,
        List.<APIVariable>of(stringVar("rvar", List.of("a", "b"))), List.of(child));
    return study(studyId, root);
  }

  // ---- union ReferenceMetadata fixtures ----

  public static ReferenceMetadata unionMetadata(String token, List<String> members) {
    return new ReferenceMetadata(twoEntities(token), members);
  }

  /** household (root) -> participant (child); household_id / participant_id PK columns. */
  public static ReferenceMetadata householdParticipantUnion(String token, List<String> members) {
    APIEntity participant = entity(PARTICIPANT_ID, "participant_id", true,
        List.<APIVariable>of(stringVar("pvar", List.of("x"))), List.of());
    APIEntity household = entity(HOUSEHOLD_ID, "household_id", false,
        List.<APIVariable>of(stringVar("region", List.of("north", "south"))), List.of(participant));
    return new ReferenceMetadata(study(token, household), members);
  }

  // ---- read-back helpers ----

  public static List<String> firstVarVocabulary(APIStudyDetail study) {
    return ((APIVariableWithValues) study.getRootEntity().getVariables().get(0)).getVocabulary();
  }

  public static long[] firstVarIntRange(APIStudyDetail study) {
    APIIntegerDistributionDefaults dd =
        ((APIIntegerVariable) study.getRootEntity().getVariables().get(0)).getDistributionDefaults();
    return new long[]{ dd.getRangeMin(), dd.getRangeMax() };
  }

  // ---- misc ----

  public static VariableSpec varSpec(String entityId, String varId) {
    return VariableDef.newVariableSpec(entityId, varId); // verified factory on VariableDef
  }

  public static StreamSpec streamSpec(String entityId) {
    return new StreamSpec("test-" + UUID.randomUUID(), entityId);
  }
}
```

> NOTE ON ROW KEYS (resolves the MUST-VERIFY referenced in Tasks 11 & 13): the subset decode keys **id/ancestor PK columns by the entity's PK column name** (`= idColumnName = idColumnDef.getVariableId()`) and **variable value columns by the bare `variableId`** (default `TallRowConversionUtils.getTallToWideFunction` uses `Variable::getId`, not dot notation). Fixture rows and the `UnionTabularStreams` helper both use these exact keys, so unit tests are self-consistent. The remaining live-DB check is only that `EdaSubsettingClient.getTabularDataIterator` passes the **default** key function (bare id) rather than a dot-notation override — confirm before the integration run.

---

### Task 4: `MetaStudyMerger` — Tier 1 structural merge + vocab-union + range-merge

Produce a single merged `APIStudyDetail` from N member details. `id` = union token. Structural incompatibility → `BadRequestException` naming what differs. Vocabularies become the deduped union; continuous/date ranges become `min(mins)`/`max(maxes)`. No row scan.

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/MetaStudyMerger.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/MetaStudyMergerTest.java`

**Interfaces:**
- Consumes: `StudyId` (Task 1); generated `APIStudyDetail`/`APIEntity`/`APIVariable*` types.
- Produces:
  - `MetaStudyMerger.merge(StudyId unionId, List<APIStudyDetail> members) : APIStudyDetail` — throws `BadRequestException` on structural incompatibility. The returned detail's tree is member[0]'s shape with merged vocabularies/ranges; `id` = `unionId.getRawToken()`.
  - `MetaStudyMerger.structuralFingerprint(APIEntity) : String` — deterministic string of `(entityId, idColumnName, {varId:type:dataShape:units}...)` recursed over children (used for compatibility comparison and testable in isolation).

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetaStudyMergerTest {

  // Helpers build minimal APIStudyDetail graphs. Prefer the generated *Impl classes
  // (e.g. APIStudyDetailImpl, APIEntityImpl, APIStringVariableImpl). See TestStudies below.

  @Test
  void rejectsDifferentEntityTreeShape() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    APIStudyDetail b = TestStudies.twoEntities("studyB");
    StudyId union = StudyId.parse("union(studyA,studyB)");
    BadRequestException ex = assertThrows(BadRequestException.class,
      () -> MetaStudyMerger.merge(union, List.of(a, b)));
    assertTrue(ex.getMessage().toLowerCase().contains("entity") || ex.getMessage().toLowerCase().contains("structur"));
  }

  @Test
  void rejectsVariableTypeMismatch() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "age", List.of("1"));
    APIStudyDetail b = TestStudies.oneEntityOneIntVar("studyB", "EUPA_1", "age");
    StudyId union = StudyId.parse("union(studyA,studyB)");
    assertThrows(BadRequestException.class, () -> MetaStudyMerger.merge(union, List.of(a, b)));
  }

  @Test
  void mergesVocabularyAsDedupedUnion() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    APIStudyDetail b = TestStudies.oneEntityOneStringVar("studyB", "EUPA_1", "sex", List.of("M", "U"));
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    assertEquals("union(studyA,studyB)", merged.getId());
    List<String> vocab = TestStudies.firstVarVocabulary(merged);
    assertEquals(List.of("F", "M", "U"), vocab); // deduped, order-normalized (sorted)
  }

  @Test
  void mergesIntegerRangeAsMinOfMinsMaxOfMaxes() {
    APIStudyDetail a = TestStudies.oneEntityIntVarWithRange("studyA", "EUPA_1", "age", 5L, 40L);
    APIStudyDetail b = TestStudies.oneEntityIntVarWithRange("studyB", "EUPA_1", "age", 1L, 60L);
    StudyId union = StudyId.parse("union(studyA,studyB)");

    APIStudyDetail merged = MetaStudyMerger.merge(union, List.of(a, b));

    long[] range = TestStudies.firstVarIntRange(merged); // {rangeMin, rangeMax}
    assertEquals(1L, range[0]);
    assertEquals(60L, range[1]);
  }
}
```

> Create the full `TestStudies` fixture (see the **Shared Test Fixture** section immediately above) as the first action of this step — it must compile before this test does. This test uses `TestStudies.oneEntityOneStringVar`, `twoEntities`, `oneEntityOneIntVar`, `oneEntityIntVarWithRange`, `firstVarVocabulary`, and `firstVarIntRange`, all defined there.

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyMergerTest'`
Expected: FAIL — `MetaStudyMerger` does not exist.

- [ ] **Step 3: Implement `MetaStudyMerger`**

```java
package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.generated.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tier 1 metadata merge for union meta-studies. Combines per-study summaries
 * (vocabularies, ranges) already present in each study's metadata; does not read
 * row data. Produces one merged APIStudyDetail whose id is the union token.
 */
public final class MetaStudyMerger {

  private MetaStudyMerger() {}

  public static APIStudyDetail merge(StudyId unionId, List<APIStudyDetail> members) {
    if (members.size() < 2)
      throw new BadRequestException("A union needs at least two members: " + unionId);

    // 1. structural compatibility: every member's tree fingerprint must match member[0]
    String reference = structuralFingerprint(members.get(0).getRootEntity());
    for (int i = 1; i < members.size(); i++) {
      String fp = structuralFingerprint(members.get(i).getRootEntity());
      if (!fp.equals(reference)) {
        throw new BadRequestException(
          "Union members are structurally incompatible. '" + members.get(0).getId()
          + "' vs '" + members.get(i).getId() + "'. Fingerprints differ:\n  "
          + reference + "\n  " + fp);
      }
    }

    // 2. build merged detail from member[0]'s shape, merging summaries across members
    APIStudyDetail merged = new APIStudyDetailImpl();
    merged.setId(unionId.getRawToken());
    merged.setIsUserStudy(members.get(0).getIsUserStudy());
    merged.setRootEntity(mergeEntity(members.stream().map(APIStudyDetail::getRootEntity).toList()));
    return merged;
  }

  /**
   * Deterministic structural signature: entity id, id column, and each variable's
   * id:type:dataShape:units, recursed over children in declared order.
   */
  public static String structuralFingerprint(APIEntity entity) {
    StringBuilder sb = new StringBuilder();
    appendFingerprint(entity, sb);
    return sb.toString();
  }

  private static void appendFingerprint(APIEntity e, StringBuilder sb) {
    sb.append('[').append(e.getId()).append('|').append(e.getIdColumnName()).append('{');
    e.getVariables().stream()
      .sorted(Comparator.comparing(APIVariable::getId))
      .forEach(v -> sb.append(v.getId()).append(':').append(v.getType())
        .append(':').append(dataShapeOf(v)).append(':').append(unitsOf(v)).append(';'));
    sb.append('}');
    e.getChildren().stream()
      .sorted(Comparator.comparing(APIEntity::getId))
      .forEach(c -> appendFingerprint(c, sb));
    sb.append(']');
  }

  private static String dataShapeOf(APIVariable v) {
    return (v instanceof APIVariableWithValues wv && wv.getDataShape() != null)
      ? wv.getDataShape().toString() : "-";
  }

  private static String unitsOf(APIVariable v) {
    // units live on integer/number/date variable subtypes; treat absent as "-"
    if (v instanceof APIIntegerVariable iv && iv.getUnits() != null) return iv.getUnits();
    if (v instanceof APIDateVariable) return "-"; // dates have no units
    return "-";
  }

  private static APIEntity mergeEntity(List<APIEntity> entityAcrossMembers) {
    APIEntity ref = entityAcrossMembers.get(0);
    APIEntityImpl out = new APIEntityImpl();
    out.setId(ref.getId());
    out.setIdColumnName(ref.getIdColumnName());
    out.setDisplayName(ref.getDisplayName());
    out.setDisplayNamePlural(ref.getDisplayNamePlural());
    out.setDescription(ref.getDescription());
    out.setIsManyToOneWithParent(ref.getIsManyToOneWithParent());
    out.setCollections(ref.getCollections());

    // merge variables by id (structural compat already guarantees identical id sets/types)
    List<APIVariable> mergedVars = new ArrayList<>();
    for (APIVariable refVar : ref.getVariables()) {
      List<APIVariable> sameVarAcrossMembers = entityAcrossMembers.stream()
        .map(e -> e.getVariables().stream().filter(v -> v.getId().equals(refVar.getId())).findFirst().orElseThrow())
        .toList();
      mergedVars.add(mergeVariable(sameVarAcrossMembers));
    }
    out.setVariables(mergedVars);

    // recurse children (matched by id)
    List<APIEntity> mergedChildren = new ArrayList<>();
    for (APIEntity refChild : ref.getChildren()) {
      List<APIEntity> sameChildAcrossMembers = entityAcrossMembers.stream()
        .map(e -> e.getChildren().stream().filter(c -> c.getId().equals(refChild.getId())).findFirst().orElseThrow())
        .toList();
      mergedChildren.add(mergeEntity(sameChildAcrossMembers));
    }
    out.setChildren(mergedChildren);
    return out;
  }

  private static APIVariable mergeVariable(List<APIVariable> across) {
    APIVariable ref = across.get(0);
    if (!(ref instanceof APIVariableWithValues)) {
      return ref; // categories / non-value vars carry no vocab/range to merge
    }
    // Vocabulary union (deduped, sorted for a normalized order)
    if (ref instanceof APIStringVariable) {
      APIStringVariableImpl out = copyStringVar((APIStringVariable) ref);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APIIntegerVariable) {
      APIIntegerVariableImpl out = copyIntVar((APIIntegerVariable) ref);
      mergeIntRange(out, across);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APIDateVariable) {
      APIDateVariableImpl out = copyDateVar((APIDateVariable) ref);
      mergeDateRange(out, across);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    // number variables and any other WithValues subtype: merge range analogously
    return mergeNumberVariable(across);
  }

  private static List<String> unionVocab(List<APIVariable> across) {
    TreeSet<String> union = new TreeSet<>(); // dedup + normalized (sorted) order
    for (APIVariable v : across) {
      List<String> vocab = ((APIVariableWithValues) v).getVocabulary();
      if (vocab != null) union.addAll(vocab);
    }
    return union.isEmpty() ? null : new ArrayList<>(union);
  }

  // Range merges: min(mins) / max(maxes) over each member's distributionDefaults.
  private static void mergeIntRange(APIIntegerVariableImpl out, List<APIVariable> across) {
    Long min = null, max = null, dMin = null, dMax = null;
    for (APIVariable v : across) {
      APIIntegerDistributionDefaults dd = ((APIIntegerVariable) v).getDistributionDefaults();
      if (dd == null) continue;
      min = minL(min, dd.getRangeMin());     max = maxL(max, dd.getRangeMax());
      dMin = minL(dMin, dd.getDisplayRangeMin()); dMax = maxL(dMax, dd.getDisplayRangeMax());
    }
    APIIntegerDistributionDefaultsImpl dd = new APIIntegerDistributionDefaultsImpl();
    dd.setRangeMin(min); dd.setRangeMax(max);
    dd.setDisplayRangeMin(dMin != null ? dMin : min);
    dd.setDisplayRangeMax(dMax != null ? dMax : max);
    out.setDistributionDefaults(dd);
  }

  private static void mergeDateRange(APIDateVariableImpl out, List<APIVariable> across) {
    String min = null, max = null;
    for (APIVariable v : across) {
      APIDateDistributionDefaults dd = ((APIDateVariable) v).getDistributionDefaults();
      if (dd == null) continue;
      min = minStr(min, dd.getRangeMin()); max = maxStr(max, dd.getRangeMax());
    }
    APIDateDistributionDefaultsImpl dd = new APIDateDistributionDefaultsImpl();
    dd.setRangeMin(min); dd.setRangeMax(max);
    dd.setDisplayRangeMin(min); dd.setDisplayRangeMax(max);
    out.setDistributionDefaults(dd);
  }

  private static APIVariable mergeNumberVariable(List<APIVariable> across) {
    // Mirror mergeIntRange for APINumberVariable/APINumberDistributionDefaults (Double bounds).
    // Copy ref, union vocab, min/max the numeric bounds. See mergeIntRange for the pattern.
    throw new UnsupportedOperationException("implement APINumberVariable range merge mirroring mergeIntRange");
  }

  // --- small helpers (null-safe min/max; shallow copies preserving all ref fields) ---
  private static Long minL(Long a, Long b) { return a == null ? b : (b == null ? a : Math.min(a, b)); }
  private static Long maxL(Long a, Long b) { return a == null ? b : (b == null ? a : Math.max(a, b)); }
  private static String minStr(String a, String b) { return a == null ? b : (b == null ? a : (a.compareTo(b) <= 0 ? a : b)); }
  private static String maxStr(String a, String b) { return a == null ? b : (b == null ? a : (a.compareTo(b) >= 0 ? a : b)); }

  // copy*Var: build a *Impl, copy every getter of the ref var onto it (id, parentId,
  // displayName, type, dataShape, isMultiValued, imputeZero, distinctValuesCount, units,
  // hasStudyDependentVocabulary, etc.). Keep these exhaustive so no metadata is dropped.
  private static APIStringVariableImpl copyStringVar(APIStringVariable v) { /* copy all fields */ return new APIStringVariableImpl(); }
  private static APIIntegerVariableImpl copyIntVar(APIIntegerVariable v) { /* copy all fields */ return new APIIntegerVariableImpl(); }
  private static APIDateVariableImpl copyDateVar(APIDateVariable v) { /* copy all fields */ return new APIDateVariableImpl(); }
}
```

> IMPLEMENTATION NOTE: The `copy*Var` helpers must copy **every** field of the source variable so display metadata survives the merge — enumerate the getters/setters from the concrete `APIStringVariable`/`APIIntegerVariable`/`APIDateVariable` interfaces (verified accessors in the reference-facts section) and set each. Do not leave them as stubs. Implement `mergeNumberVariable` mirroring `mergeIntRange` against `APINumberVariable`/`APINumberDistributionDefaults`. Confirm the exact `APIDateDistributionDefaults` bound type (String vs Long) from the generated model before finalizing `mergeDateRange`.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyMergerTest'`
Expected: PASS (4 tests + helper compiles).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/MetaStudyMerger.java \
        src/test/java/org/veupathdb/service/eda/common/model/MetaStudyMergerTest.java \
        src/test/java/org/veupathdb/service/eda/common/model/TestStudies.java
git commit -m "feat(union): Tier 1 metadata merge (structural compat, vocab union, range merge)"
```

---

### Task 5: `ReferenceMetadata` member-list constructor

Add a constructor that builds the tree from a merged `APIStudyDetail` (reusing the existing tree builder) and tags every `EntityDef` with the member-study list.

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/ReferenceMetadataUnionTest.java`

**Interfaces:**
- Consumes: merged `APIStudyDetail` (Task 4); `EntityDef.setMemberStudyIds` (Task 3).
- Produces:
  - New constructor `ReferenceMetadata(APIStudyDetail mergedStudy, List<String> memberStudyIds)`. `getStudyId()` returns the merged study's id (the union token). Every `EntityDef` in the tree has `getMemberStudyIds() == memberStudyIds`.
  - New accessor `ReferenceMetadata.getMemberStudyIds() : List<String>` (empty for single-study instances built by the existing constructor).

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReferenceMetadataUnionTest {

  @Test
  void unionConstructorTagsEveryEntityWithMembers() {
    // two-entity study so we assert tagging recurses
    var study = TestStudies.twoEntities("union(studyA,studyB)");
    var members = List.of("studyA", "studyB");

    ReferenceMetadata md = new ReferenceMetadata(study, members);

    assertEquals("union(studyA,studyB)", md.getStudyId());
    assertEquals(members, md.getMemberStudyIds());
    // root + child both tagged
    var root = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    var child = md.getEntity(TestStudies.CHILD_ID).orElseThrow();
    assertEquals(members, root.getMemberStudyIds());
    assertEquals(members, child.getMemberStudyIds());
  }

  @Test
  void singleStudyConstructorHasEmptyMembers() {
    var study = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    ReferenceMetadata md = new ReferenceMetadata(study);
    assertTrue(md.getMemberStudyIds().isEmpty());
    assertTrue(md.getEntity("EUPA_1").orElseThrow().getMemberStudyIds().isEmpty());
  }
}
```

> Uses `TestStudies.twoEntities`, `ROOT_ID`, `CHILD_ID` (defined in the Shared Test Fixture section).

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.ReferenceMetadataUnionTest'`
Expected: FAIL — new constructor / `getMemberStudyIds` undefined.

- [ ] **Step 3: Implement**

In `ReferenceMetadata.java`, add a field and the constructor. Keep `_studyId`/`_entityTree` final; set member ids via a new final field:

```java
  private final List<String> _memberStudyIds;
```

Update the existing constructor to initialize `_memberStudyIds = List.of();`:

```java
  public ReferenceMetadata(APIStudyDetail study) {
    _studyId = study.getId();
    _memberStudyIds = java.util.List.of();
    _entityTree = buildEntityTree(study.getRootEntity(), new ArrayList<>());
  }
```

Add the union constructor:

```java
  public ReferenceMetadata(APIStudyDetail mergedStudy, List<String> memberStudyIds) {
    _studyId = mergedStudy.getId();
    _memberStudyIds = java.util.List.copyOf(memberStudyIds);
    _entityTree = buildEntityTree(mergedStudy.getRootEntity(), new ArrayList<>());
    // tag every EntityDef with the member list
    _entityTree.flatten().forEach(node -> node.getContents().setMemberStudyIds(_memberStudyIds));
  }

  public List<String> getMemberStudyIds() {
    return _memberStudyIds;
  }
```

> IMPLEMENTATION NOTE: Use the `TreeNode<EntityDef>` traversal that the class already uses elsewhere to visit every node (the reference agent noted helper methods like `getDescendants`; if `TreeNode` exposes `flatten()`/`findAll()` use it, otherwise walk via a small recursive helper `void tagAll(TreeNode<EntityDef> node)` that calls `node.getContents().setMemberStudyIds(...)` then recurses `node.getChildNodes()`). Confirm the exact `TreeNode` traversal API before finalizing.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.ReferenceMetadataUnionTest'`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java \
        src/test/java/org/veupathdb/service/eda/common/model/ReferenceMetadataUnionTest.java \
        src/test/java/org/veupathdb/service/eda/common/model/TestStudies.java
git commit -m "feat(union): ReferenceMetadata member-list constructor tags every EntityDef"
```

---

### Task 6: Union-aware study resolution (endpoint + `EdaSubsettingClient.getReferenceMetadata`)

Route union tokens: `GET /studies/{study-id}` returns the merged `APIStudyDetail`; the merge layer builds a union `ReferenceMetadata`. This task wires Tier-1 only (Tier-2 + caching added in Tasks 7–8; here we build every request — a temporary un-cached path that Task 7 wraps with the cache).

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java`
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java` (add `getReferenceMetadata`)
- Modify: `src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java` (`getStudiesByStudyId`, ~l.90-98)
- Modify: `src/main/kotlin/org/veupathdb/service/eda/merge/core/request/RequestResources.kt` (l.30)
- Test: `src/test/java/org/veupathdb/service/eda/common/model/MetaStudyResolverTest.java`

**Interfaces:**
- Consumes: `StudyId` (T1), `MetaStudyMerger.merge` (T4), `ReferenceMetadata(APIStudyDetail, List<String>)` (T5), `EdaSubsettingClient.getStudy` (existing).
- Produces:
  - `MetaStudyResolver.resolveStudyDetail(StudyId id, Function<String,APIStudyDetail> memberFetcher) : APIStudyDetail` — plain id → `memberFetcher.apply(id)`; union → `MetaStudyMerger.merge(id, members.map(memberFetcher))`.
  - `MetaStudyResolver.resolveReferenceMetadata(StudyId id, Function<String,APIStudyDetail> memberFetcher) : ReferenceMetadata` — plain → `new ReferenceMetadata(detail)`; union → `new ReferenceMetadata(mergedDetail, id.getMembers())`.
  - `EdaSubsettingClient.getReferenceMetadata(String studyId) : ReferenceMetadata` — production entry using `EdaSubsettingClient::getStudy` as the fetcher.

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.model;

import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MetaStudyResolverTest {

  @Test
  void plainIdPassesThrough() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F", "M"));
    Function<String, APIStudyDetail> fetch = Map.of("studyA", a)::get;

    ReferenceMetadata md = MetaStudyResolver.resolveReferenceMetadata(StudyId.parse("studyA"), fetch);

    assertEquals("studyA", md.getStudyId());
    assertTrue(md.getMemberStudyIds().isEmpty());
  }

  @Test
  void unionBuildsMergedMetadata() {
    APIStudyDetail a = TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F"));
    APIStudyDetail b = TestStudies.oneEntityOneStringVar("studyB", "EUPA_1", "sex", List.of("M"));
    Function<String, APIStudyDetail> fetch = Map.of("studyA", a, "studyB", b)::get;

    ReferenceMetadata md = MetaStudyResolver.resolveReferenceMetadata(StudyId.parse("union(studyA,studyB)"), fetch);

    assertEquals("union(studyA,studyB)", md.getStudyId());
    assertEquals(List.of("studyA", "studyB"), md.getMemberStudyIds());
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyResolverTest'`
Expected: FAIL — `MetaStudyResolver` undefined.

- [ ] **Step 3: Implement `MetaStudyResolver`**

```java
package org.veupathdb.service.eda.common.model;

import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.List;
import java.util.function.Function;

public final class MetaStudyResolver {

  private MetaStudyResolver() {}

  public static APIStudyDetail resolveStudyDetail(StudyId id, Function<String, APIStudyDetail> memberFetcher) {
    if (!id.isUnion()) {
      return memberFetcher.apply(id.getMembers().get(0));
    }
    List<APIStudyDetail> members = id.getMembers().stream().map(memberFetcher).toList();
    return MetaStudyMerger.merge(id, members);
  }

  public static ReferenceMetadata resolveReferenceMetadata(StudyId id, Function<String, APIStudyDetail> memberFetcher) {
    if (!id.isUnion()) {
      return new ReferenceMetadata(memberFetcher.apply(id.getMembers().get(0)));
    }
    APIStudyDetail merged = resolveStudyDetail(id, memberFetcher);
    return new ReferenceMetadata(merged, id.getMembers());
  }
}
```

- [ ] **Step 4: Add `EdaSubsettingClient.getReferenceMetadata`**

```java
  public static ReferenceMetadata getReferenceMetadata(String studyId) {
    return MetaStudyResolver.resolveReferenceMetadata(
      StudyId.parse(studyId), EdaSubsettingClient::getStudy);
  }
```

(Imports: `org.veupathdb.service.eda.common.model.MetaStudyResolver`, `StudyId`, `ReferenceMetadata`.)

- [ ] **Step 5: Route the endpoint and the merge layer**

`StudiesService.getStudiesByStudyId` (l.90-98) — replace the single-study resolution with union-aware resolution, keeping the plain path identical:

```java
  @Override
  public GetStudiesByStudyIdResponse getStudiesByStudyId(String studyId) {
    checkPerms(_request, studyId, StudyAccess::allowStudyMetadata); // now union-aware (Task 2)
    StudyId parsed = StudyId.parse(studyId);
    APIStudyDetail apiStudyDetail = parsed.isUnion()
      ? MetaStudyResolver.resolveStudyDetail(parsed, id -> EdaSubsettingClient.getStudy(id))
      : ApiConversionUtil.getApiStudyDetail(Resources.getStudyResolver().getStudyById(studyId));
    StudyIdGetResponse response = new StudyIdGetResponseImpl();
    response.setStudy(apiStudyDetail);
    return GetStudiesByStudyIdResponse.respond200WithApplicationJson(response);
  }
```

`RequestResources.kt:30` — build metadata via the union-aware entry:

```kotlin
  val metadata = EdaSubsettingClient.getReferenceMetadata(studyId)
```

- [ ] **Step 6: Run tests + compile**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyResolverTest'` → PASS
Run: `./gradlew compileJava compileKotlin` → BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java \
        src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java \
        src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java \
        src/main/kotlin/org/veupathdb/service/eda/merge/core/request/RequestResources.kt \
        src/test/java/org/veupathdb/service/eda/common/model/MetaStudyResolverTest.java
git commit -m "feat(union): route union tokens through merged metadata (endpoint + merge layer)"
```

---

### Task 7: Union metadata cache keyed by token + member data-versions

Cache the merged result so Tier-1 (and Tier-2, Task 8) run once per data-version. Key = union token + each member's `lastModified` epoch-millis.

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/MetaStudyCache.java`
- Modify: `src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java` (accept a version supplier; consult cache)
- Test: `src/test/java/org/veupathdb/service/eda/common/model/MetaStudyCacheTest.java`

**Interfaces:**
- Consumes: `StudyId`, a `Function<String,Long>` returning a member's data-version (`study.getLastModified().getTime()`), and a builder `Supplier<APIStudyDetail>`.
- Produces:
  - `MetaStudyCache.cacheKey(StudyId id, Function<String,Long> versionOf) : String` — `token + "@" + join(sortedByMemberOrder versions)`.
  - `MetaStudyCache.getOrBuild(StudyId id, Function<String,Long> versionOf, Supplier<APIStudyDetail> builder) : APIStudyDetail` — `synchronized`, single-flight: returns the cached value if the version-suffixed key matches; else builds exactly once under the lock, caches, and returns. Concurrent callers for the same token share one build (no duplicate Tier-2 scan). Stale keys for the same token are replaced on data-version change.
  - `getOrBuild` also caches a **known-invalid** verdict: if the builder throws `BadRequestException`, the message is stored (keyed by data-version) so a repeatedly-requested incompatible union re-throws the same `400` without re-scanning. Unexpected (non-`BadRequestException`) failures are not cached, allowing retry.

- [ ] **Step 1: Write the failing test**

```java
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyCacheTest'`
Expected: FAIL — `MetaStudyCache` undefined.

- [ ] **Step 3: Implement `MetaStudyCache`**

```java
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
```

> CONCURRENCY TRADEOFF: this coarse monitor serializes all union builds — while one union's Tier-2 scan runs, a request for a *different* union waits. This matches MetadataCache's coarse-lock precedent and is fine because unions are rare and each token builds only once per data-version. Single-study requests are unaffected (they never touch this cache). **Upgrade path if union-build contention ever appears:** replace the single monitor with per-token single-flight — a `ConcurrentHashMap<String, CompletableFuture<Entry>>` keyed by the version-suffixed key, where the first caller installs a future and computes while others `join()` it; different tokens then build in parallel. Not needed for v1.

- [ ] **Step 4: Wire the cache into `MetaStudyResolver`**

Add an overload that consults a shared cache (single static instance is fine; it is keyed by data-version so it self-invalidates). Keep the pure `resolve*` methods for testability:

```java
  private static final MetaStudyCache CACHE = new MetaStudyCache();

  public static APIStudyDetail resolveStudyDetailCached(
      StudyId id, Function<String, APIStudyDetail> memberFetcher, Function<String, Long> versionOf) {
    if (!id.isUnion()) return memberFetcher.apply(id.getMembers().get(0));
    return CACHE.getOrBuild(id, versionOf, () -> MetaStudyMerger.merge(id, id.getMembers().stream().map(memberFetcher).toList()));
  }
```

Update `EdaSubsettingClient.getReferenceMetadata` and `StudiesService.getStudiesByStudyId` to use the cached path, supplying `versionOf = member -> Resources.getStudyResolver().getStudyById(member).getLastModified().getTime()`.

> IMPLEMENTATION NOTE: `getStudyById(member)` is already cached in `MetadataCache` (5-min refresh) and returns the lib `Study` carrying `getLastModified()`. This makes the union cache invalidate whenever a member's `lastModified` advances — which is the VDI/user-study data-version signal the spec requires (§7). Verify `Study.getLastModified()` is non-null for user studies before relying on it; if a member's version can be null, treat null as "always rebuild" (do not cache).

- [ ] **Step 5: Run tests + compile**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.MetaStudyCacheTest'` → PASS
Run: `./gradlew compileJava compileKotlin` → BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/MetaStudyCache.java \
        src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java \
        src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java \
        src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java \
        src/test/java/org/veupathdb/service/eda/common/model/MetaStudyCacheTest.java
git commit -m "feat(union): cache merged metadata keyed by token + member data-versions"
```

---

### Task 8: Tier 2 deep value-consistency check

Stream every member's full data for shared ancestor/root entities, dedup by entity ID, and confirm a deduplicated instance's variable values agree across members. Disagreement → `400` naming entity/variable/studies. Runs once at metadata-build time, inside the cached build (Task 7), so it's part of the "spinner cost".

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/UnionValueConsistencyChecker.java`
- Modify: `src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java` (run Tier 2 inside the cached builder for unions)
- Test: `src/test/java/org/veupathdb/service/eda/common/model/UnionValueConsistencyCheckerTest.java`

**Interfaces:**
- Consumes: merged `ReferenceMetadata`; a per-`(studyId, entityId)` stream supplier `BiFunction<String,String,CloseableIterator<Map<String,String>>>` (so the test injects fixture streams; production supplies `EdaSubsettingClient.getTabularDataIterator`).
- Produces:
  - `UnionValueConsistencyChecker.check(ReferenceMetadata unionMetadata, EntityDef sharedEntity, BiFunction<...> streamSupplier)` — throws `BadRequestException` on conflict. For pure union, the "shared ancestor/root entities" are the root and any ancestor whose instances recur across members; v1 checks the **root entity** (the case the motivating use case cares about) and any entity flagged shared.

- [ ] **Step 1: Write the failing test**

```java
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
    String pk = entity.getIdColumnDef().getVariableId(); // e.g. participant_id column

    BiFunction<String, String, CloseableIterator<Map<String, String>>> supplier = (studyId, entityId) ->
      studyId.equals("studyA")
        ? rows(List.of(Map.of(pk, "P1", "sex", "F"), Map.of(pk, "P2", "sex", "M")))
        : rows(List.of(Map.of(pk, "P2", "sex", "M"), Map.of(pk, "P3", "sex", "F"))); // P2 agrees

    assertDoesNotThrow(() -> UnionValueConsistencyChecker.check(md, entity, supplier));
  }

  @Test
  void rejectsWhenSharedInstanceValueConflicts() {
    var md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    String pk = entity.getIdColumnDef().getVariableId();

    BiFunction<String, String, CloseableIterator<Map<String, String>>> supplier = (studyId, entityId) ->
      studyId.equals("studyA")
        ? rows(List.of(Map.of(pk, "P2", "sex", "M")))
        : rows(List.of(Map.of(pk, "P2", "sex", "F"))); // conflict on P2.sex

    BadRequestException ex = assertThrows(BadRequestException.class,
      () -> UnionValueConsistencyChecker.check(md, entity, supplier));
    assertTrue(ex.getMessage().contains("P2"));
    assertTrue(ex.getMessage().contains("sex"));
  }
}
```

> Uses `TestStudies.unionMetadata(token, members)` and `ROOT_ID` (defined in the Shared Test Fixture section).

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.UnionValueConsistencyCheckerTest'`
Expected: FAIL — checker undefined.

- [ ] **Step 3: Implement the checker**

```java
package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.gusdb.fgputil.iterator.CloseableIterator;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Tier 2: confirms that entity instances shared across union members (same entity ID)
 * carry identical native variable values. One-time cost, cached by the caller.
 */
public final class UnionValueConsistencyChecker {

  private UnionValueConsistencyChecker() {}

  public static void check(ReferenceMetadata unionMetadata, EntityDef sharedEntity,
                           BiFunction<String, String, CloseableIterator<Map<String, String>>> streamSupplier) {
    String pkKey = sharedEntity.getIdColumnDef().getVariableId();
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
```

- [ ] **Step 4: Wire Tier 2 into the cached union build**

In `MetaStudyResolver.resolveReferenceMetadata` union path (the one that produces the `ReferenceMetadata` used for data requests), after building the union `ReferenceMetadata`, run the checker over the root entity (and any entity whose instances are shared) using `EdaSubsettingClient.getTabularDataIterator` as the supplier. Because this is the expensive step, it must execute **inside** the cached builder (Task 7) so it runs once per data-version. Concretely, thread Tier 2 through `resolveStudyDetailCached` so the cached unit includes the Tier-2 verdict:

```java
  // inside the union builder supplier used by MetaStudyCache.getOrBuild:
  APIStudyDetail merged = MetaStudyMerger.merge(id, memberDetails);          // Tier 1
  ReferenceMetadata md = new ReferenceMetadata(merged, id.getMembers());
  EntityDef root = md.getEntity(merged.getRootEntity().getId()).orElseThrow();
  UnionValueConsistencyChecker.check(md, root,
    (studyId, entityId) -> EdaSubsettingClient.getTabularDataIterator(
      studyId, java.util.List.of(),
      allNativeVarsStreamSpec(md, entityId)));       // stream all native vars of the entity
  // if check throws BadRequestException, MetaStudyCache stores the known-invalid verdict
  return merged;
```

> IMPLEMENTATION NOTE: `allNativeVarsStreamSpec(md, entityId)` builds a `StreamSpec(randomName, entityId)` containing the entity's native `VariableSpec`s (from `EntityDef.getVariables()` filtered to `VariableSource.NATIVE`). Add this small helper next to the wiring. For v1, check the **root** entity; extend to other shared ancestors when the extension case lands. Bound the scan and add a log line with row counts + elapsed millis (risk §13.2 telemetry).

- [ ] **Step 5: Run tests + compile**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.UnionValueConsistencyCheckerTest'` → PASS
Run: `./gradlew compileJava compileKotlin` → BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/UnionValueConsistencyChecker.java \
        src/main/java/org/veupathdb/service/eda/common/model/MetaStudyResolver.java \
        src/test/java/org/veupathdb/service/eda/common/model/UnionValueConsistencyCheckerTest.java
git commit -m "feat(union): Tier 2 deep value-consistency check (cached, surfaces on metadata request)"
```

---

### Task 9: PK sort comparator matching the DB collation

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/PkSortComparator.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/PkSortComparatorTest.java`

**Interfaces:**
- Consumes: nothing.
- Produces:
  - `PkSortComparator.forKeyColumns(List<String> orderedIdColumnKeys) : Comparator<Map<String,String>>` — compares two rows by the given id-column map keys in order, using the DB's **case-insensitive** collation (`String.CASE_INSENSITIVE_ORDER`, tie-broken deterministically).
  - `PkSortComparator.COLLATION : Comparator<String>` — the single pinned per-column collation comparator (swap here if DB verification demands a different collation).

- [ ] **Step 1: Write the failing test**

```java
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.PkSortComparatorTest'`
Expected: FAIL — comparator undefined.

- [ ] **Step 3: Implement**

```java
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
    Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER).thenComparing(Comparator.nullsFirst(Comparator.naturalOrder()));

  public static Comparator<Map<String, String>> forKeyColumns(List<String> orderedIdColumnKeys) {
    return (r1, r2) -> {
      for (String col : orderedIdColumnKeys) {
        int c = COLLATION.compare(r1.get(col), r2.get(col));
        if (c != 0) return c;
      }
      return 0;
    };
  }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.PkSortComparatorTest'`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/PkSortComparator.java \
        src/test/java/org/veupathdb/service/eda/common/model/PkSortComparatorTest.java
git commit -m "feat(union): PK sort comparator pinned to case-insensitive DB collation"
```

> AFTER MERGE (must-verify, spec §8.4 / §13.1): run a one-off check that a real study's SQL `ORDER BY` output equals the Java comparator ordering over its `ids_map`. If a mismatch is found (e.g. a linguistic NLS collation), change `PkSortComparator.COLLATION` to a matching `java.text.Collator` and re-run all merge tests. Track as a follow-up verification item, not a code placeholder.

---

### Task 10: K-way merge iterator with dedup

**Files:**
- Create: `src/main/java/org/veupathdb/service/eda/common/model/KWayMergeIterator.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/model/KWayMergeIteratorTest.java`

**Interfaces:**
- Consumes: `CloseableIterator<Map<String,String>>` (FgpUtil), `Comparator<Map<String,String>>` (Task 9), and a dedup key extractor `Function<Map<String,String>,String>` (the entity PK value).
- Produces:
  - `new KWayMergeIterator(List<CloseableIterator<Map<String,String>>> members, Comparator<Map<String,String>> keyComparator, Function<Map<String,String>,String> dedupKey)` implementing `CloseableIterator<Map<String,String>>`. Emits rows in `keyComparator` order; when the same `dedupKey` appears at the merge frontier from multiple members, emits once (first member in list wins); closes all member iterators on `close()`.

Assumption (verified): every member stream is already sorted by `keyComparator`. Dedup is frontier-only (drop a row whose id already appeared) — no per-row value comparison (Tier 2 vouches for value identity).

- [ ] **Step 1: Write the failing test**

```java
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.KWayMergeIteratorTest'`
Expected: FAIL — iterator undefined.

- [ ] **Step 3: Implement**

```java
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
```

> CORRECTNESS NOTE: `_lastEmittedKey` dedup relies on all copies of an id being adjacent in the globally-merged order — true because every member is PK-sorted and the comparator is total, so identical ids from different members are always neighbors at the frontier. This is why dedup can be frontier-only. Also dedups repeats **within** a member.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.model.KWayMergeIteratorTest'`
Expected: PASS (3 tests).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/model/KWayMergeIterator.java \
        src/test/java/org/veupathdb/service/eda/common/model/KWayMergeIteratorTest.java
git commit -m "feat(union): k-way merge iterator with frontier dedup by entity id"
```

---

### Task 11: Wire union merge into the tabular stream seam

Make `getSubsettingTabularStream` open one iterator per member and k-way merge them when the metadata is a union; otherwise take today's exact single-study path.

**Files:**
- Modify: `src/main/kotlin/org/veupathdb/service/eda/merge/core/request/MergedTabularRequestResources.kt` (`getSubsettingTabularStream`, l.61-74)
- Create: `src/main/java/org/veupathdb/service/eda/common/client/UnionTabularStreams.java` (Java helper the Kotlin calls, so the merge logic is unit-testable in Java)
- Test: `src/test/java/org/veupathdb/service/eda/common/client/UnionTabularStreamsTest.java`

**Interfaces:**
- Consumes: `ReferenceMetadata` (member list + `getTabularIdColumns`), `KWayMergeIterator` (T10), `PkSortComparator` (T9), `EdaSubsettingClient.getTabularDataIterator` (existing).
- Produces:
  - `UnionTabularStreams.open(ReferenceMetadata metadata, List<APIFilter> subsetFilters, StreamSpec spec, TabularStreamOpener opener) : CloseableIterator<Map<String,String>>` where `TabularStreamOpener` is `@FunctionalInterface (String studyId, List<APIFilter>, StreamSpec) -> CloseableIterator<Map<String,String>>`. If `metadata.getMemberStudyIds()` is empty → single call to `opener` with `metadata.getStudyId()`. If non-empty → open per member, k-way merge on the entity's ordered id columns, dedup by the entity PK value.
  - `UnionTabularStreams.orderedIdColumnKeys(ReferenceMetadata, String entityId) : List<String>` — the row-map keys for ancestor PKs (root→leaf) then entity PK, matching the DB ORDER BY.

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.generated.model.APIFilter;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionTabularStreamsTest {

  private static Map<String, String> r(String pk, String pkVal, String v) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal); m.put("sex", v); return m;
  }
  private static CloseableIterator<Map<String, String>> s(List<Map<String, String>> rows) {
    return CloseableIterator.of(rows.iterator());
  }

  @Test
  void singleStudyUsesOneOpenerCall() {
    ReferenceMetadata md = new ReferenceMetadata(
      TestStudies.oneEntityOneStringVar("studyA", "EUPA_1", "sex", List.of("F")));
    StreamSpec spec = new StreamSpec("s", "EUPA_1");
    List<String> opened = new ArrayList<>();
    var opener = (UnionTabularStreams.TabularStreamOpener) (studyId, f, sp) -> {
      opened.add(studyId); return s(List.of());
    };
    UnionTabularStreams.open(md, List.of(), spec, opener);
    assertEquals(List.of("studyA"), opened);
  }

  @Test
  void unionMergesPerMemberStreamsWithDedup() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    String pk = md.getEntity(TestStudies.ROOT_ID).orElseThrow().getIdColumnDef().getVariableId();
    StreamSpec spec = new StreamSpec("s", TestStudies.ROOT_ID);

    var opener = (UnionTabularStreams.TabularStreamOpener) (studyId, f, sp) ->
      studyId.equals("studyA")
        ? s(List.of(r(pk, "P1", "F"), r(pk, "P2", "M")))
        : s(List.of(r(pk, "P2", "M"), r(pk, "P3", "F")));

    CloseableIterator<Map<String, String>> merged = UnionTabularStreams.open(md, List.of(), spec, opener);
    List<String> ids = new ArrayList<>();
    while (merged.hasNext()) ids.add(merged.next().get(pk));
    merged.close();
    assertEquals(List.of("P1", "P2", "P3"), ids); // P2 deduped
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.UnionTabularStreamsTest'`
Expected: FAIL — helper undefined.

- [ ] **Step 3: Implement `UnionTabularStreams`**

```java
package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.*;
import org.veupathdb.service.eda.generated.model.APIFilter;

import java.util.*;

public final class UnionTabularStreams {

  private UnionTabularStreams() {}

  @FunctionalInterface
  public interface TabularStreamOpener {
    CloseableIterator<Map<String, String>> open(String studyId, List<APIFilter> filters, StreamSpec spec);
  }

  public static CloseableIterator<Map<String, String>> open(
      ReferenceMetadata metadata, List<APIFilter> subsetFilters, StreamSpec spec, TabularStreamOpener opener) {

    List<String> members = metadata.getMemberStudyIds();
    if (members.isEmpty()) {
      // single-study: today's exact path
      return opener.open(metadata.getStudyId(), subsetFilters, spec);
    }

    List<String> idKeys = orderedIdColumnKeys(metadata, spec.getEntityId());
    String entityPkKey = idKeys.get(idKeys.size() - 1); // entity PK is last in ORDER BY

    List<CloseableIterator<Map<String, String>>> perMember = new ArrayList<>();
    try {
      for (String member : members) {
        perMember.add(opener.open(member, subsetFilters, spec));
      }
    } catch (RuntimeException e) {
      perMember.forEach(UnionTabularStreams::closeQuietly);
      throw e;
    }

    // Safe to construct outside the try above: KWayMergeIterator's constructor closes every
    // member it was handed if priming a first row throws (see its ctor). Do NOT also wrap this
    // in the try — that would double-close on failure.
    return new KWayMergeIterator(
      perMember,
      PkSortComparator.forKeyColumns(idKeys),
      row -> compositeKey(row, idKeys));   // dedup on the FULL composite key
  }

  /** Ancestor PK columns root->leaf, then the entity PK column — matching the DB ORDER BY. */
  public static List<String> orderedIdColumnKeys(ReferenceMetadata metadata, String entityId) {
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();
    // metadata.getTabularIdColumns returns the entity's id columns; ensure ancestor->entity order.
    List<VariableDef> idCols = metadata.getTabularIdColumns(entity);
    List<String> keys = new ArrayList<>();
    for (VariableDef v : idCols) keys.add(rowKeyFor(v));
    return keys;
  }

  private static String compositeKey(Map<String, String> row, List<String> idKeys) {
    StringBuilder sb = new StringBuilder();
    for (String k : idKeys) sb.append(row.get(k)).append(' ');
    return sb.toString();
  }

  private static String rowKeyFor(VariableDef idColumn) {
    // Verified: id/ancestor PK columns are keyed in the row map by the entity's PK column
    // name, which equals the id column's variableId (EntityDef builds its idColumnDef with
    // variableId = idColumnName). Variable value columns use the bare variableId too
    // (default TallRowConversionUtils key function = Variable::getId).
    return idColumn.getVariableId();
  }

  private static void closeQuietly(CloseableIterator<?> it) {
    try { it.close(); } catch (Exception ignored) {}
  }
}
```

> ROW-KEY FORMAT (resolved by verification): id/ancestor PK columns are keyed by the entity's PK column name (= `idColumnName` = `idColumnDef.getVariableId()`); variable value columns are keyed by the bare `variableId` (default `TallRowConversionUtils.getTallToWideFunction` uses `Variable::getId`, not dot notation). `rowKeyFor` returns `idColumn.getVariableId()` accordingly, and the `TestStudies` fixtures use the same keys, so the unit tests are self-consistent. **One live check remains:** confirm `EdaSubsettingClient.getTabularDataIterator` → `FilteredResultFactory.tabularSubsetIterator` passes the **default** key function (bare id), not a dot-notation override. If it overrides, change `rowKeyFor` and `UnionDistribution`'s `valueKey` to `VariableDef.toDotNotation(...)` and update the fixtures. A wrong key silently breaks dedup and ancestor joins.

Also verify `orderedIdColumnKeys` returns ancestor-root→leaf-then-entity-PK order; if `getTabularIdColumns(entity)` returns a different order (or omits ancestor PKs), build the list explicitly from `metadata.getAncestors(entity)` (reversed to root-first) plus the entity's own PK column key.

- [ ] **Step 4: Wire the Kotlin seam**

`MergedTabularRequestResources.getSubsettingTabularStream` — delegate both branches through the union helper, passing `EdaSubsettingClient::getTabularDataIterator` as the opener:

```kotlin
  fun getSubsettingTabularStream(spec: StreamSpec): CloseableIterator<Map<String, String>> {
    val opener = UnionTabularStreams.TabularStreamOpener { studyId, filters, s ->
      EdaSubsettingClient.getTabularDataIterator(studyId, filters, s)
    }
    return if (spec.filtersOverride.isPresent && subsetFilters.isNotEmpty()) {
      val combined = StreamSpec(spec.streamName, spec.entityId).apply {
        addAll(spec)
        setFiltersOverride(subsetFilters + spec.filtersOverride.get())
      }
      UnionTabularStreams.open(metadata, subsetFilters, combined, opener)
    } else {
      UnionTabularStreams.open(metadata, subsetFilters, spec, opener)
    }
  }
```

> NOTE: single-study behavior is preserved because `UnionTabularStreams.open` with an empty member list makes exactly one `getTabularDataIterator(metadata.studyId, subsetFilters, spec)` call — identical to today.

- [ ] **Step 5: Run tests + compile**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.UnionTabularStreamsTest'` → PASS
Run: `./gradlew compileJava compileKotlin` → BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/UnionTabularStreams.java \
        src/main/kotlin/org/veupathdb/service/eda/merge/core/request/MergedTabularRequestResources.kt \
        src/test/java/org/veupathdb/service/eda/common/client/UnionTabularStreamsTest.java
git commit -m "feat(union): k-way merge per-member tabular streams at the merge seam"
```

---

### Task 12: Union-aware subset count

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java` (`getSubsetCount`, l.90-92)
- Test: `src/test/java/org/veupathdb/service/eda/common/client/EdaSubsettingClientCountTest.java`

**Interfaces:**
- Consumes: `metadata.getMemberStudyIds()`, `UnionTabularStreams.open` (T11) or a counting helper.
- Produces: `getSubsetCount(ReferenceMetadata, String entityId, List<APIFilter>)` returns the **deduped** count for unions (count over the merged stream), unchanged for single studies.

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EdaSubsettingClientCountTest {

  private static Map<String, String> row(String pk, String pkVal) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal); return m;
  }

  @Test
  void unionCountDedupesSharedIds() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    String pk = md.getEntity(TestStudies.ROOT_ID).orElseThrow().getIdColumnDef().getVariableId();

    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, sp) ->
      studyId.equals("studyA")
        ? CloseableIterator.of(List.of(row(pk, "P1"), row(pk, "P2")).iterator())
        : CloseableIterator.of(List.of(row(pk, "P2"), row(pk, "P3")).iterator());

    long count = EdaSubsettingClient.countUnionEntity(md, TestStudies.ROOT_ID, List.of(), opener);
    assertEquals(3L, count); // P2 counted once
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.EdaSubsettingClientCountTest'`
Expected: FAIL — `countUnionEntity` undefined.

- [ ] **Step 3: Implement**

Add a testable counting helper and branch `getSubsetCount`:

```java
  // package/public static, testable with an injected opener
  public static long countUnionEntity(ReferenceMetadata metadata, String entityId,
                                      List<APIFilter> filters, UnionTabularStreams.TabularStreamOpener opener) {
    StreamSpec spec = new StreamSpec("count-" + java.util.UUID.randomUUID(), entityId);
    // id columns only are needed to count distinct entity instances
    long n = 0;
    try (CloseableIterator<Map<String, String>> it = UnionTabularStreams.open(metadata, filters, spec, opener)) {
      while (it.hasNext()) { it.next(); n++; }
    } catch (Exception e) {
      throw new RuntimeException("Failed counting union entity " + entityId, e);
    }
    return n;
  }

  public static long getSubsetCount(ReferenceMetadata metadata, String entityId, List<APIFilter> subsetFilters) {
    if (metadata.getMemberStudyIds().isEmpty()) {
      return StudiesService.handleCountRequest(metadata.getStudyId(), entityId, subsetFilters).getCount();
    }
    return countUnionEntity(metadata, entityId, subsetFilters,
      (studyId, f, sp) -> getTabularDataIterator(studyId, f, sp));
  }
```

> NOTE: the merged stream already dedups by composite id key (Task 11), so counting emitted rows == distinct deduped instances. A `StreamSpec` with no added variables streams id columns only, keeping this cheap.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.EdaSubsettingClientCountTest'`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java \
        src/test/java/org/veupathdb/service/eda/common/client/EdaSubsettingClientCountTest.java
git commit -m "feat(union): union-aware subset count over deduped merged stream"
```

---

### Task 13: Union-aware distribution + root-vocab

Compute the categorical distribution over the deduped merged stream (consistent with count); return the merged vocabulary union for root-vocab.

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java` (`getCategoricalDistribution` l.94-120, `getVocabByRootEntity` l.122-140)
- Create: `src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java`

**Interfaces:**
- Consumes: merged stream (T11), `VariableSpec`.
- Produces:
  - `UnionDistribution.tally(ReferenceMetadata, VariableSpec, List<APIFilter>, UnionTabularStreams.TabularStreamOpener) : Map<String,Long>` — category value → deduped count, computed over the merged stream of the variable's entity.
  - `getCategoricalDistribution` for unions builds its `VariableDistributionPostResponse` from `UnionDistribution.tally`; single-study path unchanged.
  - `getVocabByRootEntity` for unions returns the union of member vocabularies (from the merged metadata) as the vocab stream; single-study path unchanged.

- [ ] **Step 1: Write the failing test**

```java
package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionDistributionTest {

  private static Map<String, String> row(String pk, String pkVal, String sexCol, String sex) {
    var m = new LinkedHashMap<String, String>(); m.put(pk, pkVal); m.put(sexCol, sex); return m;
  }

  @Test
  void tallyDedupesSharedInstancesThenCountsCategories() throws Exception {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    String pk = entity.getIdColumnDef().getVariableId();
    String sexCol = "sex"; // row-map key for the sex variable (VERIFY against decode key format)
    VariableSpec sexSpec = TestStudies.varSpec(TestStudies.ROOT_ID, "sex");

    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, sp) ->
      studyId.equals("studyA")
        ? CloseableIterator.of(List.of(row(pk, "P1", sexCol, "F"), row(pk, "P2", sexCol, "M")).iterator())
        : CloseableIterator.of(List.of(row(pk, "P2", sexCol, "M"), row(pk, "P3", sexCol, "F")).iterator());

    Map<String, Long> tally = UnionDistribution.tally(md, sexSpec, List.of(), opener);
    assertEquals(2L, tally.get("F")); // P1, P3
    assertEquals(1L, tally.get("M")); // P2 (deduped)
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.UnionDistributionTest'`
Expected: FAIL — `UnionDistribution` undefined.

- [ ] **Step 3: Implement `UnionDistribution.tally` and branch the client methods**

```java
package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.*;

public final class UnionDistribution {

  private UnionDistribution() {}

  public static Map<String, Long> tally(ReferenceMetadata metadata, VariableSpec varSpec,
      List<APIFilter> filters, UnionTabularStreams.TabularStreamOpener opener) {
    String entityId = varSpec.getEntityId();
    String valueKey = UnionTabularStreams.orderedIdColumnKeys(metadata, entityId).isEmpty()
      ? varSpec.getVariableId() : varSpec.getVariableId(); // VERIFY: use the row-map key for this var
    StreamSpec spec = new StreamSpec("dist-" + UUID.randomUUID(), entityId);
    // add the target variable so its column is present in the rows
    // spec.addVar(varSpec)  <-- use the actual StreamSpec API to include the variable
    Map<String, Long> counts = new LinkedHashMap<>();
    try (CloseableIterator<Map<String, String>> it = UnionTabularStreams.open(metadata, filters, spec, opener)) {
      while (it.hasNext()) {
        String v = it.next().get(valueKey);
        if (v != null) counts.merge(v, 1L, Long::sum);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed tallying union distribution for " + varSpec, e);
    }
    return counts;
  }
}
```

Branch `getCategoricalDistribution` in `EdaSubsettingClient`:

```java
    if (!metadata.getMemberStudyIds().isEmpty()) {
      Map<String, Long> tally = UnionDistribution.tally(metadata, varSpec, subsetFilters,
        (studyId, f, sp) -> getTabularDataIterator(studyId, f, sp));
      return buildDistributionResponse(tally, valueSpec); // assemble VariableDistributionPostResponse
    }
    // ... existing single-study body unchanged ...
```

Branch `getVocabByRootEntity`: for unions, return the merged vocabulary (union across members) as the vocab stream, sourced from the merged `ReferenceMetadata`'s variable (already computed in Tier 1). Keep the single-study SQL path unchanged.

> IMPLEMENTATION NOTE: `buildDistributionResponse` assembles a `VariableDistributionPostResponseImpl` from the tally in the same shape the single-study path returns (histogram/among-counts). Enumerate the response fields from the existing `StudiesService.handleDistributionRequest` return and populate them from `tally`. `spec.addVar(varSpec)` must use the real `StreamSpec` variable-add API (it extends `ArrayList<VariableSpec>`; `addVar`/`add` per the verified accessors). Resolve `valueKey` to the exact row-map key for the variable (MUST-VERIFY, same key-format issue as Task 11).

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.common.client.UnionDistributionTest'`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java \
        src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java \
        src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java
git commit -m "feat(union): union-aware distribution (deduped tally) and root-vocab (merged union)"
```

---

### Task 14: End-to-end union integration test

Prove a two-study union produces a correct merged tabular result with parent-child join correctness and correct count, exercising the whole path (metadata merge → member-list tagging → per-member stream open → k-way merge → `StreamingEntityNode` joins).

**Files:**
- Create: `src/test/java/org/veupathdb/service/eda/merge/UnionMergeIntegrationTest.java`

**Interfaces:**
- Consumes: everything above, with injected fixture streams (no live DB).

- [ ] **Step 1: Write the integration test**

```java
package org.veupathdb.service.eda.merge;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.client.UnionTabularStreams;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.TestStudies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UnionMergeIntegrationTest {

  private static Map<String, String> row(Map<String, String> m) { return new LinkedHashMap<>(m); }

  @Test
  void twoStudyUnionMergesChildEntityAndInheritsUnionedAncestor() throws Exception {
    // household (root) shared across members; participant (child) unioned underneath.
    ReferenceMetadata md = TestStudies.householdParticipantUnion("union(studyA,studyB)", List.of("studyA", "studyB"));
    String householdPk = "household_id";
    String participantPk = "participant_id";

    // studyA: H1 -> {PA1}; studyB: H1 (shared, agrees) -> {PB1}; H2 -> {PB2}
    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, spec) -> {
      String entityId = spec.getEntityId();
      if (entityId.equals(TestStudies.HOUSEHOLD_ID)) {
        return studyId.equals("studyA")
          ? CloseableIterator.of(List.of(Map.of(householdPk, "H1", "region", "north")).iterator())
          : CloseableIterator.of(List.of(Map.of(householdPk, "H1", "region", "north"),
                                         Map.of(householdPk, "H2", "region", "south")).iterator());
      } else { // participant
        return studyId.equals("studyA")
          ? CloseableIterator.of(List.of(Map.of(householdPk, "H1", participantPk, "PA1")).iterator())
          : CloseableIterator.of(List.of(Map.of(householdPk, "H1", participantPk, "PB1"),
                                         Map.of(householdPk, "H2", participantPk, "PB2")).iterator());
      }
    };

    // merged household stream: H1 deduped, H2 once
    var households = UnionTabularStreams.open(md, List.of(),
      TestStudies.streamSpec(TestStudies.HOUSEHOLD_ID), opener);
    List<String> hIds = new ArrayList<>();
    while (households.hasNext()) hIds.add(households.next().get(householdPk));
    households.close();
    assertEquals(List.of("H1", "H2"), hIds);

    // merged participant stream: three distinct participants, PK-sorted, ancestor pk present
    var participants = UnionTabularStreams.open(md, List.of(),
      TestStudies.streamSpec(TestStudies.PARTICIPANT_ID), opener);
    List<String> pIds = new ArrayList<>();
    while (participants.hasNext()) {
      var r = participants.next();
      assertNotNull(r.get(householdPk)); // ancestor pk carried for join
      pIds.add(r.get(participantPk));
    }
    participants.close();
    assertEquals(List.of("PA1", "PB1", "PB2"), pIds);
  }
}
```

> `TestStudies.householdParticipantUnion(...)`, `HOUSEHOLD_ID`, `PARTICIPANT_ID`, and `streamSpec(entityId)` are all defined in the Shared Test Fixture section. Order fixture rows by the PK sort key so the merge assertions hold. This test's participant assertions depend on `orderedIdColumnKeys` returning `[household_id, participant_id]` (ancestor before entity PK) — see the Task 11 verify note.

- [ ] **Step 2: Run the test**

Run: `./gradlew test --tests 'org.veupathdb.service.eda.merge.UnionMergeIntegrationTest'`
Expected: PASS.

- [ ] **Step 3: Run the full suite**

Run: `./gradlew test`
Expected: BUILD SUCCESSFUL, all tests pass.

- [ ] **Step 4: Commit**

```bash
git add src/test/java/org/veupathdb/service/eda/merge/UnionMergeIntegrationTest.java \
        src/test/java/org/veupathdb/service/eda/common/model/TestStudies.java
git commit -m "test(union): end-to-end two-study union merge + ancestor join + count"
```

---

## Deferred (designed-for, not built) — do NOT implement in this plan

- **Compute** (`/computes/*`, RabbitMQ + S3): metadata and token flow through, but job identity/cache-keying and worker data access are out of v1 (spec §11). Leave `ComputeController` single-study; a union token will simply fail to resolve there.
- **`extension(...)` meta-studies:** the `EntityDef` member list and token grammar reserve room; build no extension logic.
- **Binary-specific cross-study acceleration:** unnecessary — the path-agnostic merge uses each member's existing binary/SQL choice.
- **Raw union file download bundles:** access check is enforced (Task 2) but streaming a union bundle is not supported (returns 400).

## Post-implementation must-verifies (spec §13) — track as follow-ups, not code stubs

1. **Collation match (§8.4/§13.1):** verify `PkSortComparator.COLLATION` equals the DB `ORDER BY` on real study PKs; swap to a `Collator` if not. (Task 9 note.)
2. **Row-map key format (Task 11/13):** confirm the exact `Map<String,String>` key for id columns and variables so dedup/joins/tally align. This is the highest-risk detail.
3. **Tier 2 latency (§13.2):** measure first-call latency on a large union; the log line added in Task 8 provides row-count + elapsed telemetry. Consider a size guard.
4. **VDI/user-study `lastModified` semantics (§7/§13.4):** confirm `Study.getLastModified()` advances on every user-data change and is non-null for user studies; treat null as "always rebuild".
5. **Access-control completeness (§4/§13.5):** re-run the Task 2 Step 7 audit after all wiring; ensure no path authorizes the literal `union(...)` token.

## Self-review notes

- **Spec coverage:** §3 parser→T1; §4 access→T2; §5 model→T3/T5, endpoint audit→T6; §6 Tier1→T4, Tier2→T8; §7 caching→T7; §8 data path (seam→T11, comparator→T9, merge→T10, count/dist/vocab→T12/T13); §9 error table→T1/T2/T4/T8; §10 API/no-RAML→Global Constraints + T6; §11 deferred→"Deferred" section; §12 testing→each task's tests + T14; §13 risks→"Post-implementation must-verifies".
- **Known open verifications** are called out inline as MUST-VERIFY with concrete resolution steps (not vague "handle edge cases"): row-map key format, id-column ordering, DB collation, `TreeNode` traversal API, date-range bound type, `StreamSpec.addVar` API, `StudyAccess` factory shape. Each names exactly what to confirm and where.

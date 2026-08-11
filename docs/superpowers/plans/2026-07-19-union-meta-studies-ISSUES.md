# Union Meta-Studies — Execution Issues Log

Running log of findings during subagent-driven execution of
`2026-07-19-union-meta-studies.md`. Critical/Important findings are fixed in-task
(fix subagent → re-review) and recorded here as resolved. Minor findings are
recorded here and triaged at the final whole-branch review. Deferred MUST-VERIFY
items (live-DB checks) are tracked at the bottom.

## Findings

| Task | Severity | Finding | Status |
|------|----------|---------|--------|
| 1 | Critical | `StudyId.parse` guard over-rejected plain ids starting with `union` or containing `)`, violating identity pass-through. Root cause: a plan test case (`"union studyA,studyB)"`) contradicted the plan's strict `union(`-only design. | ✅ Resolved (commit 5496953b): dropped the guard, removed the contradicting assertion, added `plainIdStartingWithUnionPassesThrough` regression test. Plan updated to match. |
| 2 | Important | `StudyId.parse` trimmed input, so the plain-id auth path used a trimmed id where old code passed the raw string (byte-for-byte deviation; no security impact — cannot loosen entitlement). | ✅ Resolved (commit e6de42ee): plain ids preserved byte-for-byte; `plainIdPreservesOriginalStringByteForByte` regression test added. Plan synced. |
| 2 | Minor | Plan test `unionRequiresEveryMemberAuthorized` used a trivial `sa -> true` predicate that never exercised the deny path. | ✅ Resolved in-task (implementer used discriminating `StudyAccess::allowResultsAll`); plan synced. |
| 2 | Minor | Fully-qualified inline class refs (`StudyId`, `BadRequestException`) instead of imports in `StudyAccess.java` / `download/Service.java`. | Open — triage at final review. |
| 2 | Minor | `StudyAccessUnionTest` covers only last-member-denied, not first-member-denied (loop is order-agnostic, so not a real gap). | Open — triage at final review. |
| 4 | Important | `MetaStudyMerger.mergeDateRange` dropped `binUnits` — metadata loss for DATE variables in unions (central to the longitudinal use case). | ✅ Resolved (9f9780dd): binUnits (and other non-range date/number dd fields) carried through; `mergesDateRangeAndCarriesBinUnits` regression test added. |
| 4 | Minor | NUMBER and DATE merge paths had no direct tests. | ✅ Resolved (9f9780dd): added `mergesNumberRangeAsMinOfMinsMaxOfMaxes` + `mergesDateRangeAndCarriesBinUnits`. |
| 4 | Minor | `additionalProperties` (generated model catch-all) not copied onto merged vars/entities/study. Low risk (usually empty). | Open — triage at final review. |
| 4 | Note | `ReferenceMetadata(APIStudyDetail, List<String>)` constructor pulled forward from Task 5 (TestStudies requires it to compile). Semantics flagged for Task 5 to finalize/test. | ✅ Finalized in Task 5 (07152169): added `final _memberStudyIds` field + `getMemberStudyIds()`; single-arg ctor delegates. |
| 6 | Minor | Unused `import ...ReferenceMetadata` left in `RequestResources.kt` after switching to `getReferenceMetadata`. Warning-only. | Open — triage at final review. |
| 7 | Minor | Duplicated null-safe version-lookup (`EdaSubsettingClient.getMemberDataVersion` vs inline lambda in `StudiesService`) — could diverge if one is edited. | Open — triage at final review. |
| 7 | Minor | `versionOf` invoked twice per member (null-check then in `cacheKey`); latent fragility — `cacheKey` would bake literal `"null"` if a version flips null between reads (not reachable in current per-request call pattern). Consider hardening `getOrBuild`/`cacheKey` to reject null versions. | Open — triage at final review. |
| 8 | Important | Tier 2 skipped on the null-data-version cache-bypass path (ran Tier 1 only) — a null-version union with conflicting shared values could pass metadata build and be trusted by the data path (silent wrong data). | ✅ Resolved (5aeb5b5f): both union branches route through `buildAndVerify` (Tier 1 + Tier 2); only caching differs. |
| 8 | Minor | Tier-1+Tier-2 logic risked duplication across the two union branches. | ✅ Resolved (5aeb5b5f): single `buildAndVerify` is the only merge+check site. |
| 10 | Minor | `KWayMergeIterator` null dedup-key never dedups against a preceding null (`key != null && key.equals(...)` vs `Objects.equals`). Harmless today (dedup key is a non-null PK). | Open — triage at final review. |
| 10 | Minor | Test helper `s(Map... rows)` triggers unchecked-varargs warning; add `@SafeVarargs`. Cosmetic. | Open — triage at final review. |
| 10 | Minor | Test name `handlesThreeWayOverlapAndEmptyMembers` overstates fixture (id `B` overlaps 2 of 3 members, not a true 3-way collision). Naming nit. | Open — triage at final review. |
| 13 | Important | Union distribution `subsetSize`/`numMissingCases` wrong when the variable has null/missing values (dropped null rows; missing hardcoded to 0) — misleads viz totals. | ✅ Resolved (a74b64bb): `tally` now carries `totalSeen` + `missingSeen`; response sets subsetSize/numVarValues/numMissingCases/numDistinctEntityRecords per single-study semantics; test adds a missing-value row. |
| 13 | Limitation (v1) | Union root-vocab uses the union token as a placeholder pk for every row: correct bytes for non-study-dependent vocabs (common case), but collapses per-instance grouping for `hasStudyDependentVocabulary` variables. | Open — documented v1 limitation (below); follow-up ticket. |
| Final review | Important | Tier-2 value-check validates only the ROOT entity, but the hot-path merge dedups EVERY entity (composite key, first-member-wins). Shared non-root / multi-level-ancestor instances (or shared leaf IDs) with conflicting values would be silently resolved to the first member with no error. Aligns with the spec's "shared ancestor/root entities" language and the primary use case (sharing at root), but the "flagged shared" mechanism (plan §6) was never built. | Tracked as v1 limitation (below) + gated: only enable unions whose cross-member sharing is at the root (or a validated ancestor). |
| 11 | Note | Dedup uses the full composite id key (ancestors + entity PK), not the brief's literal "entity PK". Signed off (spec owner): equivalent under Tier-2 consistency, strictly safer, and correctly implements shared-key semantics (same child ID under same parent → deduped; different → unioned). `ReferenceMetadata.getTabularIdColumns` returns REVERSE of DB order — `orderedIdColumnKeys` correctly built from `getAncestors` reversed + entity PK. | ✅ Accepted (c21ceaa2). |

## Deferred MUST-VERIFY items (from the plan — resolve post-merge, before enabling union in prod)

- [ ] **Collation match (§8.4/§13.1):** confirm `PkSortComparator.COLLATION` equals the DB `ORDER BY` on real study PKs; swap to a `Collator` if not. (Task 9)
- [ ] **Key function (Task 11/13):** confirm `FilteredResultFactory.tabularSubsetIterator` uses the default (bare-id) key function, not a dot-notation override.
- [ ] **`orderedIdColumnKeys` ordering (Task 11):** confirm `getTabularIdColumns` returns ancestor-root→leaf-then-entity-PK (or build explicitly).
- [ ] **Tier 2 latency (§13.2):** measure first-call latency on a large union; consider a size guard.
- [ ] **VDI `lastModified` semantics (§7/§13.4):** confirm `Study.getLastModified()` advances on user-data change and is non-null.
- [ ] **Access-control completeness (§4/§13.5):** re-run the Task 2 audit after all wiring; no path authorizes the literal token.
- [ ] **Study-dependent vocab on unions (Task 13):** union root-vocab does not preserve per-root-instance grouping for `hasStudyDependentVocabulary` variables (emits the union token as pk for every row). Correct for the common non-study-dependent case. Needs proper per-instance handling before study-dependent-vocab variables are used on unions.
- [ ] **Tier-2 checks only the root entity (final review):** the deep value-consistency check runs on the root only, while the hot-path merge dedups every entity. Safe for the intended use case (cross-member sharing at the root; child records union with distinct IDs), but a shared non-root instance or a shared leaf ID with conflicting values would be silently first-member-wins. Before enabling unions with multi-level shared ancestors, extend the checker to every shared entity (note: the checker keys on the bare entity PK; the merge keys on the composite id — reconcile when extending to children).

## Deploy-enable gate (from final whole-branch review)

The dormant code is safe to MERGE (single-study unaffected; unions fail closed on auth; no union clients exist). Before unions are made REACHABLE in production, either add an **off-by-default feature flag** on the `union(...)` parse boundary, OR close these correctness/performance linchpins:
- Collation match (§8.4) — the merge comparator vs the DB `ORDER BY` **is the dedup/join linchpin**; a mismatch silently corrupts.
- Row-map key-function (Task 11) — confirm `tabularSubsetIterator` uses the default bare-id key.
- VDI `lastModified` reliably non-null (§7) — else large null-version unions full-rescan Tier-2 every request.
- Tier-2 root-only limitation (above) — gate to root-sharing unions until extended.

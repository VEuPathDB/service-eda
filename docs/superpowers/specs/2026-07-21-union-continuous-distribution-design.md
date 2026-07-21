# Union continuous (binned) distribution — design

**Date:** 2026-07-21
**Status:** proposed
**Branch:** `explore-study-merge`
**Related:** `docs/superpowers/specs/2026-07-18-union-meta-studies-design.md` (§8.5 count/distribution/vocab)

## 1. Goal

Make the subsetting **distribution** endpoint work for **continuous** variables
(integer / number / date) on a union meta-study, so the frontend
`HistogramFilter` renders for unions the same way it already does for a single
study. Today a continuous variable on a union reaches
`EdaSubsettingClient.getCategoricalDistribution`, whose categorical-only guard
throws `IllegalArgumentException("...non-categorical var (for now)...")`.

Categorical/ordinal/binary union distributions already work (deduped tally →
discrete response). This design adds the continuous case.

### Scope (agreed)

- **In scope (A — "make it work"):** correct binned distributions for continuous
  union variables, binning **in Java** over the merged/deduped union stream,
  using whatever `binSpec` the client sends.
- **Out of scope (B — deferred):** deriving a *better* default `binWidth` for the
  merged variable. `MetaStudyMerger` keeps its current behaviour of taking the
  **first member's** `binWidth` verbatim (`mergeNumberRange`/`mergeDateRange`).
  The range-ratio scaling idea (`binWidth * (newMax-newMin)/(oldMax-oldMin)`) is
  a separate follow-up spec.

## 2. Background — where binning actually happens

Clarified during brainstorming (correcting an earlier misstatement):

- **Binning is always done in Java**, never in SQL. FgpUtil's
  `AbstractBinDistribution` (subclasses `IntegerBinDistribution`,
  `FloatingPointBinDistribution`, `DateBinDistribution`) walks a sorted
  `(value, count)` stream into bins and computes stats
  (`subsetMin/Max/Mean`, `numVarValues`, `numMissingCases`, …).
- The single-study DB path only uses SQL for a **distinct-value tally**
  (`FilteredResultFactory.produceVariableDistribution`: `GROUP BY value`), then
  feeds that to the Java binner. The single-study distribution endpoint is
  **DB-only** — it does not branch on binary-files subsetting, and there is no
  binary-files value-tally implementation.
- The **union path already sidesteps all of that**: `UnionDistribution.tally`
  reads raw rows via `UnionTabularStreams.open` → `getTabularDataIterator`
  (which picks binary-files vs DB **per member**), tallies distinct values in
  Java, and dedups shared instances. So union distributions transparently
  support binary-files members — something the single-study endpoint lacks.

**Key reuse insight:** FgpUtil's binner is driven by a tiny
`DistributionStreamProvider` — a stream of `(value, count)` tuples (a `null` key
carries the missing-count) plus `getRecordCount()`. That is *exactly* what the
existing union `Tally` already contains. So we bin the union by adapting the
`Tally` into a `DistributionStreamProvider` and handing it to the FgpUtil
classes. **No reimplementation of binning, stats, or `MAX_BINS`.**

## 3. Approach

Reuse the existing categorical union tally as the shared intermediate for both
shapes, then dispatch:

- **CONTINUOUS** (type ∈ {INTEGER, NUMBER, DATE}) → adapt the `Tally` to a
  `DistributionStreamProvider`, build the matching FgpUtil bin distribution with
  a request-derived bin spec, call `generateDistribution()`, map the resulting
  `DistributionResult` to the API response via `ApiConversionUtil`.
- **everything else** (categorical / ordinal / binary) → existing
  `buildUnionDistributionResponse` (discrete: one bin per value).

## 4. Components

### 4.1 `UnionDistribution.tally(...)` — unchanged, reused

Produces `Tally(Map<String,Long> counts, long totalSeen, long missingSeen)` over
the merged/deduped stream. `counts` maps each raw value string → deduped count;
`totalSeen` = deduped instances (incl. missing); `missingSeen` = deduped
instances whose value is null/blank. Already covered by `UnionDistributionTest`.

### 4.2 `UnionDistribution.binnedResult(...)` — new

```
static DistributionResult binnedResult(
    Tally tally,
    APIVariableType type,                 // INTEGER | NUMBER | DATE
    Optional<BinSpecWithRange> binSpec,   // subsetting bin spec (from the request)
    ValueSpec valueSpec)                   // generated-model ValueSpec (COUNT | PROPORTION)
```

`binResult` maps the generated `ValueSpec` to FgpUtil's
`AbstractDistribution.ValueSpec` itself (a trivial COUNT/PROPORTION switch) —
`DistributionFactory.convertValueSpec` is private and `ApiConversionUtil.toInternalValueSpec`
returns a different intermediate enum, so neither is reused here.

Responsibilities:

1. **Adapt the tally to a `DistributionStreamProvider`** (private inner class):
   - `getRecordCount()` → `tally.totalSeen()`.
   - `getDistributionStream()` → `tally.counts()` entries **sorted ascending by
     typed value**, each mapped to `TwoTuple<>(valueString, count)`, plus (when
     `missingSeen > 0`) a `TwoTuple<>(null, missingSeen)` tuple.
   - **Sort is mandatory** — `AbstractBinDistribution` assumes ascending values
     and never revisits an earlier bin. Sort numerically for INTEGER/NUMBER,
     chronologically for DATE (ISO-8601 date strings sort lexically =
     chronologically). This is the one genuinely new correctness requirement.
2. **Build the FgpUtil distribution** by type:
   - INTEGER → `IntegerBinDistribution(provider, valueSpec, numberBinSpec)`
   - NUMBER → `FloatingPointBinDistribution(provider, valueSpec, numberBinSpec)`
   - DATE → `DateBinDistribution(provider, valueSpec, dateBinSpec)`
3. `return distribution.generateDistribution();`

### 4.3 Bin-spec adapters — new (private, in `UnionDistribution`)

The frontend always sends a full `binSpec` for continuous variables, so we build
the FgpUtil bin specs directly from the request `BinSpecWithRange`, with **no
subsetting `Variable`** (unlike `EdaNumberBinSpec`/`EdaDateBinSpec`, which need
one for their fallback):

- `NumberBinSpec` (FgpUtil): `getDisplayRangeMin/Max` → `binSpec.getDisplayRangeMin/Max`;
  `getBinSize` → `binSpec.getBinWidth`.
- `DateBinSpec` (FgpUtil): `getDisplayRangeMin/Max` → strings; `getBinSize` → int;
  `getBinUnits` → `ChronoUnit` (convert from the subsetting `BinUnits` enum,
  mirroring `EdaDateBinSpec`'s mapping).

### 4.4 `EdaSubsettingClient` — generalize the entry point

Rename `getCategoricalDistribution` → **`getSubsetDistribution`** (it now covers
both shapes) and thread the request bin spec through:

```
static VariableDistributionPostResponse getSubsetDistribution(
    ReferenceMetadata metadata, VariableSpec varSpec, List<APIFilter> filters,
    org.veupathdb.service.eda.generated.model.BinSpecWithRange apiBinSpec,
    ValueSpec valueSpec)
```

- Keep the NATIVE-source check. **Remove** the blanket CATEGORICAL guard.
- Union branch dispatch:
  - `dataShape == CONTINUOUS` → `Tally` →
    `UnionDistribution.binnedResult(tally, var.getType(),
    ApiConversionUtil.toInternalBinSpecWithRange(apiBinSpec), valueSpec)`
    → map with `ApiConversionUtil.toApiHistogramBins/Stats`.
  - else → existing `buildUnionDistributionResponse(tally, valueSpec)`.
- Non-union branch: unchanged delegation to `handleDistributionRequest`
  (retained for robustness; the endpoint only calls this for unions).

### 4.5 `StudiesService` endpoint — pass the bin spec

`postStudiesEntitiesVariablesDistribution...` already branches on
`isUnion()`. Update the union arm to call `getSubsetDistribution(...)` and pass
`request.getBinSpec()` through. Single-study arm unchanged.

### 4.6 Reused as-is

`ApiConversionUtil.{toApiHistogramBins, toApiHistogramStats,
toInternalBinSpecWithRange, toInternalValueSpec}` (all already `public static`).

## 5. Data flow (continuous union)

```
POST .../variables/{vid}/distribution  (isUnion, binSpec present)
  → StudiesService: getSubsetDistribution(md, varSpec, filters, apiBinSpec, valueSpec)
    → UnionDistribution.tally(...)            // merged+deduped stream → value→count, totals
    → UnionDistribution.binnedResult(...)     // sort tally → DistributionStreamProvider
        → FgpUtil {Integer|FloatingPoint|Date}BinDistribution.generateDistribution()
        → DistributionResult (bins + stats)   // binning + stats in Java
    → ApiConversionUtil.toApiHistogramBins/Stats
  → VariableDistributionPostResponse (200)
```

## 6. Error handling & edge cases

| Condition | Behaviour |
|---|---|
| `binSpec` absent on a continuous union | `400` (bin spec required). Divergence from single-study, which falls back to variable defaults; acceptable for scope A since the frontend always sends one, and ties into deferred B. Document it. |
| Non-continuous var (categorical/ordinal/binary) | existing discrete tally response |
| Continuous but type not in {INTEGER,NUMBER,DATE} | `400`, mirroring `DistributionFactory` |
| Requested bins exceed `MAX_BINS` (2000) | FgpUtil throws `IllegalArgumentException` → `400` |
| Empty subset / all-missing | FgpUtil emits the empty bins; stats reflect zero var-values, `numMissingCases` set |
| Values outside `displayRange` | counted in stats (min/max/mean) but not placed in a bin — matches single-study semantics |

## 7. Testing strategy

Pure-Java, no DB — the tally is an injected `Map`/`Tally`, matching existing
union tests.

- **`UnionDistribution.binnedResult` unit tests:**
  - INTEGER: values with a known `binWidth` → assert bin boundaries, per-bin
    counts, and stats (min/max/mean, numVarValues).
  - NUMBER: floating-point binning + mean.
  - DATE: `binUnits` (e.g. `year`/`month`) binning; assert bins.
  - **Unsorted tally input** → correct bins (proves the sort step).
  - **Missing values** → `numMissingCases` populated, missing excluded from bins.
  - Out-of-range values counted in stats but not binned.
- **`getSubsetDistribution` continuous union test** (mock `TabularStreamOpener`,
  like the categorical `UnionDistributionTest`): two members with an overlapping
  (deduped) instance → assert the shared instance is counted once in the
  histogram, end-to-end to the API response.
- **Dedup interaction:** a shared instance present in both members contributes
  one value to exactly one bin.

## 8. Files touched

- `common/client/UnionDistribution.java` — add `binnedResult` + private
  `DistributionStreamProvider` and bin-spec adapters. (If it grows past ~150
  lines, split the binning into a sibling `UnionBinnedDistribution`.)
- `common/client/EdaSubsettingClient.java` — rename + generalize
  `getCategoricalDistribution` → `getSubsetDistribution`; shape dispatch.
- `subset/service/StudiesService.java` — union arm passes `request.getBinSpec()`.
- Tests: extend `UnionDistributionTest`; update the caller rename.

## 9. Non-goals / deferred

- **B:** scaled/derived default `binWidth` in `MetaStudyMerger` — separate spec.
- Binary-files distribution for **single** studies — pre-existing gap, unchanged.
- `PROPORTION` value spec is handled by FgpUtil already; no special work.

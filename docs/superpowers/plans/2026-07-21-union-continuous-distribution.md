# Union Continuous (Binned) Distribution Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the subsetting distribution endpoint return correct binned histograms for continuous (integer/number/date) variables on a union meta-study.

**Architecture:** Reuse the existing union value `Tally` (deduped value→count + totals) as a `DistributionStreamProvider`, feed it to FgpUtil's bin-distribution classes with a request-derived bin spec, and map the resulting `DistributionResult` to the API response. Binning, stats, and MAX_BINS handling all come from FgpUtil — no reimplementation. Categorical/ordinal/binary unions keep the existing discrete tally path.

**Tech Stack:** Java 21; FgpUtil `org.gusdb.fgputil.distribution.*`; JUnit 5.

## Global Constraints

- Binning is application-side over the merged/deduped union stream — never SQL. Do not touch `FilteredResultFactory.produceVariableDistribution`.
- Scope A only: use the client-supplied `binSpec`. Do NOT change `MetaStudyMerger` binWidth defaults (deferred scope B).
- A `binSpec` is **required** for a continuous union distribution; its absence is a `400` (BadRequestException). No fallback to variable defaults.
- Single-study (non-union) distribution behaviour must remain byte-for-byte unchanged.
- The `(value, count)` stream fed to FgpUtil MUST be sorted ascending by typed value (numeric for integer/number, chronological for date).

## File Structure

- **Modify** `src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java` — add `binnedResult(...)` plus private `DistributionStreamProvider` and bin-spec adapter inner classes. Owns all FgpUtil-binning interaction.
- **Modify** `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java` — rename `getCategoricalDistribution` → `getSubsetDistribution`; add continuous-vs-discrete dispatch; thread the request bin spec.
- **Modify** `src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java` — union arm passes `request.getBinSpec()`.
- **Modify** `src/main/java/org/veupathdb/service/eda/data/core/AbstractPlugin.java` — update the two renamed-method call sites (categorical usages; pass `null` bin spec).
- **Modify** `src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java` — add binning unit tests.

---

### Task 1: Numeric binning in `UnionDistribution.binnedResult` (integer + number)

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java`

**Interfaces:**
- Consumes: existing `UnionDistribution.Tally` record `(Map<String,Long> counts, long totalSeen, long missingSeen)`.
- Produces: `public static DistributionResult binnedResult(Tally tally, APIVariableType type, Optional<BinSpecWithRange> binSpec, org.veupathdb.service.eda.generated.model.ValueSpec valueSpec)` where `BinSpecWithRange` is `org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange` and `DistributionResult` is `org.gusdb.fgputil.distribution.DistributionResult`.

- [ ] **Step 1: Write the failing tests** (append to `UnionDistributionTest.java`)

Add these imports at the top of the file:

```java
import org.gusdb.fgputil.distribution.AbstractDistribution;
import org.gusdb.fgputil.distribution.DistributionResult;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange;
```

Add these test methods:

```java
  @Test
  void binsIntegerTallyWithMissingAndUnsortedInput() {
    // insertion order deliberately NOT ascending, to prove the sort step
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("5", 3L);
    counts.put("1", 2L);
    counts.put("3", 1L);
    var tally = new UnionDistribution.Tally(counts, /*totalSeen*/ 8, /*missingSeen*/ 2);

    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin(1).setDisplayRangeMax(6).setBinWidth(2);

    DistributionResult result = UnionDistribution.binnedResult(
        tally, APIVariableType.INTEGER, java.util.Optional.of(spec),
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT);

    // 6 non-missing values fall into width-2 bins starting at 1: [1,3)=2, [3,5)=1, [5,7)=3
    long binnedTotal = result.getHistogramData().stream()
        .mapToLong(b -> b.getValue().longValue()).sum();
    assertEquals(6L, binnedTotal);
    assertEquals(6L, result.getStatistics().getNumVarValues());
    assertEquals(2L, result.getStatistics().getNumMissingCases());
    assertEquals(8L, result.getStatistics().getSubsetSize());
  }

  @Test
  void binsNumberTallyAndComputesMean() {
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("0.5", 1L);
    counts.put("1.5", 2L);
    counts.put("2.5", 1L);
    var tally = new UnionDistribution.Tally(counts, 4, 0);

    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin(0.0).setDisplayRangeMax(3.0).setBinWidth(1.0);

    DistributionResult result = UnionDistribution.binnedResult(
        tally, APIVariableType.NUMBER, java.util.Optional.of(spec),
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT);

    assertEquals(4L, result.getStatistics().getNumVarValues());
    // mean = (0.5 + 1.5*2 + 2.5) / 4 = 1.5
    assertEquals(1.5, ((Number) result.getStatistics().getSubsetMean()).doubleValue(), 1e-9);
  }

  @Test
  void continuousBinningRequiresBinSpec() {
    var tally = new UnionDistribution.Tally(new java.util.HashMap<>(), 0, 0);
    assertThrows(jakarta.ws.rs.BadRequestException.class, () ->
        UnionDistribution.binnedResult(tally, APIVariableType.NUMBER,
            java.util.Optional.empty(),
            org.veupathdb.service.eda.generated.model.ValueSpec.COUNT));
  }
```

- [ ] **Step 2: Run the tests to verify they fail**

Run: `./gradlew test --tests "*.UnionDistributionTest" 2>&1 | tail -20`
Expected: FAIL to compile — `binnedResult` does not exist.

- [ ] **Step 3: Implement `binnedResult` + numeric adapters**

Add imports to `UnionDistribution.java`:

```java
import jakarta.ws.rs.BadRequestException;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.distribution.AbstractDistribution;
import org.gusdb.fgputil.distribution.DistributionResult;
import org.gusdb.fgputil.distribution.DistributionStreamProvider;
import org.gusdb.fgputil.distribution.FloatingPointBinDistribution;
import org.gusdb.fgputil.distribution.IntegerBinDistribution;
import org.gusdb.fgputil.distribution.NumberBinDistribution;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
```

Add these members to the `UnionDistribution` class:

```java
  /**
   * Bins a continuous variable's deduped union tally into a histogram by reusing FgpUtil's
   * bin-distribution machinery. The tally is exposed as a {@link DistributionStreamProvider}
   * (sorted (value,count) tuples + record count) and handed to the type-appropriate FgpUtil
   * distribution with a request-derived bin spec. Date support is added in a later task.
   *
   * @param binSpec the request bin spec; REQUIRED for continuous variables (empty -> 400)
   */
  public static DistributionResult binnedResult(
      Tally tally, APIVariableType type, Optional<BinSpecWithRange> binSpec,
      org.veupathdb.service.eda.generated.model.ValueSpec valueSpec) {

    BinSpecWithRange spec = binSpec.orElseThrow(() -> new BadRequestException(
        "A bin spec is required for a continuous variable distribution on a union study."));

    AbstractDistribution.ValueSpec vs = switch (valueSpec) {
      case COUNT -> AbstractDistribution.ValueSpec.COUNT;
      case PROPORTION -> AbstractDistribution.ValueSpec.PROPORTION;
    };

    DistributionStreamProvider provider = tallyStreamProvider(tally, type);

    AbstractDistribution dist = switch (type) {
      case INTEGER -> new IntegerBinDistribution(provider, vs, new RequestNumberBinSpec(spec));
      case NUMBER -> new FloatingPointBinDistribution(provider, vs, new RequestNumberBinSpec(spec));
      default -> throw new BadRequestException(
          "Continuous distribution supports only integer, number, and date variables; got " + type);
    };
    return dist.generateDistribution();
  }

  /**
   * Adapts a deduped {@link Tally} to FgpUtil's {@link DistributionStreamProvider}: a stream of
   * (value,count) tuples sorted ascending by typed value (FgpUtil's binner never revisits an
   * earlier bin), plus a leading (null,count) tuple carrying the missing-value count, plus the
   * deduped record count.
   */
  private static DistributionStreamProvider tallyStreamProvider(Tally tally, APIVariableType type) {
    Comparator<String> byValue = switch (type) {
      case INTEGER, NUMBER -> Comparator.comparingDouble(Double::parseDouble);
      default -> Comparator.naturalOrder(); // DATE: ISO-8601 strings sort chronologically
    };
    List<TwoTuple<String, Long>> tuples = new ArrayList<>();
    if (tally.missingSeen() > 0) tuples.add(new TwoTuple<>(null, tally.missingSeen()));
    tally.counts().entrySet().stream()
        .sorted(Map.Entry.comparingByKey(byValue))
        .forEach(e -> tuples.add(new TwoTuple<>(e.getKey(), e.getValue())));
    return new DistributionStreamProvider() {
      @Override public Stream<TwoTuple<String, Long>> getDistributionStream() { return tuples.stream(); }
      @Override public long getRecordCount() { return tally.totalSeen(); }
    };
  }

  /** FgpUtil number bin spec built purely from the request bin spec (no subsetting Variable). */
  private static final class RequestNumberBinSpec implements NumberBinDistribution.NumberBinSpec {
    private final BinSpecWithRange _spec;
    RequestNumberBinSpec(BinSpecWithRange spec) { _spec = spec; }
    @Override public Object getDisplayRangeMin() { return _spec.getDisplayRangeMin(); }
    @Override public Object getDisplayRangeMax() { return _spec.getDisplayRangeMax(); }
    @Override public Object getBinSize() { return _spec.getBinWidth(); }
  }
```

- [ ] **Step 4: Run the tests to verify they pass**

Run: `./gradlew test --tests "*.UnionDistributionTest" 2>&1 | tail -20`
Expected: PASS (all UnionDistributionTest methods).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java \
        src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java
git commit -m "feat(union): numeric binned distribution over the union tally

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 2: Date binning support

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java`
- Test: `src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java`

**Interfaces:**
- Consumes: `binnedResult(...)` from Task 1.
- Produces: `DATE` branch in `binnedResult` + private `RequestDateBinSpec` implementing `org.gusdb.fgputil.distribution.DateBinDistribution.DateBinSpec`.

- [ ] **Step 1: Write the failing test** (append to `UnionDistributionTest.java`)

```java
  @Test
  void binsDateTallyByYear() {
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("2002-06-01T00:00:00", 1L);
    counts.put("2000-06-01T00:00:00", 2L);
    counts.put("2001-06-01T00:00:00", 1L);
    var tally = new UnionDistribution.Tally(counts, 4, 0);

    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin("2000-01-01T00:00:00")
        .setDisplayRangeMax("2003-01-01T00:00:00")
        .setBinWidth(1)
        .setBinUnits(org.veupathdb.service.eda.subset.model.distribution.BinUnits.YEAR);

    DistributionResult result = UnionDistribution.binnedResult(
        tally, APIVariableType.DATE, java.util.Optional.of(spec),
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT);

    long binnedTotal = result.getHistogramData().stream()
        .mapToLong(b -> b.getValue().longValue()).sum();
    assertEquals(4L, binnedTotal);
    assertEquals(4L, result.getStatistics().getNumVarValues());
  }

  @Test
  void dateBinningRequiresBinUnits() {
    var counts = new java.util.LinkedHashMap<String, Long>();
    counts.put("2000-06-01T00:00:00", 1L);
    var tally = new UnionDistribution.Tally(counts, 1, 0);
    BinSpecWithRange spec = new BinSpecWithRange()
        .setDisplayRangeMin("2000-01-01T00:00:00")
        .setDisplayRangeMax("2003-01-01T00:00:00")
        .setBinWidth(1); // no binUnits
    assertThrows(jakarta.ws.rs.BadRequestException.class, () ->
        UnionDistribution.binnedResult(tally, APIVariableType.DATE,
            java.util.Optional.of(spec),
            org.veupathdb.service.eda.generated.model.ValueSpec.COUNT));
  }
```

- [ ] **Step 2: Run the test to verify it fails**

Run: `./gradlew test --tests "*.UnionDistributionTest" 2>&1 | tail -20`
Expected: FAIL — `binnedResult` throws "supports only integer, number, and date" for `DATE` (date branch not yet added) / `RequestDateBinSpec` missing.

- [ ] **Step 3: Implement the date branch + `RequestDateBinSpec`**

Add imports to `UnionDistribution.java`:

```java
import java.time.temporal.ChronoUnit;
import org.gusdb.fgputil.distribution.DateBinDistribution;
import org.veupathdb.service.eda.subset.Utils;
```

In `binnedResult`, add the `DATE` case to the `switch (type)` (before `default`):

```java
      case DATE -> new DateBinDistribution(provider, vs, new RequestDateBinSpec(spec));
```

Add the adapter inner class (mirrors `EdaDateBinSpec`, but request-only — no subsetting Variable):

```java
  /** FgpUtil date bin spec built purely from the request bin spec (no subsetting Variable). */
  private static final class RequestDateBinSpec implements DateBinDistribution.DateBinSpec {
    private final BinSpecWithRange _spec;
    RequestDateBinSpec(BinSpecWithRange spec) {
      if (spec.getBinUnits() == null) {
        throw new BadRequestException("binUnits is required for date variable distributions.");
      }
      _spec = spec;
    }
    @Override public ChronoUnit getBinUnits() {
      return switch (_spec.getBinUnits()) {
        case DAY -> ChronoUnit.DAYS;
        case WEEK -> ChronoUnit.WEEKS;
        case MONTH -> ChronoUnit.MONTHS;
        case YEAR -> ChronoUnit.YEARS;
      };
    }
    @Override public int getBinSize() { return _spec.getBinWidth().intValue(); }
    @Override public String getDisplayRangeMin() {
      return Utils.standardizeLocalDateTime(castToString(_spec.getDisplayRangeMin()));
    }
    @Override public String getDisplayRangeMax() {
      return Utils.standardizeLocalDateTime(castToString(_spec.getDisplayRangeMax()));
    }
    private static String castToString(Object v) {
      if (v == null || v instanceof String) return (String) v;
      throw new BadRequestException("Date range boundary must be a date-formatted string value.");
    }
  }
```

- [ ] **Step 4: Run the test to verify it passes**

Run: `./gradlew test --tests "*.UnionDistributionTest" 2>&1 | tail -20`
Expected: PASS.

**Note (integration risk to verify in the running service):** the date-value strings in `tally.counts()` come from the tabular stream (`getTabularDataIterator`), not the DB distribution query. This test uses the `yyyy-MM-dd'T'HH:mm:ss` form. If a live union date histogram shows mis-parsed/empty bins, the tabular formatter emits a different date format than `DateBinDistribution` parses — the fix is to standardize each date value in `tallyStreamProvider` (map through `Utils.standardizeLocalDateTime`) for the `DATE` type. Verify against a real union date variable after Task 3.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/UnionDistribution.java \
        src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java
git commit -m "feat(union): date binned distribution over the union tally

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 3: Dispatch on shape in `EdaSubsettingClient` + wire the endpoint

**Files:**
- Modify: `src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java:154`
- Modify: `src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java:139`
- Modify: `src/main/java/org/veupathdb/service/eda/data/core/AbstractPlugin.java:373,382`
- Test: `src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java`

**Interfaces:**
- Consumes: `UnionDistribution.binnedResult(...)`; `ApiConversionUtil.{toApiHistogramBins, toApiHistogramStats, toInternalBinSpecWithRange}` (all `public static`).
- Produces: `public static VariableDistributionPostResponse getSubsetDistribution(ReferenceMetadata metadata, VariableSpec varSpec, List<APIFilter> filters, org.veupathdb.service.eda.generated.model.BinSpecWithRange apiBinSpec, ValueSpec valueSpec)` (renamed from `getCategoricalDistribution`; adds the `apiBinSpec` parameter before `valueSpec`).

- [ ] **Step 1: Write the failing test** (append to `UnionDistributionTest.java`)

This drives a continuous union distribution end-to-end through `getSubsetDistribution` with a mock opener, asserting a shared instance is deduped before binning. Reuse the existing test's row/opener style.

```java
  @Test
  void getSubsetDistributionBinsContinuousUnionDeduped() {
    ReferenceMetadata md = TestStudies.unionMetadata("union(studyA,studyB)", List.of("studyA", "studyB"));
    var entity = md.getEntity(TestStudies.ROOT_ID).orElseThrow();
    String pk = org.veupathdb.service.eda.common.model.VariableDef.toDotNotation(entity.getIdColumnDef());
    String ageCol = TestStudies.ROOT_ID + ".age";
    org.veupathdb.service.eda.generated.model.VariableSpec ageSpec = TestStudies.varSpec(TestStudies.ROOT_ID, "age");

    // NOTE: requires TestStudies.unionMetadata's root entity to expose a continuous "age" var;
    // if it does not, add it to TestStudies (see Step 3 note).
    UnionDistribution.TallyOpener opener = null; // placeholder; see actual opener type below
  }
```

Replace the placeholder body with the concrete opener (matching `UnionTabularStreams.TabularStreamOpener`, as used elsewhere in this test file):

```java
    UnionTabularStreams.TabularStreamOpener opener = (studyId, f, sp) -> {
      var rows = studyId.equals("studyA")
          ? List.of(java.util.Map.of(pk, "P1", ageCol, "5"),
                    java.util.Map.of(pk, "P2", ageCol, "15"))
          : List.of(java.util.Map.of(pk, "P2", ageCol, "15"),   // shared, deduped
                    java.util.Map.of(pk, "P3", ageCol, "25"));
      return CloseableIterator.of(rows.iterator());
    };

    org.veupathdb.service.eda.generated.model.BinSpecWithRange apiBinSpec =
        new org.veupathdb.service.eda.generated.model.BinSpecWithRangeImpl();
    apiBinSpec.setDisplayRangeMin(0);
    apiBinSpec.setDisplayRangeMax(30);
    apiBinSpec.setBinWidth(10);

    var response = EdaSubsettingClient.getSubsetDistributionForTest(
        md, ageSpec, List.of(), apiBinSpec,
        org.veupathdb.service.eda.generated.model.ValueSpec.COUNT, opener);

    long total = response.getHistogram().stream().mapToLong(b -> b.getValue().longValue()).sum();
    assertEquals(3L, total);            // P1, P2 (deduped), P3
    assertEquals(3L, response.getStatistics().getNumVarValues());
```

> The production `getSubsetDistribution` uses the real DB opener internally, which is not available in a unit test. To keep this testable without a live DB, in Step 3 extract a package-visible seam `getSubsetDistributionForTest(..., opener)` that the public `getSubsetDistribution` delegates to with the real opener. This mirrors how `UnionDistribution.tally` and `countUnionEntity` already accept an injected opener for testability.

- [ ] **Step 2: Run the test to verify it fails**

Run: `./gradlew test --tests "*.UnionDistributionTest" 2>&1 | tail -20`
Expected: FAIL to compile — `getSubsetDistributionForTest` / `age` var not present yet.

- [ ] **Step 3: Rename + generalize `getCategoricalDistribution`**

In `EdaSubsettingClient.java`, replace the method (currently at line 154). Remove the blanket `CATEGORICAL` guard; dispatch on data shape. Keep the NATIVE-source check and the non-union delegation.

```java
  public static VariableDistributionPostResponse getSubsetDistribution(
      ReferenceMetadata metadata, VariableSpec varSpec, List<APIFilter> filters,
      org.veupathdb.service.eda.generated.model.BinSpecWithRange apiBinSpec, ValueSpec valueSpec) {
    return getSubsetDistributionForTest(metadata, varSpec, filters, apiBinSpec, valueSpec,
        (studyId, f, sp) -> getTabularDataIterator(studyId, f, sp));
  }

  // Package-visible seam: the tabular-stream opener is injected so this is unit-testable
  // without a live DB (mirrors UnionDistribution.tally / countUnionEntity).
  static VariableDistributionPostResponse getSubsetDistributionForTest(
      ReferenceMetadata metadata, VariableSpec varSpec, List<APIFilter> filters,
      org.veupathdb.service.eda.generated.model.BinSpecWithRange apiBinSpec, ValueSpec valueSpec,
      UnionTabularStreams.TabularStreamOpener opener) {

    VariableDef var = metadata.getVariable(varSpec).orElseThrow();
    if (var.getSource() != VariableSource.NATIVE) {
      throw new IllegalArgumentException("Cannot call subsetting distribution endpoint with a non-native var: " + var);
    }

    if (metadata.getMemberStudyIds().isEmpty()) {
      // non-union: unchanged single-study path (handles all shapes)
      VariableDistributionPostRequest request = new VariableDistributionPostRequestImpl();
      request.setFilters(filters);
      request.setValueSpec(valueSpec);
      request.setBinSpec(apiBinSpec);
      return StudiesService.handleDistributionRequest(
          metadata.getStudyId(), varSpec.getEntityId(), varSpec.getVariableId(), request);
    }

    UnionDistribution.Tally tally = UnionDistribution.tally(metadata, varSpec, filters, opener);

    if (var.getDataShape() == APIVariableDataShape.CONTINUOUS) {
      DistributionResult result = UnionDistribution.binnedResult(
          tally, var.getType(),
          ApiConversionUtil.toInternalBinSpecWithRange(apiBinSpec), valueSpec);
      VariableDistributionPostResponse response = new VariableDistributionPostResponseImpl();
      response.setHistogram(ApiConversionUtil.toApiHistogramBins(result.getHistogramData()));
      response.setStatistics(ApiConversionUtil.toApiHistogramStats(result.getStatistics()));
      return response;
    }
    // categorical / ordinal / binary: existing discrete tally response
    return buildUnionDistributionResponse(tally, valueSpec);
  }
```

Add imports to `EdaSubsettingClient.java`:

```java
import org.gusdb.fgputil.distribution.DistributionResult;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.subset.service.ApiConversionUtil;
```

> If `getCategoricalDistribution` currently signs `throws`/returns via a `UnionTabularStreams.TabularStreamOpener` inline lambda for the tally, reuse that exact lambda expression `(studyId, f, sp) -> getTabularDataIterator(studyId, f, sp)` shown above.

- [ ] **Step 3b: Update the three call sites**

`StudiesService.java` (union arm, ~line 139) — pass the request bin spec:

```java
        ? EdaSubsettingClient.getSubsetDistribution(
            EdaSubsettingClient.getReferenceMetadata(studyId),
            VariableDef.newVariableSpec(entityId, variableId),
            request.getFilters(),
            request.getBinSpec(),
            request.getValueSpec() == null ? ValueSpec.COUNT : request.getValueSpec())
```

`AbstractPlugin.java:373` and `:382` — categorical usages; pass `null` bin spec:

```java
    VariableDistributionPostResponse response = EdaSubsettingClient.getSubsetDistribution(_referenceMetadata, varSpec, subsetFilters, null, ValueSpec.COUNT);
```
```java
    VariableDistributionPostResponse response = EdaSubsettingClient.getSubsetDistribution(_referenceMetadata, varSpec, subsetFilters, null, ValueSpec.PROPORTION);
```

- [ ] **Step 3c: Add a continuous `age` var to the union test fixture (if absent)**

Confirm `TestStudies.unionMetadata`'s root entity exposes a continuous `age` variable. If not, add an integer var `age` to the root entity builder used by `twoEntities(...)` in `TestStudies.java` (follow the existing `intVar(...)` / entity-builder pattern in that file), so the Task 3 test can request it. Keep existing vars intact so other tests are unaffected.

- [ ] **Step 4: Build and run the full affected test set**

Run: `./gradlew test --tests "*.UnionDistributionTest" --tests "*Union*" --tests "*MetaStudy*" 2>&1 | tail -25`
Expected: PASS. Also `./gradlew compileJava` must succeed (verifies the `AbstractPlugin` + `StudiesService` call-site updates compile).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/org/veupathdb/service/eda/common/client/EdaSubsettingClient.java \
        src/main/java/org/veupathdb/service/eda/subset/service/StudiesService.java \
        src/main/java/org/veupathdb/service/eda/data/core/AbstractPlugin.java \
        src/test/java/org/veupathdb/service/eda/common/client/UnionDistributionTest.java \
        src/test/java/org/veupathdb/service/eda/common/model/TestStudies.java
git commit -m "feat(union): union-aware continuous distribution endpoint (dispatch on shape)

Rename getCategoricalDistribution -> getSubsetDistribution; continuous union vars
bin via UnionDistribution.binnedResult, others keep the discrete tally response.
Thread the request binSpec through the endpoint.

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Self-Review

**Spec coverage:**
- §3/§4.2 numeric binning → Task 1. §4.3 date adapter → Task 2. §4.4 rename + dispatch → Task 3. §4.5 endpoint bin spec → Task 3b. §4.6 `ApiConversionUtil` reuse → Task 3. §6 `binSpec` required → Task 1 (`continuousBinningRequiresBinSpec`) + Global Constraints. §7 testing (unsorted, missing, dedup, types) → Tasks 1–3. §9 non-goals (no `MetaStudyMerger` change) → Global Constraints.
- Gap surfaced during planning and covered: `getCategoricalDistribution` has 3 call sites (StudiesService + AbstractPlugin ×2), all updated in Task 3b.

**Placeholder scan:** the Task 3 Step 1 test intentionally shows a placeholder line then replaces it with concrete code; final test body is complete. No "TODO/handle edge cases" left.

**Type consistency:** `binnedResult(Tally, APIVariableType, Optional<BinSpecWithRange>, generated.ValueSpec)` is defined identically in Task 1 and consumed in Task 3. `getSubsetDistribution(..., generated.BinSpecWithRange apiBinSpec, ValueSpec)` matches all three call sites. `RequestNumberBinSpec`/`RequestDateBinSpec` implement the exact FgpUtil interfaces (`NumberBinDistribution.NumberBinSpec`, `DateBinDistribution.DateBinSpec`).

**Risks flagged:** date value formatting from the tabular stream vs `DateBinDistribution` parsing (Task 2 note, verify live after Task 3); `TestStudies` may need an `age` var (Task 3c).

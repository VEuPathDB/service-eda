package org.veupathdb.service.eda.common.client;

import jakarta.ws.rs.BadRequestException;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.distribution.AbstractDistribution;
import org.gusdb.fgputil.distribution.DistributionResult;
import org.gusdb.fgputil.distribution.DistributionStreamProvider;
import org.gusdb.fgputil.distribution.FloatingPointBinDistribution;
import org.gusdb.fgputil.distribution.IntegerBinDistribution;
import org.gusdb.fgputil.distribution.NumberBinDistribution;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Computes a categorical distribution tally (category value -> deduped count) for a union
 * meta-study by streaming the target variable's entity over the deduped merged stream (see
 * {@link UnionTabularStreams#open}). Shared entity instances across member studies are deduped
 * by the merge before being counted, so the resulting tally is consistent with
 * {@link EdaSubsettingClient#countUnionEntity}.
 */
public final class UnionDistribution {

  private UnionDistribution() {}

  /**
   * Result of {@link #tally}: category counts plus enough bookkeeping to reproduce
   * single-study {@code HistogramStats} semantics (subsetSize, numMissingCases, etc.).
   *
   * @param counts map of category value to deduped count (rows with a null/blank value for the
   * variable are omitted, not counted as a category)
   * @param totalSeen total number of deduped rows observed (all entity instances in the subset,
   * whether or not they have a value for the variable)
   * @param missingSeen number of deduped rows whose value for the variable was null or blank
   */
  public record Tally(Map<String, Long> counts, long totalSeen, long missingSeen) {}

  /**
   * @param metadata union (or single-study) reference metadata
   * @param varSpec the categorical variable to tally
   * @param filters subset filters to apply
   * @param opener per-member tabular stream opener (injected for testability without a live DB)
   * @return a {@link Tally} carrying the category counts along with the total and missing row
   * counts, computed over the same deduped merged stream in a single pass
   */
  public static Tally tally(
      ReferenceMetadata metadata,
      VariableSpec varSpec,
      List<APIFilter> filters,
      UnionTabularStreams.TabularStreamOpener opener) {

    String entityId = varSpec.getEntityId();
    // row-map key for the value column is qualified "entityId.column", matching how the tabular
    // stream keys its columns (see FilteredResultFactory.getTabularOutputColumns). A bare
    // variableId never matches, so every row would read as null (i.e. missing).
    String valueKey = VariableDef.toDotNotation(varSpec);

    StreamSpec spec = new StreamSpec("dist-" + UUID.randomUUID(), entityId);
    spec.addVar(varSpec);

    Map<String, Long> counts = new LinkedHashMap<>();
    long totalSeen = 0;
    long missingSeen = 0;
    try (CloseableIterator<Map<String, String>> it = UnionTabularStreams.open(metadata, filters, spec, opener)) {
      while (it.hasNext()) {
        String value = it.next().get(valueKey);
        totalSeen++;
        if (value == null || value.isBlank()) {
          missingSeen++;
        } else {
          counts.merge(value, 1L, Long::sum);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed tallying union distribution for " + varSpec, e);
    }
    return new Tally(counts, totalSeen, missingSeen);
  }

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
}

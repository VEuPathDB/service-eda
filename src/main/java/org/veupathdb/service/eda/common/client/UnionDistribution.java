package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
}

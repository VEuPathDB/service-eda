package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
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
   * @param metadata union (or single-study) reference metadata
   * @param varSpec the categorical variable to tally
   * @param filters subset filters to apply
   * @param opener per-member tabular stream opener (injected for testability without a live DB)
   * @return map of category value to deduped count across the merged stream; rows with a null
   * value for the variable are omitted (not counted as a category)
   */
  public static Map<String, Long> tally(
      ReferenceMetadata metadata,
      VariableSpec varSpec,
      List<APIFilter> filters,
      UnionTabularStreams.TabularStreamOpener opener) {

    String entityId = varSpec.getEntityId();
    // row-map key for the value column is the bare variableId
    String valueKey = varSpec.getVariableId();

    StreamSpec spec = new StreamSpec("dist-" + UUID.randomUUID(), entityId);
    spec.addVar(varSpec);

    Map<String, Long> counts = new LinkedHashMap<>();
    try (CloseableIterator<Map<String, String>> it = UnionTabularStreams.open(metadata, filters, spec, opener)) {
      while (it.hasNext()) {
        String value = it.next().get(valueKey);
        if (value != null) {
          counts.merge(value, 1L, Long::sum);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed tallying union distribution for " + varSpec, e);
    }
    return counts;
  }
}

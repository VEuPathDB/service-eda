package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.validation.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.merge.core.MergeRequestProcessor;
import org.veupathdb.service.eda.merge.core.request.ComputeInfo;
import org.veupathdb.service.eda.merge.core.request.MergedTabularRequestResources;

import java.io.InputStream;
import java.util.List;

public class EdaMergingClient implements StreamingDataClient {
  @Override
  public StreamSpecValidator getStreamSpecValidator() {
    return new EdaMergingSpecValidator();
  }

  @NotNull
  public static String columnHeaderFor(@NotNull VariableSpec var) {
    return VariableDef.toDotNotation(var);
  }

  @NotNull
  public static InputStream getTabularDataStream(
    @NotNull ReferenceMetadata metadata,
    @NotNull List<APIFilter> subset,
    @NotNull List<DerivedVariableSpec> derivedVariableSpecs,
    @Nullable ComputeInfo computeInfo,
    @NotNull StreamSpec spec
  ) throws ValidationException {
    // if asked to include computed vars, do some validation before trying
    if (spec.isIncludeComputedVars() && computeInfo == null)
      throw new RuntimeException("Computed vars requested but no compute associated with this visualization");

    return new MergeRequestProcessor(new MergedTabularRequestResources(
      metadata.getStudyId(),
      derivedVariableSpecs,
      spec.getFiltersOverride().orElse(subset),
      computeInfo,
      spec.getEntityId(),
      spec
    )).createMergeDataStream();
  }
}

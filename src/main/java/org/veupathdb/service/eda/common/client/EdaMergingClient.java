package org.veupathdb.service.eda.common.client;

import kotlin.Pair;
import org.gusdb.fgputil.validation.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.merge.core.MergeRequestProcessor;
import org.veupathdb.service.eda.merge.core.request.ComputeInfo;
import org.veupathdb.service.eda.merge.core.request.MergedTabularRequestResources;
import org.veupathdb.service.eda.xgenerated.model.xComputeRequestBase;

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
    @Nullable Pair<String, Object> computeInfoOpt,
    @NotNull StreamSpec spec
  ) throws ValidationException {
    ComputeInfo computeInfo = null;

    // if asked to include computed vars, do some validation before trying
    if (spec.isIncludeComputedVars()) {

      // a compute name and config must be provided
      if (computeInfoOpt == null)
        throw new RuntimeException("Computed vars requested but no compute associated with this visualization");

      var config = (ComputeRequestBase) computeInfoOpt.getSecond();
      computeInfo = new ComputeInfo(
        computeInfoOpt.getFirst(),
        new EdaComputeClient.ComputeRequestBody(
          config.getStudyId(),
          config.getFilters(),
          config.getDerivedVariables(),
          xComputeRequestBase.getConfig(config)
        )
      );
    }

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

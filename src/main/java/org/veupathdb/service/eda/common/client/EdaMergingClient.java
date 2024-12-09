package org.veupathdb.service.eda.common.client;

import jakarta.ws.rs.ProcessingException;
import kotlin.Pair;
import org.gusdb.fgputil.Tuples;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.merge.ServiceExternal;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class EdaMergingClient extends StreamingDataClient {

  public EdaMergingClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    super(serviceBaseUrl, authHeader);
  }

  @Override
  public StreamSpecValidator getStreamSpecValidator() {
    return new EdaMergingSpecValidator();
  }

  @Override
  public String varToColumnHeader(VariableSpec var) {
    return columnHeaderFor(var);
  }

  public static String columnHeaderFor(VariableSpec var) {
    return VariableDef.toDotNotation(var);
  }

  @Override
  public ResponseFuture getTabularDataStream(
    ReferenceMetadata metadata,
    List<APIFilter> subset,
    StreamSpec spec
  ) throws ProcessingException {
    return getTabularDataStream(metadata, subset, Collections.emptyList(), Optional.empty(), spec);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static ResponseFuture getTabularDataStream(
    ReferenceMetadata metadata,
    List<APIFilter> subset,
    List<DerivedVariableSpec> derivedVariableSpecs,
    Optional<Pair<String, Object>> computeInfoOpt,
    StreamSpec spec
  ) throws ProcessingException {

    // build request object
    MergedEntityTabularPostRequest request = new MergedEntityTabularPostRequestImpl();
    request.setStudyId(metadata.getStudyId());
    request.setFilters(spec.getFiltersOverride().orElse(subset));
    request.setEntityId(spec.getEntityId());
    request.setDerivedVariables(derivedVariableSpecs);
    request.setOutputVariables(spec);

    // if asked to include computed vars, do some validation before trying
    if (spec.isIncludeComputedVars()) {
      // a compute name and config must be provided
      var computeInfo = computeInfoOpt.orElseThrow(() ->
        new RuntimeException("Computed vars requested but no compute associated with this visualization"));

      var computeSpec = new ComputeSpecForMergingImpl();
      computeSpec.setComputeName(computeInfo.getFirst());
      computeSpec.setComputeConfig(computeInfo.getSecond());
      request.setComputeSpec(computeSpec);
    }

    // make request
    return ClientUtil.makeAsyncPostRequest(getUrl("/merging-internal/query"), request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }

  @Deprecated
  public List<DerivedVariableMetadata> getDerivedVariableMetadata(String studyId, List<DerivedVariableSpec> derivedVariableSpecs) {
    // return empty if given empty
    if (derivedVariableSpecs.isEmpty())
      return Collections.emptyList();

    // create the request
    DerivedVariableBulkMetadataRequest request = new DerivedVariableBulkMetadataRequestImpl();
    request.setStudyId(studyId);
    request.setDerivedVariables(derivedVariableSpecs);

    return ServiceExternal.processDvMetadataRequest(studyId, derivedVariableSpecs);
  }

}

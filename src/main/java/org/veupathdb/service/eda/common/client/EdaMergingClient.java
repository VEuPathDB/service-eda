package org.veupathdb.service.eda.common.client;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import jakarta.ws.rs.ProcessingException;
import org.gusdb.fgputil.Tuples;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

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
    return VariableDef.toDotNotation(var);
  }

  @Override
  public ResponseFuture getTabularDataStream(
      ReferenceMetadata metadata,
      List<APIFilter> subset,
      Optional<Tuples.TwoTuple<String,Object>> computeInfoOpt,
      StreamSpec spec) throws ProcessingException {

    // build request object
    MergedEntityTabularPostRequest request = new MergedEntityTabularPostRequestImpl();
    request.setStudyId(metadata.getStudyId());
    request.setFilters(subset);
    request.setEntityId(spec.getEntityId());
    request.setDerivedVariables(metadata.getDerivedVariableFactory().getDerivedVariableSpecs());
    request.setOutputVariables(spec);

    // if asked to include computed vars, do some validation before trying
    if (spec.isIncludeComputedVars()) {
      // a compute name and config must be provided
      Tuples.TwoTuple<String,Object> computeInfo = computeInfoOpt.orElseThrow(() -> new RuntimeException(
          "Computed vars requested but no compute associated with this visualization"));
      // compute entity must be the same as, or a parent of, the target entity
      /* FIX on 12/21/22: compute config does not necessarily know output entity so cannot validate here without
                          first requesting the metadata object; may do that in the future but for now, skip
      String computeEntityId = computeInfo.getSecond().getOutputEntityId();
      EntityDef target = metadata.getEntity(spec.getEntityId()).orElseThrow();
      if (!computeEntityId.equals(target.getId()) &&
          metadata.getAncestors(target).stream().filter(ent -> ent.getId().equals(computeEntityId)).findFirst().isEmpty()) {
        throw new RuntimeException("Computed entity must be the same as, or an ancestor of, the target entity of the merged stream.");
      }*/
      ComputeSpecForMerging computeSpec = new ComputeSpecForMergingImpl();
      computeSpec.setComputeName(computeInfo.getFirst());
      computeSpec.setComputeConfig(computeInfo.getSecond());
      request.setComputeSpec(computeSpec);
    }

    // make request
    return ClientUtil.makeAsyncPostRequest(getUrl("/query"), request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }
}

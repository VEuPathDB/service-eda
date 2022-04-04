package org.veupathdb.service.eda.common.client;

import java.util.List;
import java.util.Map.Entry;
import jakarta.ws.rs.ProcessingException;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.VariableSpec;

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
      StreamSpec spec) throws ProcessingException {

    // build request object
    MergedEntityTabularPostRequest request = new MergedEntityTabularPostRequestImpl();
    request.setStudyId(metadata.getStudyId());
    request.setFilters(subset);
    request.setEntityId(spec.getEntityId());
    request.setDerivedVariables(metadata.getDerivedVariables());
    request.setOutputVariables(spec);

    // make request
    return ClientUtil.makeAsyncPostRequest(getUrl("/query"), request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }
}

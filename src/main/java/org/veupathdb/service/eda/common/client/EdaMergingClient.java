package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.gusdb.fgputil.functional.Either;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.DerivedVariable;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EdaMergingClient extends AbstractTabularDataClient {

  public EdaMergingClient(String serviceBaseUrl) {
    super(serviceBaseUrl);
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
  public InputStream getTabularDataStream(
      ReferenceMetadata metadata,
      List<APIFilter> subset,
      StreamSpec spec) {

    // build request object
    MergedEntityTabularPostRequest request = new MergedEntityTabularPostRequestImpl();
    request.setStudyId(metadata.getStudyId());
    request.setFilters(subset);
    request.setEntityId(spec.getEntityId());
    request.setDerivedVariables(metadata.getDerivedVariables());
    request.setOutputVariables(spec);

    // make request
    Either<Optional<InputStream>, RequestFailure> result = ClientUtil
        .makePostRequest(getUrl("/query"), request, "text/tabular");

    // handle result
    if (result.isLeft()) {
      return result.getLeft().orElseThrow(() ->
          new RuntimeException("Tabular request did not return a response body."));
    }
    throw new RuntimeException(result.getRight().toString());
  }
}

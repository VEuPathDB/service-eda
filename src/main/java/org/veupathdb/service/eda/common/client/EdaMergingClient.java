package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gusdb.fgputil.functional.Either;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationLevel;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.DerivedVariable;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EdaMergingClient extends AbstractTabularDataClient {

  public EdaMergingClient(String serviceBaseUrl) {
    super(serviceBaseUrl);
  }

  @Override
  public String varToColumnHeader(VariableSpec var) {
    return var.getEntityId() + "." + var.getVariableId();
  }

  @Override
  public ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata) {
    // FIXME: currently do not support derived vars
    // TODO: don't forget to check for unique stream names
    return new EdaSubsettingClient(null).validateStreamSpecs(streamSpecs, metadata);
  }

  public InputStream getTabularDataStream(
      String studyId,
      List<APIFilter> subset,
      List<DerivedVariable> derivedVariables,
      StreamSpec spec) {

    // build request object
    MergedEntityTabularPostRequest request = new MergedEntityTabularPostRequestImpl();
    request.setStudyId(studyId);
    request.setFilters(subset);
    request.setEntityId(spec.getEntityId());
    request.setDerivedVariables(derivedVariables);
    request.setOutputVariableIds(spec);

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

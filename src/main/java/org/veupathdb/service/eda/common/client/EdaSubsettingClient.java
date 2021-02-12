package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.gusdb.fgputil.functional.Either;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponse;

public class EdaSubsettingClient extends EdaClient {

  public EdaSubsettingClient(String serviceBaseUrl) {
    super(serviceBaseUrl);
  }

  public APIStudyDetail getStudy(String studyId) {
    return getResponseObject("/studies/" + studyId, StudyIdGetResponse.class).getStudy();
  }

  public InputStream getDataStream(
      APIStudyDetail study,
      List<APIFilter> subset,
      StreamSpec spec) {
    EntityTabularPostRequest request = new EntityTabularPostRequestImpl();
    request.setFilters(subset);
    request.setOutputVariableIds(spec.stream()
      .map(var -> var.getVariableId()) // subsetting service only takes var IDs (must match entity requested)
      .collect(Collectors.toList()));
    String url = "/studies/" + study.getId() + "/entities/" + spec.getEntityId() + "/tabular";

    try {
      Either<InputStream, RequestFailure> result = makePostRequest(url, request, "text/tabular");
      if (result.isLeft()) return result.getLeft();
      throw new RuntimeException(result.getRight().toString());
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

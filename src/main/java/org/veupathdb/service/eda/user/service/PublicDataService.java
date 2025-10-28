package org.veupathdb.service.eda.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.veupathdb.lib.container.jaxrs.model.UserInfo;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.service.eda.generated.model.AnalysisSummaryWithUser;
import org.veupathdb.service.eda.generated.resources.PublicAnalysesProjectId;
import org.veupathdb.service.eda.user.model.ProvenancePropsLookup;
import org.veupathdb.service.eda.user.model.UserDataFactory;

public class PublicDataService implements PublicAnalysesProjectId {

  @Override
  public GetPublicAnalysesByProjectIdResponse getPublicAnalysesByProjectId(String projectId) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    List<AnalysisSummaryWithUser> publicAnalyses = dataFactory.getPublicAnalyses();
    ProvenancePropsLookup.assignCurrentProvenanceProps(dataFactory, publicAnalyses);
    return GetPublicAnalysesByProjectIdResponse.respond200WithApplicationJson(populateOwnerData(publicAnalyses));
  }

  public List<AnalysisSummaryWithUser> populateOwnerData(List<AnalysisSummaryWithUser> analyses) {
    // collect the set of users for whom we need data (dedup)
    Set<Long> userIdsForLookup = analyses.stream()
        .map(AnalysisSummaryWithUser::getUserId)
        .map(Number::longValue)
        .collect(Collectors.toSet());

    // look up user information
    Map<Long, UserInfo> userData = UserProvider.getUsersById(userIdsForLookup);

    // distribute user information among the public analyses
    return analyses.stream()
        .peek(analysis -> {
          UserInfo user = userData.get(analysis.getUserId().longValue());
          if (user == null) {
            throw new RuntimeException("Public analysis " + analysis.getAnalysisId() +
                " owner ID " + analysis.getUserId() + " does not correspond to an existing user");
          }
          analysis.setUserName(user.getDisplayName());
          analysis.setUserOrganization(user.getOrganization());
        })
        .collect(Collectors.toList());
  }
}

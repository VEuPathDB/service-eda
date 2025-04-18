package org.veupathdb.service.eda.user.service;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import org.glassfish.jersey.server.ContainerRequest;
import org.veupathdb.lib.container.jaxrs.model.UserInfo;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.eda.generated.model.AnalysisListPostResponse;
import org.veupathdb.service.eda.generated.model.SingleAnalysisPublicInfo;
import org.veupathdb.service.eda.generated.model.SingleAnalysisPublicInfoImpl;
import org.veupathdb.service.eda.generated.resources.ImportAnalysisProjectId;
import org.veupathdb.service.eda.user.Utils;
import org.veupathdb.service.eda.user.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.veupathdb.service.eda.user.Utils.*;

@Authenticated(allowGuests = true)
public class ImportAnalysisService implements ImportAnalysisProjectId {

  @Context
  private ContainerRequest _request;

  @Override
  public GetImportAnalysisByProjectIdAndAnalysisIdResponse getImportAnalysisByProjectIdAndAnalysisId(String projectId, String analysisId) {
    return GetImportAnalysisByProjectIdAndAnalysisIdResponse.respond200WithApplicationJson(
      importAnalysis(projectId, analysisId, Optional.empty(), _request));
  }

  @Override
  public GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse getImportAnalysisInfoByProjectIdAndAnalysisId(String projectId, String analysisId) {
    AnalysisDetailWithUser analysis = new UserDataFactory(projectId).getAnalysisById(analysisId);
    SingleAnalysisPublicInfo info = new SingleAnalysisPublicInfoImpl();
    info.setStudyId(analysis.getStudyId());
    return GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse.respond200WithApplicationJson(info);
  }

  /**
   * After validating params, copies the analysis with the passed ID to a new
   * analysis owned by the active user (i.e. whose credentials were provided).
   * The optional userID is for an endpoint whose path includes both userId and
   * analysisId; so if present the analysis being copied must be owned by the
   * user passed in (or 404 will be thrown).  The copier's permission to access
   * the study of the analysis at the "visualization" level is also checked,
   * with a 403 result if disallowed.
   *
   * @param projectId project ID under which analysis is stored
   *
   * @param analysisId ID of analysis to be copied
   *
   * @param userIdOpt ID of owner of analysis to be copied (not required, but verified if provided)
   *
   * @param request container request, used to look up submitted credentials
   *
   * @return response describing newly created analysis
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static AnalysisListPostResponse importAnalysis(String projectId, String analysisId, Optional<String> userIdOpt, ContainerRequest request) {

    // create data factory (validates projectId)
    UserDataFactory dataFactory = new UserDataFactory(projectId);

    // look up analysis by ID
    AnalysisDetailWithUser oldAnalysis = dataFactory.getAnalysisById(analysisId);

    // if provided, verify URL's userId and analysisId match
    long userId = userIdOpt.map(userIdStr -> {
      try {
        long verifiedId = Long.parseLong(userIdStr);
        Utils.verifyOwnership(verifiedId, oldAnalysis);
        return verifiedId;
      } catch (NumberFormatException e) {
        throw new NotFoundException();
      }
    }).orElse(oldAnalysis.getUserId());

    // make sure user importing has access to this analysis' study
    // NOTE: even though we're calling getStudyId(), this method currently returns a dataset ID!!
    Utils.requireSubsettingPermission(request, oldAnalysis.getStudyId());

    // make a copy of the analysis, assign a new owner, check display name (must be unique) and insert
    UserInfo newOwner = Utils.getActiveUser(request);
    dataFactory.addUserIfAbsent(newOwner);
    UserInfo provenanceOwner = UserProvider.getUsersById(List.of(userId)).get(userId);
    AnalysisDetailWithUser newAnalysis = new AnalysisDetailWithUser(
      IdGenerator.getNextAnalysisId(dataFactory), newOwner.getUserId(), oldAnalysis, provenanceOwner);

    // If the owner ID has changed (meaning we are copying to a new user) AND we
    // have some derived variables attached to the analysis, copy the derived
    // variables from the old owner to the new owner.
    if (newOwner.getUserId() != userId && !isNullOrEmpty(newAnalysis.getDescriptor().getDerivedVariables())) {
      newAnalysis.getDescriptor().setDerivedVariables(copyDerivedVariables(
        dataFactory,
        newOwner,
        newAnalysis.getDescriptor().getDerivedVariables()
      ));
    }

    dataFactory.insertAnalysis(newAnalysis);
    return newAnalysis.getIdObject();
  }

  private static List<String> copyDerivedVariables(
    UserDataFactory dataFactory,
    UserInfo newOwner,
    List<String> originalDerivedVarsIds
  ) {
    // Look up the original derived variables from the database.
    var originalVars = dataFactory.getDerivedVariables(originalDerivedVarsIds);

    // Fetch all the derived variables owned by the target user.
    var newOwnerVars = dataFactory.getDerivedVariablesForUser(newOwner.getUserId());

    // Build an index of the derived variables that the new owner already has
    // or has a copy of.
    var newOwnerVarMap  = new HashMap<String, String>(newOwnerVars.size() * 2);
    newOwnerVars.forEach(it -> {
      newOwnerVarMap.put(it.getVariableID(), it.getVariableID());
      callIfPresent(it.getProvenance(), prov -> newOwnerVarMap.put(prov.getCopiedFrom(), it.getVariableID()));
    });

    // Make a list to hold the derived variable IDs that we will actually use
    // to attach to the new analysis copy. This list may contain a combination
    // of either existing derived variables already owned by newOwner, or new
    // copies of derived variables that were not previously owned by newOwner.
    var newDerivedVarIdList = new ArrayList<String>(originalDerivedVarsIds.size());

    // For each of the source analysis' derived variables
    for (var oldDerivedVar : originalVars) {
      // Check if newOwner already has a derived variable that was copied from
      // the source analysis' derived variable.
      var hit = newOwnerVarMap.get(oldDerivedVar.getVariableID());

      if (hit != null) {
        newDerivedVarIdList.add(hit);
        continue;
      }

      // Check if newOwner already has a derived variable that was the origin
      // of the source analysis' derived variable
      if (oldDerivedVar.getProvenance() != null) {
        hit = newOwnerVarMap.get(oldDerivedVar.getProvenance().getCopiedFrom());

        if (hit != null) {
          newDerivedVarIdList.add(hit);
          continue;
        }
      }

      // If we've made it this far, then the current source analysis derived
      // variable is new to newOwner.  Create a copy of it for newOwner and add
      // it to the list for return vars.
      var newDerivedVarId = issueUUID();
      dataFactory.addDerivedVariable(new DerivedVariableRow(newDerivedVarId, newOwner.getUserId(), oldDerivedVar));
      newDerivedVarIdList.add(newDerivedVarId);
    }

    return newDerivedVarIdList;
  }
}

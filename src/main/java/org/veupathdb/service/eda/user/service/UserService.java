package org.veupathdb.service.eda.user.service;

import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import org.glassfish.jersey.server.ContainerRequest;
import org.veupathdb.lib.container.jaxrs.errors.UnprocessableEntityException;
import org.veupathdb.lib.container.jaxrs.model.UserInfo;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.generated.resources.UsersUserId;
import org.veupathdb.service.eda.user.Utils;
import org.veupathdb.service.eda.user.model.*;

import static org.gusdb.fgputil.functional.Functions.also;
import static org.veupathdb.service.eda.user.Utils.*;

@Authenticated(allowGuests = true)
public class UserService implements UsersUserId {

  @Context
  private ContainerRequest _request;

  @Override
  public GetUsersPreferencesByUserIdAndProjectIdResponse getUsersPreferencesByUserIdAndProjectId(String userId, String projectId) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    String prefs = dataFactory.readPreferences(user.getUserId());
    return GetUsersPreferencesByUserIdAndProjectIdResponse.respond200WithApplicationJson(prefs);
  }

  @Override
  public PutUsersPreferencesByUserIdAndProjectIdResponse putUsersPreferencesByUserIdAndProjectId(String userId, String projectId, String entity) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    dataFactory.addUserIfAbsent(user);
    dataFactory.writePreferences(user.getUserId(), entity);
    return PutUsersPreferencesByUserIdAndProjectIdResponse.respond202();
  }

  @Override
  public GetUsersAnalysesByUserIdAndProjectIdResponse getUsersAnalysesByUserIdAndProjectId(String userId, String projectId) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    List<AnalysisSummary> summaries = dataFactory.getAnalysisSummaries(Utils.getAuthorizedUser(_request, userId).getUserId());
    ProvenancePropsLookup.assignCurrentProvenanceProps(dataFactory, summaries);
    return GetUsersAnalysesByUserIdAndProjectIdResponse.respond200WithApplicationJson(summaries);
  }

  @Override
  public PostUsersAnalysesByUserIdAndProjectIdResponse postUsersAnalysesByUserIdAndProjectId(String userId, String projectId, AnalysisListPostRequest entity) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    dataFactory.addUserIfAbsent(user);
    AnalysisDetailWithUser newAnalysis = new AnalysisDetailWithUser(
        IdGenerator.getNextAnalysisId(dataFactory), user.getUserId(), entity);
    dataFactory.insertAnalysis(newAnalysis);
    return PostUsersAnalysesByUserIdAndProjectIdResponse.respond200WithApplicationJson(newAnalysis.getIdObject());
  }

  @Override
  public PatchUsersAnalysesByUserIdAndProjectIdResponse patchUsersAnalysesByUserIdAndProjectId(String userId, String projectId, AnalysisListPatchRequest entity) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    performBulkDeletion(dataFactory, user, entity.getAnalysisIdsToDelete());
    performInheritGuestAnalyses(dataFactory, user, entity.getInheritOwnershipFrom());
    return PatchUsersAnalysesByUserIdAndProjectIdResponse.respond202();
  }

  @Override
  public GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse getUsersAnalysesByUserIdAndProjectIdAndAnalysisId(String userId, String projectId, String analysisId) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    AnalysisDetailWithUser analysis = dataFactory.getAnalysisById(analysisId);
    Utils.verifyOwnership(user.getUserId(), analysis);
    ProvenancePropsLookup.assignCurrentProvenanceProps(dataFactory, List.of(analysis));
    return GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse.respond200WithApplicationJson(analysis);
  }

  @Override
  public PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse patchUsersAnalysesByUserIdAndProjectIdAndAnalysisId(String userId, String projectId, String analysisId, SingleAnalysisPatchRequest entity) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    AnalysisDetailWithUser analysis = dataFactory.getAnalysisById(analysisId);
    Utils.verifyOwnership(user.getUserId(), analysis);

    editAnalysis(user, analysis, entity);

    dataFactory.updateAnalysis(analysis);
    return PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse.respond202();
  }

  @Override
  public DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse deleteUsersAnalysesByUserIdAndProjectIdAndAnalysisId(String userId, String projectId, String analysisId) {
    UserDataFactory dataFactory = new UserDataFactory(projectId);
    UserInfo user = Utils.getAuthorizedUser(_request, userId);
    Utils.verifyOwnership(dataFactory, user.getUserId(), analysisId);
    dataFactory.deleteAnalyses(analysisId);
    return DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse.respond202();
  }

  @Override
  public PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse postUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisId(String userId, String projectId, String analysisId) {
    return PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse.respond200WithApplicationJson(
        ImportAnalysisService.importAnalysis(projectId, analysisId, Optional.of(userId), _request));
  }

  @Override
  public GetUsersDerivedVariablesByUserIdAndProjectIdResponse getUsersDerivedVariablesByUserIdAndProjectId(String userId, String projectId) {
    var user        = Utils.getAuthorizedUser(_request, userId);
    var dataFactory = new UserDataFactory(projectId);
    var resultRows  = dataFactory.getDerivedVariablesForUser(user.getUserId());

    return GetUsersDerivedVariablesByUserIdAndProjectIdResponse.respond200WithApplicationJson(
      resultRows.stream()
        .map(DerivedVariableRow::toGetResponse)
        .toList());
  }

  @Override
  public PostUsersDerivedVariablesByUserIdAndProjectIdResponse postUsersDerivedVariablesByUserIdAndProjectId(
    String userId,
    String projectId,
    DerivedVariablePostRequest entity
  ) {
    var user = Utils.getAuthorizedUser(_request, userId);
    var dataFactory = new UserDataFactory(projectId);

    validateDerivedVariablePostBody(entity);
    Utils.requireSubsettingPermission(_request, entity.getDatasetId());

    // TODO: validate entity ID!!  This will require a docker-compose change so
    //  we can have access to the subsetting service URL.
    //  https://github.com/VEuPathDB/EdaUserService/issues/31

    var variableID = Utils.issueUUID();

    dataFactory.addDerivedVariable(new DerivedVariableRow(variableID, user.getUserId(), entity));

    return PostUsersDerivedVariablesByUserIdAndProjectIdResponse.respond200WithApplicationJson(also(new DerivedVariablePostResponseImpl(), res -> {
      res.setVariableId(variableID);
      res.setEntityId(entity.getEntityId());
    }));
  }

  @Override
  public GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse getUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableId(
    String userId,
    String projectId,
    String derivedVariableId
  ) {
    var user = Utils.getAuthorizedUser(_request, userId);
    var dataFactory = new UserDataFactory(projectId);
    var variable = dataFactory.getDerivedVariableById(derivedVariableId).orElseThrow(NotFoundException::new);

    if (variable.getUserID() != user.getUserId())
      throw new ForbiddenException();

    return GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse.respond200WithApplicationJson(variable.toGetResponse());
  }

  @Override
  public PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse patchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableId(
    String userId,
    String projectId,
    String derivedVariableId,
    DerivedVariablePatchRequest entity
  ) {
    var user = Utils.getAuthorizedUser(_request, userId);
    var dataFactory = new UserDataFactory(projectId);
    var variable = dataFactory.getDerivedVariableById(derivedVariableId).orElseThrow(NotFoundException::new);

    if (variable.getUserID() != user.getUserId())
      throw new ForbiddenException();

    var displayName = entity.getDisplayName() == null || entity.getDisplayName().isBlank()
      ? variable.getDisplayName()
      : entity.getDisplayName();
    var description = entity.getDescription() == null
      ? variable.getDescription()
      : entity.getDescription();

    checkMaxSize(DerivedVariableRow.MAX_DISPLAY_NAME_LENGTH, "displayName", displayName);
    checkMaxSize(DerivedVariableRow.MAX_DESCRIPTION_LENGTH, "description", description);

    dataFactory.patchDerivedVariable(derivedVariableId, displayName, description);

    return PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse.respond204();
  }

  private void performBulkDeletion(UserDataFactory dataFactory, UserInfo user, List<String> analysisIdsToDelete) {
    if (analysisIdsToDelete == null || analysisIdsToDelete.isEmpty())
      return;
    try {
      String[] idArray = analysisIdsToDelete.toArray(new String[0]);
      Utils.verifyOwnership(dataFactory, user.getUserId(), idArray);
      dataFactory.deleteAnalyses(idArray);
    }
    catch (NotFoundException nfe) {
      // validateOwnership throws not found if ID does not exist; convert to 400
      throw new BadRequestException(nfe.getMessage());
    }
  }

  private void performInheritGuestAnalyses(UserDataFactory dataFactory, UserInfo user, Long guestUserId) {
    if (guestUserId == null)
      return;
    if (user.isGuest())
      throw new BadRequestException("Guest users cannot inherit analyses.");
    dataFactory.addUserIfAbsent(user);
    dataFactory.transferGuestAnalysesOwnership(guestUserId, user.getUserId());
  }

  private static void editAnalysis(UserInfo user, AnalysisDetail analysis, SingleAnalysisPatchRequest entity) {
    boolean changeMade = false;
    if (entity.getIsPublic() != null) {
      if (user.isGuest() && entity.getIsPublic()) {
        throw new BadRequestException("Guest users cannot make their analyses public.");
      }
      changeMade = true; analysis.setIsPublic(entity.getIsPublic());
    }
    if (entity.getDisplayName() != null) {
      changeMade = true; analysis.setDisplayName(
          checkMaxSize(50, "displayName", checkNonEmpty("displayName", entity.getDisplayName())));
    }
    if (entity.getDescription() != null) {
      changeMade = true; analysis.setDescription(
          checkMaxSize(4000, "description", entity.getDescription()));
    }
    if (entity.getDescriptor() != null) {
      changeMade = true;
      analysis.setDescriptor(entity.getDescriptor());

      // Derived variable ids (variableId of each spec) must be unique
      var seenIds = new HashSet<String>();
      for (var spec : getDerivedVariables(analysis))
        if (!seenIds.add(spec.getVariableId()))
          throw new BadRequestException("Derived variable ids must be unique within an analysis");
    }
    if (entity.getNotes() != null) {
      changeMade = true; analysis.setNotes(entity.getNotes());
    }
    if (changeMade) {
      analysis.setModificationTime(Utils.getCurrentDateTimeString());
    }
  }

  private static List<DerivedVariableSpec> getDerivedVariables(AnalysisDetail analysis) {
    return Optional.ofNullable(analysis.getDescriptor())
      .map(AnalysisDescriptor::getDerivedVariables)
      .orElseGet(Collections::emptyList);
  }

  private static void validateDerivedVariablePostBody(DerivedVariablePostRequest body) {
    var errors = new HashMap<String, List<String>>();

    if (isNullOrBlank(body.getDatasetId()))
      errors.put("datasetId", List.of("field is required"));

    if (isNullOrBlank(body.getEntityId()))
      errors.put("entityId", List.of("field is required"));

    if (isNullOrBlank(body.getDisplayName()))
      errors.put("displayName", List.of("field is required"));
    else if (body.getDisplayName().length() > DerivedVariableRow.MAX_DISPLAY_NAME_LENGTH)
      errors.put("displayName", List.of("field must not be greater than 256 characters in length"));

    if (isNullOrBlank(body.getFunctionName()))
      errors.put("functionName", List.of("field is required"));

    if (body.getConfig() == null)
      errors.put("config", List.of("field is required"));
    else if (!(body.getConfig() instanceof Map))
      errors.put("config", List.of("field must be an object"));

    if (isNullOrBlank(body.getDescription()))
      body.setDescription(null);
    else if (body.getDescription().length() > DerivedVariableRow.MAX_DESCRIPTION_LENGTH)
      errors.put("description", List.of("field must not be greater than 4000 characters in length"));

    if (!errors.isEmpty())
      throw new UnprocessableEntityException(errors);
  }

}

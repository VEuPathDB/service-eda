package org.veupathdb.service.eda.access.service.permissions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.jersey.server.ContainerRequest;
import org.gusdb.fgputil.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.lib.container.jaxrs.model.User;
import org.veupathdb.service.eda.access.controller.Util;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.access.model.ApprovalStatus;
import org.veupathdb.service.eda.access.model.DatasetProps;
import org.veupathdb.service.eda.access.service.userdataset.UserDatasetIsaStudies;
import org.veupathdb.service.eda.access.service.dataset.DatasetRepo;
import org.veupathdb.service.eda.access.service.provider.ProviderRepo;
import org.veupathdb.service.eda.access.service.staff.StaffRepo;
import org.veupathdb.service.eda.access.service.user.EndUserRepo;
import org.veupathdb.service.eda.access.service.user.EndUserUtil;

import static org.veupathdb.service.eda.util.Exceptions.errToRuntime;

public class PermissionService
{
  private static PermissionService instance;

  public PermissionsGetResponse getUserPermissions(ContainerRequest request) {
    return getUserPermissions(Util.requireUser(request));
  }

  public static StudyPermissionInfo getUserPermissions(ContainerRequest request, String datasetId) {
    try {
      User user = Util.requireUser(request);

      // find the one for this study if it exists
      var studyPermission = Optional.ofNullable(getUserPermissions(user.getUserId(), datasetId))
        // if found, convert for return
        .map(entry -> {
          StudyPermissionInfo info = new StudyPermissionInfoImpl();
          info.setDatasetId(datasetId);
          info.setStudyId(entry.getStudyId());
          info.setIsUserStudy(entry.getIsUserStudy());
          info.setActionAuthorization(entry.getActionAuthorization());
          return info;
        });

      if (studyPermission.isPresent())
        return studyPermission.get();

      // otherwise, user does not have study visibility but want to see if it's a user study
      return UserDatasetIsaStudies.getUserStudyByDatasetId(datasetId).orElseThrow(
        () -> new NotFoundException("No study exists with dataset ID: " + datasetId)
      );
    }
    catch (WebApplicationException e) {
      throw e;
    }
    catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  public static PermissionMap getPermissionMap(long userId, boolean grantAll) throws Exception {
      // assign specific permissions on each dataset for this user
      var datasetPerms = getPermissionMap(
        grantAll,
        // level of access assigned to each dataset
        DatasetRepo.Select.getDatasetProps(),
        // if datasetId is present, then user is provider; boolean indicates isManager
        ProviderRepo.Select.datasets(userId),
        // list of datasetIds user has approved access for
        EndUserRepo.Select.datasets(userId)
      );

      // supplement official studies with studies from user datasets
      datasetPerms.putAll(UserDatasetIsaStudies.getUserDatasetPermissions(userId));

      return datasetPerms;
  }

  public static DatasetPermissionEntry getUserPermissions(long userId, String datasetId) {
    return errToRuntime(() -> getUserPermissions(userId)).get(datasetId);
  }

  public static PermissionMap getUserPermissions(long userId) throws Exception {
    return getPermissionMap(userId, StaffRepo.Select.byUserId(userId).isPresent());
  }

  public static PermissionsGetResponse getUserPermissionsResponse(long userId) {
    var out = new PermissionsGetResponseImpl();

    try {
      Wrapper<Boolean> grantAll = new Wrapper<>(false);

      // check if current user is staff and set outgoing perms accordingly
      StaffRepo.Select.byUserId(userId)
        .ifPresent(s -> {
          // all staff get access to all studies
          grantAll.set(true);
          // assign staff role
          out.setIsStaff(true);
          if (s.isOwner()) {
            // staff/owner is essentially a superuser
            out.setIsOwner(true);
          }
        });

      // set permission map on permissions object
      out.setPerDataset(getPermissionMap(userId, grantAll.get()));

      return out;
    } catch (WebApplicationException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  public PermissionsGetResponse getUserPermissions(User user) {
    var out = new PermissionsGetResponseImpl();

    try {
      Wrapper<Boolean> grantAll = new Wrapper<>(false);

      // check if current user is staff and set outgoing perms accordingly
      StaffRepo.Select.byUserId(user.getUserId())
        .ifPresent(s -> {
          // all staff get access to all studies
          grantAll.set(true);
          // assign staff role
          out.setIsStaff(true);
          if (s.isOwner()) {
            // staff/owner is essentially a superuser
            out.setIsOwner(true);
          }
        });

      // set permission map on permissions object
      out.setPerDataset(getPermissionMap(user.getUserId(), grantAll.get()));

      return out;
    }
    catch (WebApplicationException e) {
      throw e;
    }
    catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  private static PermissionMap getPermissionMap(
    boolean grantToAllDatasets,
    List<DatasetProps> datasetProps,
    Map<String, Boolean> providerInfoMap,
    Map<String, ApprovalStatus> approvalStatusMap
  ) {
    var permissionMap = new PermissionMap();
    for (DatasetProps dataset : datasetProps) {

      DatasetPermissionEntry permEntry = new DatasetPermissionEntryImpl();

      permEntry.setStudyId(dataset.studyId);
      permEntry.setSha1Hash(dataset.sha1hash);
      permEntry.setDisplayName(dataset.displayName);
      permEntry.setShortDisplayName(dataset.shortDisplayName);
      permEntry.setDescription(dataset.description);
      permEntry.setIsUserStudy(false);

      boolean isProvider = providerInfoMap.containsKey(dataset.datasetId);

      // set permission type for this dataset
      permEntry.setType(isProvider ?
        DatasetPermissionLevel.PROVIDER :
        DatasetPermissionLevel.ENDUSER);

      // is manager if isProvider and provider info map has value true
      permEntry.setIsManager(isProvider && providerInfoMap.get(dataset.datasetId));

      ApprovalStatus requestStatus = approvalStatusMap.get(dataset.datasetId);
      permEntry.setAccessRequestStatus(EndUserUtil.convertApproval(requestStatus));

      boolean accessGranted = requestStatus == ApprovalStatus.APPROVED;
      boolean grantAllPermsForThisDataset = grantToAllDatasets || isProvider || accessGranted;

      ActionList actions = getActionList(dataset, grantAllPermsForThisDataset);

      permEntry.setActionAuthorization(actions);

      // add to map
      if (permissionMap.containsKey(dataset.datasetId)) {
        throw new IllegalStateException("Database (datasetpresenter table or studyiddatasetid table) " +
          "contains more than one row for dataset ID " + dataset.datasetId);
      }
      permissionMap.put(dataset.datasetId, permEntry);
    }

    return permissionMap;
  }

  public static PermissionService getInstance() {
    if (instance == null)
      instance = new PermissionService();

    return instance;
  }

  @NotNull
  private static ActionList getActionList(DatasetProps dataset, boolean grantAllPermsForThisDataset) {
    ActionList actions = new ActionListImpl();

    // all users have access to the study page of all studies
    actions.setStudyMetadata(true);

    // controls search, visualizations, small results
    boolean allowBasicAccess = grantAllPermsForThisDataset || dataset.accessLevel.allowsBasicAccess();
    actions.setSubsetting(allowBasicAccess);
    actions.setVisualizations(allowBasicAccess);
    actions.setResultsFirstPage(allowBasicAccess);

    // controls access to full dataset, downloads
    boolean allowFullAccess = grantAllPermsForThisDataset || dataset.accessLevel.allowsFullAccess();
    actions.setResultsAll(allowFullAccess);
    return actions;
  }
}

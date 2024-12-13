package org.veupathdb.service.eda.access.service.userdataset;

import io.vulpine.lib.query.util.basic.BasicPreparedReadQuery;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.access.repo.DB;
import org.veupathdb.service.eda.access.repo.SQL;
import org.veupathdb.service.eda.access.service.QueryUtil;
import org.veupathdb.service.eda.access.util.SqlUtil;
import org.veupathdb.service.eda.generated.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDatasetIsaStudies {

  // pseudo column name returned by SQL
  private static final String IS_OWNER_COL = "is_owner";
  private static final String VDI_CONTROL_SCHEMA_TEMPLATE_STRING = "\\$VDI_CONTROL_SCHEMA\\$";
  private static final String VDI_DATASETS_SCHEMA_TEMPLATE_STRING = "\\$VDI_DATASETS_SCHEMA\\$";

  /**
   * Looks up the ISA Study user datasets owned by or shared with the user
   * with the passed ID, converts them into a map of permission objects,
   * keyed on dataset ID, which it returns.
   *
   * @param userId user ID for which study permissions should be added
   */
  @SuppressWarnings("resource")
  public static Map<String, DatasetPermissionEntry> getUserDatasetPermissions(long userId) throws Exception {
    return new BasicPreparedReadQuery<>(
        String.format(SQL.Select.UserDatasets.ByUserAccess,
            Resources.getVdiControlSchema(), Resources.getVdiDatasetsSchema(), Resources.getVdiControlSchema()),
        QueryUtil.getInstance()::getAppDbConnection,
        rs -> {
          Map<String, DatasetPermissionEntry> userStudies = new HashMap<>();
          while (rs.next()) {
            String datasetId = rs.getString(DB.Column.AvailableUserDatasets.DatasetId);
            String studyId = rs.getString(DB.Column.AvailableUserDatasets.StudyId);
            boolean isOwner = rs.getBoolean(IS_OWNER_COL);
            String name = rs.getString(DB.Column.AvailableUserDatasets.Name);
            String description = rs.getString(DB.Column.AvailableUserDatasets.Description);
            userStudies.put(datasetId, createDatasetPermissionEntry(studyId, isOwner, name, description));
          }
          return userStudies;
        },
        ps -> {
          ps.setLong(1, userId);
          ps.setLong(2, userId);
        }
    ).execute().getValue();
  }

  private static DatasetPermissionEntry createDatasetPermissionEntry(String studyId, boolean isOwner, String name, String description) {

    DatasetPermissionEntry permEntry = new DatasetPermissionEntryImpl();

    permEntry.setStudyId(studyId);
    permEntry.setSha1Hash(""); // not provided or needed for user dataset studies
    permEntry.setIsUserStudy(true);
    permEntry.setDisplayName(name);
    permEntry.setShortDisplayName(name);
    permEntry.setDescription(description);
    permEntry.setAccessRequestStatus(ApprovalStatus.UNREQUESTED); // no access requests on user datasets

    // set permission type for this dataset
    permEntry.setType(isOwner ?
        DatasetPermissionLevel.PROVIDER :
        DatasetPermissionLevel.ENDUSER);

    permEntry.setIsManager(isOwner);

    // if study is owned by or has been shared with a user, full access is granted
    ActionList actions = new ActionListImpl();
    actions.setStudyMetadata(true);
    actions.setSubsetting(true);
    actions.setVisualizations(true);
    actions.setResultsFirstPage(true);
    actions.setResultsAll(true);

    permEntry.setActionAuthorization(actions);
    return permEntry;
  }

  @SuppressWarnings("resource")
  public static Optional<StudyPermissionInfo> getUserStudyByDatasetId(String datasetId) throws Exception {
    return new BasicPreparedReadQuery<>(
        SQL.Select.UserDatasets.ByDatasetId.formatted(Resources.getVdiDatasetsSchema()),
        QueryUtil.getInstance()::getAppDbConnection,
        SqlUtil.optParser(rs -> {
          ActionList nullActions = new ActionListImpl();
          nullActions.setStudyMetadata(false);
          nullActions.setResultsAll(false);
          nullActions.setVisualizations(false);
          nullActions.setResultsFirstPage(false);
          nullActions.setSubsetting(false);
          StudyPermissionInfo info = new StudyPermissionInfoImpl();
          info.setDatasetId(datasetId);
          info.setStudyId(rs.getString(DB.Column.AvailableUserDatasets.StudyId));
          info.setIsUserStudy(true);
          info.setActionAuthorization(nullActions);
          return info;
        }),
        SqlUtil.prepareSingleString(datasetId)
    ).execute().getValue();
  }

}

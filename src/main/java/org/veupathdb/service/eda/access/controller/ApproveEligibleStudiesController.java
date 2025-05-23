package org.veupathdb.service.eda.access.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.server.annotations.AdminRequired;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.eda.generated.model.EndUserPatch;
import org.veupathdb.service.eda.generated.model.EndUserPatchImpl;
import org.veupathdb.service.eda.generated.resources.ApproveEligibleAccessRequests;
import org.veupathdb.service.eda.access.model.ApprovalStatus;
import org.veupathdb.service.eda.access.model.DatasetAccessLevel;
import org.veupathdb.service.eda.access.model.DatasetProps;
import org.veupathdb.service.eda.access.model.EndUserRow;
import org.veupathdb.service.eda.access.model.SearchQuery;
import org.veupathdb.service.eda.access.service.dataset.DatasetRepo;
import org.veupathdb.service.eda.access.service.user.EndUserPatchService;
import org.veupathdb.service.eda.access.service.user.EndUserRepo;
import org.veupathdb.service.eda.access.util.Keys;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.List;

@AdminRequired
public class ApproveEligibleStudiesController implements ApproveEligibleAccessRequests {

  // any approvals will be recorded as made by this user ID
  private static final long SERVICE_USER_ID = 1926010L;

  private static final DateTimeFormatter APPROVAL_REASON_FORMAT = new DateTimeFormatterBuilder()
    .appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
    .appendPattern(", ")
    .appendValue(ChronoField.DAY_OF_MONTH)
    .appendPattern(" ")
    .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.SHORT)
    .appendPattern(" ")
    .appendValue(ChronoField.YEAR)
    .appendPattern(" hh:mm:ss zzz")
    .toFormatter();

  private static final Logger LOG = LogManager.getLogger(ApproveEligibleStudiesController.class);

  @Override
  public PostApproveEligibleAccessRequestsResponse postApproveEligibleAccessRequests() {
    try {
      // Only protected studies can be auto-approved.
      final List<DatasetProps> eligibleDatasets = DatasetRepo.Select.getInstance().datasetProps().stream()
        .filter(props -> props.accessLevel == DatasetAccessLevel.PROTECTED).toList();
      LOG.info("Found {} datasets at access level PROTECTED", eligibleDatasets.size());
      eligibleDatasets.stream()
        .filter(ds -> ds.durationForApproval != null)
        .forEach(dataset -> {
          // Fetch all requests for given dataset.
          final var query = new SearchQuery()
            .setDatasetId(dataset.datasetId)
            .setApprovalStatus(ApprovalStatus.REQUESTED);
          try {
            LOG.info("Querying end users with dataset ID {}.", dataset.datasetId);
            // Join dataset props from eda database to approval requests in accountdb.
            final List<EndUserRow> endUsers = EndUserRepo.Select.find(query);
            endUsers.stream()
              .filter(user -> {
                LOG.info("Duration {} passed since candidate request {} by {}: ",
                  Duration.between(user.getStartDate().toInstant(), Instant.now()), user.getEndUserID(), user.getUserId());
                return Duration.between(user.getStartDate().toInstant(), Instant.now()).compareTo(dataset.durationForApproval) >= 0;
              })
              .forEach(this::approveRequestAsUser);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
      return PostApproveEligibleAccessRequestsResponse.respond204();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void approveRequestAsUser(EndUserRow endUser) {
    LOG.info("Approving request for {}", endUser.getEndUserID());
    final EndUserPatch statusPatch = new EndUserPatchImpl();
    statusPatch.setOp(EndUserPatch.OpType.REPLACE);
    statusPatch.setPath("/" + Keys.Json.KEY_APPROVAL_STATUS);
    statusPatch.setValue(org.veupathdb.service.eda.generated.model.ApprovalStatus.APPROVED.getValue());

    final EndUserPatch notePatch = new EndUserPatchImpl();
    notePatch.setOp(EndUserPatch.OpType.REPLACE);

    // Note that this is set as a "denial reason" even though the request is approved. We mis-use the denial reason as
    // a general "state change reason" in the UI.
    notePatch.setPath("/" + Keys.Json.KEY_DENIAL_REASON);

    // This DateTimeFormat is used for consistency with values passed by client.
    notePatch.setValue(ZonedDateTime.now(ZoneId.of("GMT")).format(APPROVAL_REASON_FORMAT) + ": System approved request.");

    // Handles sending the e-mail and updating history.
    EndUserPatchService.modPatch(endUser, List.of(statusPatch, notePatch), SERVICE_USER_ID);
  }
}

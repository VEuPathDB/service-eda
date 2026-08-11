package org.veupathdb.service.eda.download;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.MapBuilder;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.lib.container.jaxrs.server.middleware.CustomResponseHeadersFilter;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.auth.StudyAccess;
import org.veupathdb.service.eda.common.client.DatasetAccessClient;
import org.veupathdb.service.eda.common.client.DatasetAccessClient.StudyDatasetInfo;
import org.veupathdb.service.eda.generated.model.FileContentResponseStream;
import org.veupathdb.service.eda.generated.resources.Download;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

@Authenticated(allowGuests = true)
public class Service implements Download {

  private static final Logger LOG = LogManager.getLogger(Service.class);

  @Context
  ContainerRequest _request;

  @Override
  public GetDownloadByProjectAndStudyIdResponse getDownloadByProjectAndStudyId(String project, String studyId) {
    LOG.info("Get releases by {} and {}", project, studyId);
    StudyDatasetInfo datasetInfo = checkPermsAndFetchDatasetInfo(studyId, StudyAccess::allowStudyMetadata);
    return GetDownloadByProjectAndStudyIdResponse.respond200WithApplicationJson(
      new FileStore(Resources.getDatasetsParentDir(project))
        .getReleaseNames(datasetInfo.getSha1Hash())
        .orElseThrow(NotFoundException::new));
  }

  @Override
  public GetDownloadByProjectAndStudyIdAndReleaseResponse getDownloadByProjectAndStudyIdAndRelease(
    String project,
    String studyId,
    String release
  ) {
    LOG.info("Get files by {} and {} and {}", project, studyId, release);
    StudyDatasetInfo datasetInfo = checkPermsAndFetchDatasetInfo(studyId, StudyAccess::allowStudyMetadata);
    return GetDownloadByProjectAndStudyIdAndReleaseResponse.respond200WithApplicationJson(
      new FileStore(Resources.getDatasetsParentDir(project))
        .getFiles(datasetInfo.getSha1Hash(), release)
        .orElseThrow(NotFoundException::new));
  }

  @Override
  public GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse getDownloadByProjectAndStudyIdAndReleaseAndFile(
    String project,
    String studyId,
    String release,
    String fileName
  ) {
    LOG.info("Get file content at {} and {} and {} and {}", project, studyId, release, fileName);
    StudyDatasetInfo datasetInfo = checkPermsAndFetchDatasetInfo(studyId, StudyAccess::allowResultsAll);
    Path filePath = new FileStore(Resources.getDatasetsParentDir(project))
      .getFilePath(datasetInfo.getSha1Hash(), release, fileName)
      .orElseThrow(() -> {
        LOG.info(
          "Unable to find a file with hash {}, release {}, and name {}.",
          datasetInfo.getSha1Hash(),
          release,
          fileName
        );
        return new NotFoundException();
      });
    String dispositionHeaderValue = "attachment; filename=\"" + fileName + "\"";
    Optional<String> userId = UserProvider.lookupUser(_request)
      .map(user -> Long.toString(user.getUserId()));
    ServiceMetrics.reportDownloadCount(datasetInfo.getDatasetId(), userId.orElse("None"), fileName);
    _request.setProperty(
      CustomResponseHeadersFilter.CUSTOM_HEADERS_KEY,
      new MapBuilder<>(HttpHeaders.CONTENT_DISPOSITION, dispositionHeaderValue).toMap()
    );
    return GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse.respond200WithTextPlain(
      new FileContentResponseStream(cSwallow(
        out -> IoUtil.transferStream(out, Files.newInputStream(filePath)))));
  }

  private StudyDatasetInfo checkPermsAndFetchDatasetInfo(String studyId, Function<StudyAccess, Boolean> accessGranter) {
    try {
      var parsed = org.veupathdb.service.eda.common.model.StudyId.parse(studyId);
      var user   = UserProvider.lookupUser(_request).orElseThrow();

      if (parsed.isUnion()) {
        // Raw file-bundle download of a union meta-study is not a v1 deliverable.
        // Never authorize the literal union token as a single study: enforce the
        // access check on every member first, then reject the download itself.
        parsed.getMembers().forEach(member -> checkPermsAndFetchDatasetInfoSingle(member, user.getUserId(), accessGranter));
        throw new jakarta.ws.rs.BadRequestException("union download not supported");
      }

      return checkPermsAndFetchDatasetInfoSingle(studyId, user.getUserId(), accessGranter);
    } catch (Exception e) {
      LOG.error("Unable to check study permissions and convert studyId to dataset hash", e);
      throw e;
    }
  }

  private StudyDatasetInfo checkPermsAndFetchDatasetInfoSingle(String studyId, long userId, Function<StudyAccess, Boolean> accessGranter) {
    var study = DatasetAccessClient.getStudyDatasetInfoMapForUser(userId)
      .get(studyId);

    if (study == null) {
      throw new NotFoundException("Study '" + studyId + "' cannot be found [dataset access service].");
    }

    if (!accessGranter.apply(study.getStudyAccess())) {
      throw new ForbiddenException("Permission Denied");
    }

    return study;
  }
}

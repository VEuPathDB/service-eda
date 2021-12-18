package org.veupathdb.service.eda.common.auth;

import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import org.gusdb.fgputil.ImmutableEntry;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.client.DatasetAccessClient;

public class StudyAccess {

  private final boolean _allowStudyMetadata;
  private final boolean _allowSubsetting;
  private final boolean _allowVisualizations;
  private final boolean _allowResultsFirstPage;
  private final boolean _allowResultsAll;

  public StudyAccess(
      final boolean allowStudyMetadata,
      final boolean allowSubsetting,
      final boolean allowVisualizations,
      final boolean allowResultsFirstPage,
      final boolean allowResultsAll
  ) {
    _allowStudyMetadata = allowStudyMetadata;
    _allowSubsetting = allowSubsetting;
    _allowVisualizations = allowVisualizations;
    _allowResultsFirstPage = allowResultsFirstPage;
    _allowResultsAll = allowResultsAll;
  }

  public boolean allowStudyMetadata() { return _allowStudyMetadata; }
  public boolean allowSubsetting() { return _allowSubsetting; }
  public boolean allowVisualizations() { return _allowVisualizations; }
  public boolean allowResultsFirstPage() { return _allowResultsFirstPage; }
  public boolean allowResultsAll() { return _allowResultsAll; }

  public static Entry<String,String> readAuthHeader(ContainerRequestContext request, String authHeaderKey) {
    // read raw header value
    String authHeaderValue = request.getHeaderString(authHeaderKey);
    if (authHeaderValue.isBlank()) {
      // this should not happen; should be covered by the @Authorization annotation
      throw new NotAuthorizedException("Authorization is needed to access this resource.");
    }
    return new ImmutableEntry<>(authHeaderKey, authHeaderValue);
  }

  public static void confirmPermission(Entry<String,String> authHeader,
      String dataAccessServiceUrl, String studyId, Predicate<StudyAccess> accessPredicate) {
    // check with dataset access service to see if attached auth header has permission to access
    if (!accessPredicate.test(new DatasetAccessClient(dataAccessServiceUrl, authHeader).getStudyAccess(studyId))) {
      throw new ForbiddenException();
    }
  }

  public JSONObject toJson() {
    return new JSONObject()
      .put("allowStudyMetadata", _allowStudyMetadata)
      .put("allowSubsetting", _allowSubsetting)
      .put("allowVisualizations", _allowVisualizations)
      .put("allowResultsFirstPage", _allowResultsFirstPage)
      .put("allowResultsAll", _allowResultsAll);
  }
}

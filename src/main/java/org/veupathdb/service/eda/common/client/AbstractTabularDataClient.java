package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public abstract class AbstractTabularDataClient {

  private static Logger LOG = LogManager.getLogger(AbstractTabularDataClient.class);

  private final String _serviceBaseUrl;

  public abstract StreamSpecValidator getStreamSpecValidator();

  public abstract String varToColumnHeader(VariableSpec var);

  public abstract InputStream getTabularDataStream(
      ReferenceMetadata metadata,
      List<APIFilter> subset,
      StreamSpec spec);

  public AbstractTabularDataClient(String serviceBaseUrl) {
    // remove trailing slash from baseUrl (paths must begin with a slash)
    _serviceBaseUrl = !serviceBaseUrl.endsWith("/") ? serviceBaseUrl :
        serviceBaseUrl.substring(0, serviceBaseUrl.length() - 1);
  }

  protected String getUrl(String urlPath) {
    return _serviceBaseUrl + (urlPath.startsWith("/") ? urlPath : urlPath.substring(1));
  }

}

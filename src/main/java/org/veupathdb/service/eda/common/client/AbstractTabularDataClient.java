package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.AutoCloseableList;
import org.gusdb.fgputil.functional.FunctionalInterfaces;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import static org.gusdb.fgputil.functional.Functions.cSwallow;

public abstract class AbstractTabularDataClient {

  private static Logger LOG = LogManager.getLogger(AbstractTabularDataClient.class);

  private final String _serviceBaseUrl;

  public abstract ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata);

  public abstract String varToColumnHeader(VariableSpec var);

  public AbstractTabularDataClient(String serviceBaseUrl) {
    // remove trailing slash from baseUrl (paths must begin with a slash)
    _serviceBaseUrl = !serviceBaseUrl.endsWith("/") ? serviceBaseUrl :
        serviceBaseUrl.substring(0, serviceBaseUrl.length() - 1);
  }

  protected static void checkUniqueNames(Collection<StreamSpec> streams, ValidationBundle.ValidationBundleBuilder validation) {
    Set<String> specNames = streams.stream().map(StreamSpec::getStreamName).collect(Collectors.toSet());
      if (specNames.size() != streams.size()) {
        validation.addError("Stream specs must not duplicate names.");
      }
  }

  protected String getUrl(String urlPath) {
    return _serviceBaseUrl + (urlPath.startsWith("/") ? urlPath : urlPath.substring(1));
  }

}

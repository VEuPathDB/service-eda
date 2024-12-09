package org.veupathdb.service.eda.merge.core.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.client.EdaSubsettingClient;
import org.veupathdb.service.eda.generated.model.DerivedVariableBulkMetadataRequest;
import org.veupathdb.service.eda.merge.core.RequestResourcesBase;

import java.util.Map.Entry;

/**
 * Processes and validates incoming request data for tabular and derived
 * variable metadata requests.  This class is responsible for providing a
 * ReferenceMetadata which includes derived variables generated from the passed
 * derived variable specs.  It also provides a DerivedVariableFactory from which
 * plugin instances are delivered.
 * <p>
 * Note the superclass MergedTabularRequestResources, which supplements this
 * class with target entity, subset filters, and compute information needed for
 * tabular requests.
 */
public class RequestResources extends RequestResourcesBase {

  private static final Logger LOG = LogManager.getLogger(RequestResources.class);

  protected final EdaSubsettingClient _subsetSvc;

  public RequestResources(DerivedVariableBulkMetadataRequest request, Entry<String, String> authHeader) throws ValidationException {
    super(request);

    // create subsetting service client
    _subsetSvc = new EdaSubsettingClient(Resources.SUBSETTING_SERVICE_URL, authHeader);
  }
}

package org.veupathdb.service.eda.common.client;

import java.util.Collection;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;

public class EdaMergingSpecValidator implements StreamSpecValidator {

  @Override
  public ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata) {
    // FIXME: currently do not support derived vars
    // TODO: don't forget to check for unique stream names
    return new EdaSubsettingSpecValidator().validateStreamSpecs(streamSpecs, metadata);
  }
}

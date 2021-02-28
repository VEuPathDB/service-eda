package org.veupathdb.service.eda.common.client;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationBundle.ValidationBundleBuilder;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;

public interface StreamSpecValidator {

  ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata);

  default void checkUniqueNames(Collection<StreamSpec> streamSpecs, ValidationBundleBuilder validation) {
    Set<String> specNames = streamSpecs.stream().map(StreamSpec::getStreamName).collect(Collectors.toSet());
    if (specNames.size() != streamSpecs.size()) {
      validation.addError("Stream specs must not duplicate names.");
    }
  }
}

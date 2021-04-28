package org.veupathdb.service.eda.common.client.spec;

import java.util.Collection;
import java.util.List;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationLevel;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EdaMergingSpecValidator implements StreamSpecValidator {

  @Override
  public ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata) {
    ValidationBundle.ValidationBundleBuilder validation = ValidationBundle.builder(ValidationLevel.RUNNABLE);
    checkUniqueNames(streamSpecs, validation);

    // TODO: validate merge service requests!

    return validation.build();
  }
}

package org.veupathdb.service.eda.common.client.spec;

import java.util.Collection;
import java.util.Optional;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationBundle.ValidationBundleBuilder;
import org.gusdb.fgputil.validation.ValidationLevel;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EdaSubsettingSpecValidator implements StreamSpecValidator {

  @Override
  public ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata) {
    ValidationBundleBuilder validation = ValidationBundle.builder(ValidationLevel.RUNNABLE);
    checkUniqueNames(streamSpecs, validation);
    for (StreamSpec streamSpec : streamSpecs) {
      if (metadata.getEntity(streamSpec.getEntityId()).isEmpty()) {
        validation.addError(streamSpec.getStreamName(), "Entity '" +
            streamSpec.getEntityId() + "' does not exist in study '" + metadata.getStudyId());
        continue;
      }
      for (VariableSpec varSpec : streamSpec) {
        Optional<VariableDef> varDef = metadata.getVariable(varSpec);
        if (varDef.isEmpty() || !varDef.get().getEntityId().equals(streamSpec.getEntityId())) {
          validation.addError(streamSpec.getStreamName(),
              "Bad spec for subsetting request.  Variable '" + JsonUtil.serializeObject(varSpec) +
              "' must be a member of entity '" + streamSpec.getEntityId() + "'.");
        }
        else if (!varDef.get().getSource().equals(VariableSource.NATIVE)) {
          validation.addError(streamSpec.getStreamName(),
              "Bad spec for subsetting request.  Variable '" + varSpec.getVariableId() +
              "' must be a native variable of entity '" + streamSpec.getEntityId() + "'.");
        }
      }
    }
    return validation.build();
  }
}

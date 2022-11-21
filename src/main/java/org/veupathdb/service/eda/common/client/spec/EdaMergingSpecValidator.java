package org.veupathdb.service.eda.common.client.spec;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationLevel;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EdaMergingSpecValidator implements StreamSpecValidator {

  @Override
  public ValidationBundle validateStreamSpecs(Collection<StreamSpec> streamSpecs, ReferenceMetadata metadata) {
    ValidationBundle.ValidationBundleBuilder validation = ValidationBundle.builder(ValidationLevel.RUNNABLE);
    checkUniqueNames(streamSpecs, validation);
    for (StreamSpec spec : streamSpecs) {

      // validate requested entity ID
      Optional<EntityDef> entityOpt = metadata.getEntity(spec.getEntityId());
      if (entityOpt.isEmpty()) {
        validation.addError(spec.getStreamName(), "No entity exists in study " +
            metadata.getStudyId() + " with ID " + spec.getEntityId());
        continue;
      }
      EntityDef requestedEntity = entityOpt.get();

      // each requested variable must be a member of the entity or one of its ancestors
      List<String> ancestorIds = metadata.getAncestors(requestedEntity)
          .stream().map(EntityDef::getId).collect(Collectors.toList());
      for (VariableSpec varSpec : spec) {
        Optional<VariableDef> varOpt = metadata.getVariable(varSpec);
        if (varOpt.isEmpty()) {
          validation.addError(spec.getStreamName(), "No variable exists in study " +
              metadata.getStudyId() + " with spec " + VariableDef.toDotNotation(varSpec));
        }
        else {
          // variable exists; make sure its entity is the requested entity or an ancestor
          String varEntityId = varOpt.get().getEntityId();
          if (!varEntityId.equals(requestedEntity.getId()) && !ancestorIds.contains(varEntityId)) {
            validation.addError(spec.getStreamName(), "Requested variable '" + VariableDef.toDotNotation(varOpt.get()) +
                "' must be a member of the requested entity '" + requestedEntity.getId() +
                "' or one of its ancestors: [" + ancestorIds.stream().collect(Collectors.joining(", ")) + "]");
          }
        }
      }
    }

    // TODO: validate merge service requests MORE?

    // TODO: figure out if validating requests that include computed var columns is feasible/necessary/desirable
    //          answer: yes, we should validate that computed var is the same as or an ancestor of the target entity,
    //            but can't currently do that with this API so will check in merge service

    return validation.build();
  }
}

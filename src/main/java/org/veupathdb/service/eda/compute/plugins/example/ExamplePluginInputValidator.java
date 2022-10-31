package org.veupathdb.service.eda.compute.plugins.example;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.compute.plugins.PluginConfigValidator;
import org.veupathdb.service.eda.generated.model.ExampleComputeConfig;
import org.veupathdb.service.eda.generated.model.ExamplePluginRequest;

import java.util.function.Supplier;

public class ExamplePluginInputValidator implements PluginConfigValidator<ExamplePluginRequest> {

  @Override
  public void validate(ExamplePluginRequest request, Supplier<ReferenceMetadata> referenceMetadata) {
    ReferenceMetadata meta = referenceMetadata.get();
    ExampleComputeConfig config = request.getConfig();

    // check entity
    EntityDef entity = meta.getEntity(config.getOutputEntityId())
        .orElseThrow(() -> new BadRequestException("Invalid entity ID : " + config.getOutputEntityId()));

    // check variable
    VariableDef variable = entity.getVariable(config.getInputVariable())
        .orElseThrow(() -> new BadRequestException("Invalid variable spec for output entity"));

    // check that variable is native to entity
    if (!variable.getSource().isNativeOrId()) {
      throw new BadRequestException("This plugin only accepts variables native to the output entity.");
    }

    // check that suffix is present
    if (config.getValueSuffix() == null || config.getValueSuffix().contains("\t")) {
      throw new BadRequestException("valueSuffix is required and cannot contain tabs");
    }
  }

}

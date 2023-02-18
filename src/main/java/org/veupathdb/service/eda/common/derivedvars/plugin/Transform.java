package org.veupathdb.service.eda.common.derivedvars.plugin;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.PluginMap;
import org.veupathdb.service.eda.common.derivedvars.plugin.transforms.Concatenation;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.pluginsOf;

public abstract class Transform<T> extends AbstractDerivedVariable<T> {

  public static PluginMap<Transform> getPlugins() {
    return pluginsOf(Transform.class,
      // available transforms
      Concatenation.class
    );
  }

  public abstract String getValue(Map<String,String> row);

  @Override
  public void validateDependedVariables() {
    // find the ancestors of the entity this var is declared on; dependant vars must live on the same entity as this var or an ancestor
    List<String> ancestorIds = _metadata.getAncestors(getEntity()).stream().map(EntityDef::getId).toList();
    for (VariableSpec spec : getRequiredInputVars()) {
      _metadata.getVariable(spec).orElseThrow(() ->
          new BadRequestException("Input variable for reduction derived var " + getFunctionName() + " does not exist."));
      if (!spec.getEntityId().equals(getEntity().getId()) && !ancestorIds.contains(spec.getEntityId())) {
        throw new BadRequestException("Transform derived vars can only use input variables on the same entity on which they are declared or on an ancestor.");
      }
    }
  }
}

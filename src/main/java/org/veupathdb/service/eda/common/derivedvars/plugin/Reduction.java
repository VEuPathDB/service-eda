package org.veupathdb.service.eda.common.derivedvars.plugin;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.PluginMap;
import org.veupathdb.service.eda.common.derivedvars.plugin.reductions.Mean;
import org.veupathdb.service.eda.common.derivedvars.plugin.reductions.Sum;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.pluginsOf;

public abstract class Reduction<T> extends AbstractDerivedVariable<T> {

  public static PluginMap<Reduction> getPlugins() {
    return pluginsOf(Reduction.class,
      // available reductions
      Sum.class,
      Mean.class
    );
  }

  public abstract void addRow(Map<String,String> nextRow);

  public abstract String getResultingValue();

  @Override
  public void validateDependedVariables() {
    // depended vars must all be on the same entity, which must be a descendant of this entity
    String commonEntityId = null;
    for (VariableSpec spec : getRequiredInputVars()) {
      _metadata.getVariable(spec).orElseThrow(() ->
          new BadRequestException("Input variable for reduction derived var " + getFunctionName() + " does not exist."));
      if (commonEntityId == null) {
        commonEntityId = spec.getEntityId();
      }
      else {
        if (!spec.getEntityId().equals(commonEntityId)) {
          throw new BadRequestException("Not all input variables for reduction derived var " + getFunctionName() + " come from the same entity.");
        }
      }
    }
    if (commonEntityId != null) {
      // find the ancestors of the common entity of the input vars, which should be a descendant of the target entity
      List<String> ancestorIds = _metadata.getAncestors(_metadata.getEntity(commonEntityId).orElseThrow()).stream().map(EntityDef::getId).toList();
      if (!ancestorIds.contains(getEntity().getId())) {
        throw new BadRequestException("Input vars configured for reduction derived var " + getFunctionName() + " are not on a descendant entity.");
      }
    }
  }
}

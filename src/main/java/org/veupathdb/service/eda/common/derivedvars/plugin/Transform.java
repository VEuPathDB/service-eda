package org.veupathdb.service.eda.common.derivedvars.plugin;

import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.PluginMap;
import org.veupathdb.service.eda.common.derivedvars.plugin.transforms.Concatenation;

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
    // TODO: fill in; depended vars must be on this entity or an ancestor, but
    //   note if checking against reference metadata, we get the ancestor check for free
  }
}

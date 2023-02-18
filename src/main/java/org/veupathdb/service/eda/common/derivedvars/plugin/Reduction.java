package org.veupathdb.service.eda.common.derivedvars.plugin;

import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory.PluginMap;
import org.veupathdb.service.eda.common.derivedvars.plugin.reductions.Mean;
import org.veupathdb.service.eda.common.derivedvars.plugin.reductions.Sum;

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
    // TODO: fill in; depended vars must all be on the same entity, which
    //   must be a descendant of this entity
  }
}

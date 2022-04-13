package org.veupathdb.service.eda.ss.model.varcollection;

import java.util.List;
import org.veupathdb.service.eda.ss.model.distribution.NumberDistributionConfig;
import org.veupathdb.service.eda.ss.model.variable.FloatingPointVariable;
import org.veupathdb.service.eda.ss.model.variable.VariableWithValues;

public class FloatingPointVarCollection extends VarCollection {

  private final FloatingPointVariable.Properties _floatProps;
  private final NumberDistributionConfig<Double> _distributionConfig;

  public FloatingPointVarCollection(
      Properties collectionProps,
      FloatingPointVariable.Properties floatProps,
      NumberDistributionConfig<Double> distributionConfig) {
    super(collectionProps);
    _floatProps = floatProps;
    _distributionConfig = distributionConfig;
  }

  public String getUnits() {
    return _floatProps.units;
  }

  public Long getPrecision() {
    return _floatProps.precision;
  }

  public NumberDistributionConfig<Double> getDistributionConfig() {
    return _distributionConfig;
  }

  @Override
  protected void assignDistributionDefaults(List<VariableWithValues> memberVars) {
    Double maxBinSize = (double)0; // find the biggest size
    for (VariableWithValues var : memberVars) {
      // superclass promises to only pass the correct type here
      NumberDistributionConfig<Double> varConfig = ((FloatingPointVariable)var).getDistributionConfig();
      if (varConfig.getDefaultBinWidth() > maxBinSize) {
        maxBinSize = varConfig.getDefaultBinWidth();
      }
    }
    _distributionConfig.setBinWidth(maxBinSize);
  }
}

package org.veupathdb.service.eda.ss.model.varcollection;

import java.util.List;
import org.veupathdb.service.eda.ss.model.distribution.NumberDistributionConfig;
import org.veupathdb.service.eda.ss.model.variable.IntegerVariable;
import org.veupathdb.service.eda.ss.model.variable.VariableWithValues;

public class IntegerVarCollection extends VarCollection {

  private final IntegerVariable.Properties _integerProps;
  private final NumberDistributionConfig<Long> _distributionConfig;

  public IntegerVarCollection(
      Properties collectionProperties,
      IntegerVariable.Properties integerProps,
      NumberDistributionConfig<Long> distributionConfig) {
    super(collectionProperties);
    _integerProps = integerProps;
    _distributionConfig = distributionConfig;
  }

  public String getUnits() {
    return _integerProps.units;
  }

  public NumberDistributionConfig<Long> getDistributionConfig() {
    return _distributionConfig;
  }

  @Override
  protected void assignDistributionDefaults(List<VariableWithValues> memberVars) {
    long maxBinSize = 0L; // find the biggest size
    for (VariableWithValues var : memberVars) {
      // superclass promises to only pass the correct type here
      NumberDistributionConfig<Long> varConfig = ((IntegerVariable)var).getDistributionConfig();
      if (varConfig.getDefaultBinWidth() > maxBinSize) {
        maxBinSize = varConfig.getDefaultBinWidth();
      }
    }
    _distributionConfig.setBinWidth(maxBinSize);

  }
}

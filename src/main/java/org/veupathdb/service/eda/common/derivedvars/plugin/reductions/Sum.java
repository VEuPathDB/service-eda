package org.veupathdb.service.eda.common.derivedvars.plugin.reductions;

import java.util.Map;

public class Sum extends SingleNumericVarReduction {

  private Double _sum = 0D;

  @Override
  public String getFunctionName() {
    return "sum";
  }

  @Override
  public void addRow(Map<String, String> nextRow) {
    _sum += Double.parseDouble(nextRow.get(_inputColumnName));
  }

  @Override
  public String getResultingValue() {
    return String.valueOf(_sum);
  }
}

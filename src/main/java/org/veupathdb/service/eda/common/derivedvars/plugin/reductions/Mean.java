package org.veupathdb.service.eda.common.derivedvars.plugin.reductions;

import java.util.Map;

public class Mean extends SingleNumericVarReduction {

  @Override
  public String getFunctionName() {
    return "mean";
  }

  @Override
  public Reducer createReducer() {
    return new Reducer() {

      private int _numRows = 0;
      private double _sum = 0D;

      @Override
      public void addRow(Map<String, String> nextRow) {
        _numRows++;
        _sum += Double.parseDouble(nextRow.get(_inputColumnName));
      }

      @Override
      public String getResultingValue() {
        return String.valueOf(_sum / _numRows);
      }
    };
  }

}

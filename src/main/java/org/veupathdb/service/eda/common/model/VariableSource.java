package org.veupathdb.service.eda.common.model;

public enum VariableSource {
  NATIVE,
  INHERITED,
  DERIVED_BY_REDUCTION,
  DERIVED_BY_TRANSFORM;

  public boolean isResident() {
    return !equals(INHERITED);
  }
}

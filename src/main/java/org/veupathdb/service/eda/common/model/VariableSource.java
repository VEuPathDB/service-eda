package org.veupathdb.service.eda.common.model;

public enum VariableSource {
  ID,
  NATIVE,
  INHERITED,
  DERIVED,
  COMPUTED;

  public boolean isNativeOrId() {
    return equals(ID) || equals(NATIVE);
  }
  public boolean isResident() {
    return !equals(INHERITED);
  }
}

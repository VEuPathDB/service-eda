package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(
    using = DynamicDataSpec.DynamicDataSpecDeserializer.class
)
@JsonSerialize(
    using = DynamicDataSpec.Serializer.class
)
public class DynamicDataSpecImpl implements DynamicDataSpec {
  private Object anyType;

  private DynamicDataSpecImpl() {
    this.anyType = null;
  }

  public DynamicDataSpecImpl(CollectionSpec collectionSpec) {
    this.anyType = collectionSpec;
  }

  public DynamicDataSpecImpl(VariableSpec variableSpec) {
    this.anyType = variableSpec;
  }

  public CollectionSpec getCollectionSpec() {
    if ( !(anyType instanceof  CollectionSpec)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.CollectionSpec");
    return (CollectionSpec) anyType;
  }

  public Boolean isCollectionSpec() {
    return anyType instanceof CollectionSpec;
  }

  public VariableSpec getVariableSpec() {
    if ( !(anyType instanceof  VariableSpec)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.VariableSpec");
    return (VariableSpec) anyType;
  }

  public Boolean isVariableSpec() {
    return anyType instanceof VariableSpec;
  }
}

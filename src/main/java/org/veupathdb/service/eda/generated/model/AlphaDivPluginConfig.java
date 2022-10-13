package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = AlphaDivPluginConfigImpl.class
)
public interface AlphaDivPluginConfig {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("alphaDivMethod")
  AlphaDivMethod getAlphaDivMethod();

  @JsonProperty("alphaDivMethod")
  void setAlphaDivMethod(AlphaDivMethod alphaDivMethod);
}

package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ExampleComputeVizSpecImpl.class
)
public interface ExampleComputeVizSpec {
  @JsonProperty("prefixVar")
  VariableSpec getPrefixVar();

  @JsonProperty("prefixVar")
  void setPrefixVar(VariableSpec prefixVar);
}

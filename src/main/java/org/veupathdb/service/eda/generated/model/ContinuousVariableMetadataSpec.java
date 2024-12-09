package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ContinuousVariableMetadataSpecImpl.class
)
public interface ContinuousVariableMetadataSpec {
  @JsonProperty("variable")
  VariableSpec getVariable();

  @JsonProperty("variable")
  void setVariable(VariableSpec variable);

  @JsonProperty("metadata")
  List<String> getMetadata();

  @JsonProperty("metadata")
  void setMetadata(List<String> metadata);
}

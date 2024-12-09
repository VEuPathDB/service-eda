package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = TableSpecImpl.class
)
public interface TableSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("outputVariable")
  List<VariableSpec> getOutputVariable();

  @JsonProperty("outputVariable")
  void setOutputVariable(List<VariableSpec> outputVariable);

  @JsonProperty("pagingConfig")
  APIPagingConfig getPagingConfig();

  @JsonProperty("pagingConfig")
  void setPagingConfig(APIPagingConfig pagingConfig);
}

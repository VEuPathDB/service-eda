package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "outputVariable",
    "pagingConfig"
})
public class TableSpecImpl implements TableSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("outputVariable")
  private List<VariableSpec> outputVariable;

  @JsonProperty("pagingConfig")
  private APIPagingConfig pagingConfig;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("outputVariable")
  public List<VariableSpec> getOutputVariable() {
    return this.outputVariable;
  }

  @JsonProperty("outputVariable")
  public void setOutputVariable(List<VariableSpec> outputVariable) {
    this.outputVariable = outputVariable;
  }

  @JsonProperty("pagingConfig")
  public APIPagingConfig getPagingConfig() {
    return this.pagingConfig;
  }

  @JsonProperty("pagingConfig")
  public void setPagingConfig(APIPagingConfig pagingConfig) {
    this.pagingConfig = pagingConfig;
  }
}

package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "collectionVariable",
    "alphaDivMethod"
})
public class AlphaDivComputeConfigImpl implements AlphaDivComputeConfig {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("alphaDivMethod")
  private AlphaDivMethod alphaDivMethod;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("collectionVariable")
  public VariableSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(VariableSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("alphaDivMethod")
  public AlphaDivMethod getAlphaDivMethod() {
    return this.alphaDivMethod;
  }

  @JsonProperty("alphaDivMethod")
  public void setAlphaDivMethod(AlphaDivMethod alphaDivMethod) {
    this.alphaDivMethod = alphaDivMethod;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}

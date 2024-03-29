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
    "dataType",
    "collectionSpec"
})
public class CorrelationInputDataImpl implements CorrelationInputData {
  @JsonProperty("dataType")
  private CorrelationInputDataTypes dataType;

  @JsonProperty("collectionSpec")
  private CollectionSpec collectionSpec;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("dataType")
  public CorrelationInputDataTypes getDataType() {
    return this.dataType;
  }

  @JsonProperty("dataType")
  public void setDataType(CorrelationInputDataTypes dataType) {
    this.dataType = dataType;
  }

  @JsonProperty("collectionSpec")
  public CollectionSpec getCollectionSpec() {
    return this.collectionSpec;
  }

  @JsonProperty("collectionSpec")
  public void setCollectionSpec(CollectionSpec collectionSpec) {
    this.collectionSpec = collectionSpec;
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

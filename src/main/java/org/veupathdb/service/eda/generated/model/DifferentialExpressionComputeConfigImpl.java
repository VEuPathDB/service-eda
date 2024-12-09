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
    "collectionVariable",
    "comparator",
    "differentialExpressionMethod",
    "pValueFloor"
})
public class DifferentialExpressionComputeConfigImpl implements DifferentialExpressionComputeConfig {
  @JsonProperty("collectionVariable")
  private CollectionSpec collectionVariable;

  @JsonProperty("comparator")
  private ComparatorSpec comparator;

  @JsonProperty("differentialExpressionMethod")
  private DifferentialExpressionMethod differentialExpressionMethod;

  @JsonProperty("pValueFloor")
  private String pValueFloor;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("collectionVariable")
  public CollectionSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(CollectionSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("comparator")
  public ComparatorSpec getComparator() {
    return this.comparator;
  }

  @JsonProperty("comparator")
  public void setComparator(ComparatorSpec comparator) {
    this.comparator = comparator;
  }

  @JsonProperty("differentialExpressionMethod")
  public DifferentialExpressionMethod getDifferentialExpressionMethod() {
    return this.differentialExpressionMethod;
  }

  @JsonProperty("differentialExpressionMethod")
  public void setDifferentialExpressionMethod(
      DifferentialExpressionMethod differentialExpressionMethod) {
    this.differentialExpressionMethod = differentialExpressionMethod;
  }

  @JsonProperty("pValueFloor")
  public String getPValueFloor() {
    return this.pValueFloor;
  }

  @JsonProperty("pValueFloor")
  public void setPValueFloor(String pValueFloor) {
    this.pValueFloor = pValueFloor;
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

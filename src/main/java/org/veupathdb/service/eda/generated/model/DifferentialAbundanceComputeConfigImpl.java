package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "collectionVariable",
    "comparisonVariable",
    "differentialAbundanceGroupA",
    "differentialAbundanceGroupB",
    "differentialAbundanceMethod"
})
public class DifferentialAbundanceComputeConfigImpl implements DifferentialAbundanceComputeConfig {
  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("comparisonVariable")
  private VariableSpec comparisonVariable;

  @JsonProperty("differentialAbundanceGroupA")
  private List<String> differentialAbundanceGroupA;

  @JsonProperty("differentialAbundanceGroupB")
  private List<String> differentialAbundanceGroupB;

  @JsonProperty("differentialAbundanceMethod")
  private DifferentialAbundanceMethod differentialAbundanceMethod;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("collectionVariable")
  public VariableSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(VariableSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("comparisonVariable")
  public VariableSpec getComparisonVariable() {
    return this.comparisonVariable;
  }

  @JsonProperty("comparisonVariable")
  public void setComparisonVariable(VariableSpec comparisonVariable) {
    this.comparisonVariable = comparisonVariable;
  }

  @JsonProperty("differentialAbundanceGroupA")
  public List<String> getDifferentialAbundanceGroupA() {
    return this.differentialAbundanceGroupA;
  }

  @JsonProperty("differentialAbundanceGroupA")
  public void setDifferentialAbundanceGroupA(List<String> differentialAbundanceGroupA) {
    this.differentialAbundanceGroupA = differentialAbundanceGroupA;
  }

  @JsonProperty("differentialAbundanceGroupB")
  public List<String> getDifferentialAbundanceGroupB() {
    return this.differentialAbundanceGroupB;
  }

  @JsonProperty("differentialAbundanceGroupB")
  public void setDifferentialAbundanceGroupB(List<String> differentialAbundanceGroupB) {
    this.differentialAbundanceGroupB = differentialAbundanceGroupB;
  }

  @JsonProperty("differentialAbundanceMethod")
  public DifferentialAbundanceMethod getDifferentialAbundanceMethod() {
    return this.differentialAbundanceMethod;
  }

  @JsonProperty("differentialAbundanceMethod")
  public void setDifferentialAbundanceMethod(
      DifferentialAbundanceMethod differentialAbundanceMethod) {
    this.differentialAbundanceMethod = differentialAbundanceMethod;
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

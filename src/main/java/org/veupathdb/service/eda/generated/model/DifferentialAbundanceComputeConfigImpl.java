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
    "differentialAbundanceMethod"
})
public class DifferentialAbundanceComputeConfigImpl implements DifferentialAbundanceComputeConfig {
  @JsonProperty("collectionVariable")
  private CollectionSpec collectionVariable;

  @JsonProperty("comparator")
  private ComparatorSpec comparator;

  @JsonProperty("differentialAbundanceMethod")
  private DifferentialAbundanceMethod differentialAbundanceMethod;

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

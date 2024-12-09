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
    "collection",
    "selectedMembers",
    "selectedValues"
})
public class CollectionOverlayConfigWithValuesImpl implements CollectionOverlayConfigWithValues {
  @JsonProperty("collection")
  private CollectionSpec collection;

  @JsonProperty("selectedMembers")
  private List<String> selectedMembers;

  @JsonProperty("selectedValues")
  private List<String> selectedValues;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("collection")
  public CollectionSpec getCollection() {
    return this.collection;
  }

  @JsonProperty("collection")
  public void setCollection(CollectionSpec collection) {
    this.collection = collection;
  }

  @JsonProperty("selectedMembers")
  public List<String> getSelectedMembers() {
    return this.selectedMembers;
  }

  @JsonProperty("selectedMembers")
  public void setSelectedMembers(List<String> selectedMembers) {
    this.selectedMembers = selectedMembers;
  }

  @JsonProperty("selectedValues")
  public List<String> getSelectedValues() {
    return this.selectedValues;
  }

  @JsonProperty("selectedValues")
  public void setSelectedValues(List<String> selectedValues) {
    this.selectedValues = selectedValues;
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

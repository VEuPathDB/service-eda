package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = CollectionOverlayConfigWithValuesImpl.class
)
public interface CollectionOverlayConfigWithValues extends CollectionOverlayConfig {
  @JsonProperty("collection")
  CollectionSpec getCollection();

  @JsonProperty("collection")
  void setCollection(CollectionSpec collection);

  @JsonProperty("selectedMembers")
  List<String> getSelectedMembers();

  @JsonProperty("selectedMembers")
  void setSelectedMembers(List<String> selectedMembers);

  @JsonProperty("selectedValues")
  List<String> getSelectedValues();

  @JsonProperty("selectedValues")
  void setSelectedValues(List<String> selectedValues);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}

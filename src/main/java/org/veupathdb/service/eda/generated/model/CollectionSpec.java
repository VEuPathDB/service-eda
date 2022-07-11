package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CollectionSpecImpl.class
)
public interface CollectionSpec {
  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("collectionId")
  String getCollectionId();

  @JsonProperty("collectionId")
  void setCollectionId(String collectionId);
}

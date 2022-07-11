package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "collectionId"
})
public class CollectionSpecImpl implements CollectionSpec {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("collectionId")
  private String collectionId;

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("collectionId")
  public String getCollectionId() {
    return this.collectionId;
  }

  @JsonProperty("collectionId")
  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }
}

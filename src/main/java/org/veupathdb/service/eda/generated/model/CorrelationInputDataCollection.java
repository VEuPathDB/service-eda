package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationInputDataCollectionImpl.class
)
public interface CorrelationInputDataCollection {
  @JsonProperty("dataType")
  DataTypeType getDataType();

  @JsonProperty("dataType")
  void setDataType(DataTypeType dataType);

  @JsonProperty("collectionSpec")
  CollectionSpec getCollectionSpec();

  @JsonProperty("collectionSpec")
  void setCollectionSpec(CollectionSpec collectionSpec);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);

  enum DataTypeType {
    @JsonProperty("collection")
    COLLECTION("collection");

    public final String value;

    public String getValue() {
      return this.value;
    }

    DataTypeType(String name) {
      this.value = name;
    }
  }
}

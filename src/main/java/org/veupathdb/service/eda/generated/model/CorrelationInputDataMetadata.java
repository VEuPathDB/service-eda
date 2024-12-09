package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationInputDataMetadataImpl.class
)
public interface CorrelationInputDataMetadata {
  @JsonProperty("dataType")
  DataTypeType getDataType();

  @JsonProperty("dataType")
  void setDataType(DataTypeType dataType);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);

  enum DataTypeType {
    @JsonProperty("metadata")
    METADATA("metadata");

    public final String value;

    public String getValue() {
      return this.value;
    }

    DataTypeType(String name) {
      this.value = name;
    }
  }
}

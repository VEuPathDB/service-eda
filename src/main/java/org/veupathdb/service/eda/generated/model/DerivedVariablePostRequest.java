package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DerivedVariablePostRequestImpl.class
)
public interface DerivedVariablePostRequest {
  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("functionName")
  String getFunctionName();

  @JsonProperty("functionName")
  void setFunctionName(String functionName);

  @JsonProperty("config")
  Object getConfig();

  @JsonProperty("config")
  void setConfig(Object config);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);
}

package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DerivedVariableGetResponseImpl.class
)
public interface DerivedVariableGetResponse extends DerivedVariableSpec {
  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("functionName")
  String getFunctionName();

  @JsonProperty("functionName")
  void setFunctionName(String functionName);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("config")
  Object getConfig();

  @JsonProperty("config")
  void setConfig(Object config);

  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("provenance")
  DerivedVariableProvenance getProvenance();

  @JsonProperty("provenance")
  void setProvenance(DerivedVariableProvenance provenance);
}

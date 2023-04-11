package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "variableId",
    "derivationType",
    "variableType",
    "dataShape",
    "vocabulary",
    "units",
    "dataRange"
})
public class DerivedVariableMetadataImpl implements DerivedVariableMetadata {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("derivationType")
  private DerivationType derivationType;

  @JsonProperty("variableType")
  private APIVariableType variableType;

  @JsonProperty("dataShape")
  private APIVariableDataShape dataShape;

  @JsonProperty("vocabulary")
  private List<String> vocabulary;

  @JsonProperty("units")
  private String units;

  @JsonProperty("dataRange")
  private Range dataRange;

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("derivationType")
  public DerivationType getDerivationType() {
    return this.derivationType;
  }

  @JsonProperty("derivationType")
  public void setDerivationType(DerivationType derivationType) {
    this.derivationType = derivationType;
  }

  @JsonProperty("variableType")
  public APIVariableType getVariableType() {
    return this.variableType;
  }

  @JsonProperty("variableType")
  public void setVariableType(APIVariableType variableType) {
    this.variableType = variableType;
  }

  @JsonProperty("dataShape")
  public APIVariableDataShape getDataShape() {
    return this.dataShape;
  }

  @JsonProperty("dataShape")
  public void setDataShape(APIVariableDataShape dataShape) {
    this.dataShape = dataShape;
  }

  @JsonProperty("vocabulary")
  public List<String> getVocabulary() {
    return this.vocabulary;
  }

  @JsonProperty("vocabulary")
  public void setVocabulary(List<String> vocabulary) {
    this.vocabulary = vocabulary;
  }

  @JsonProperty("units")
  public String getUnits() {
    return this.units;
  }

  @JsonProperty("units")
  public void setUnits(String units) {
    this.units = units;
  }

  @JsonProperty("dataRange")
  public Range getDataRange() {
    return this.dataRange;
  }

  @JsonProperty("dataRange")
  public void setDataRange(Range dataRange) {
    this.dataRange = dataRange;
  }
}

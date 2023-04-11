package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DerivedVariableMetadataImpl.class
)
public interface DerivedVariableMetadata extends VariableSpec {
  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("derivationType")
  DerivationType getDerivationType();

  @JsonProperty("derivationType")
  void setDerivationType(DerivationType derivationType);

  @JsonProperty("variableType")
  APIVariableType getVariableType();

  @JsonProperty("variableType")
  void setVariableType(APIVariableType variableType);

  @JsonProperty("dataShape")
  APIVariableDataShape getDataShape();

  @JsonProperty("dataShape")
  void setDataShape(APIVariableDataShape dataShape);

  @JsonProperty("vocabulary")
  List<String> getVocabulary();

  @JsonProperty("vocabulary")
  void setVocabulary(List<String> vocabulary);

  @JsonProperty("units")
  String getUnits();

  @JsonProperty("units")
  void setUnits(String units);

  @JsonProperty("dataRange")
  LabeledValueRange getDataRange();

  @JsonProperty("dataRange")
  void setDataRange(LabeledValueRange dataRange);
}

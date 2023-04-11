package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = VariableMappingImpl.class
)
public interface VariableMapping {
  @JsonProperty("variableClass")
  VariableClass getVariableClass();

  @JsonProperty("variableClass")
  void setVariableClass(VariableClass variableClass);

  @JsonProperty("variableSpec")
  VariableSpec getVariableSpec();

  @JsonProperty("variableSpec")
  void setVariableSpec(VariableSpec variableSpec);

  @JsonProperty("plotReference")
  PlotReferenceValue getPlotReference();

  @JsonProperty("plotReference")
  void setPlotReference(PlotReferenceValue plotReference);

  @JsonProperty("dataType")
  APIVariableType getDataType();

  @JsonProperty("dataType")
  void setDataType(APIVariableType dataType);

  @JsonProperty("dataShape")
  APIVariableDataShape getDataShape();

  @JsonProperty("dataShape")
  void setDataShape(APIVariableDataShape dataShape);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("displayRange")
  LabeledValueRange getDisplayRange();

  @JsonProperty("displayRange")
  void setDisplayRange(LabeledValueRange displayRange);

  @JsonProperty("vocabulary")
  List<String> getVocabulary();

  @JsonProperty("vocabulary")
  void setVocabulary(List<String> vocabulary);

  @JsonProperty("imputeZero")
  Boolean getImputeZero();

  @JsonProperty("imputeZero")
  void setImputeZero(Boolean imputeZero);

  @JsonProperty("isCollection")
  Boolean getIsCollection();

  @JsonProperty("isCollection")
  void setIsCollection(Boolean isCollection);

  @JsonProperty("members")
  List<VariableSpec> getMembers();

  @JsonProperty("members")
  void setMembers(List<VariableSpec> members);
}

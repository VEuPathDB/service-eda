package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "variableClass",
    "variableSpec",
    "plotReference",
    "dataType",
    "dataShape",
    "displayName",
    "displayRangeMin",
    "displayRangeMax",
    "vocabulary",
    "imputeZero",
    "isCollection",
    "members"
})
public class VariableMappingImpl implements VariableMapping {
  @JsonProperty("variableClass")
  private VariableClass variableClass;

  @JsonProperty("variableSpec")
  private VariableSpec variableSpec;

  @JsonProperty("plotReference")
  private PlotReferenceValue plotReference;

  @JsonProperty("dataType")
  private APIVariableType dataType;

  @JsonProperty("dataShape")
  private APIVariableDataShape dataShape;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("displayRangeMin")
  private Object displayRangeMin;

  @JsonProperty("displayRangeMax")
  private Object displayRangeMax;

  @JsonProperty("vocabulary")
  private List<String> vocabulary;

  @JsonProperty("imputeZero")
  private Boolean imputeZero;

  @JsonProperty("isCollection")
  private Boolean isCollection;

  @JsonProperty("members")
  private List<VariableSpec> members;

  @JsonProperty("variableClass")
  public VariableClass getVariableClass() {
    return this.variableClass;
  }

  @JsonProperty("variableClass")
  public void setVariableClass(VariableClass variableClass) {
    this.variableClass = variableClass;
  }

  @JsonProperty("variableSpec")
  public VariableSpec getVariableSpec() {
    return this.variableSpec;
  }

  @JsonProperty("variableSpec")
  public void setVariableSpec(VariableSpec variableSpec) {
    this.variableSpec = variableSpec;
  }

  @JsonProperty("plotReference")
  public PlotReferenceValue getPlotReference() {
    return this.plotReference;
  }

  @JsonProperty("plotReference")
  public void setPlotReference(PlotReferenceValue plotReference) {
    this.plotReference = plotReference;
  }

  @JsonProperty("dataType")
  public APIVariableType getDataType() {
    return this.dataType;
  }

  @JsonProperty("dataType")
  public void setDataType(APIVariableType dataType) {
    this.dataType = dataType;
  }

  @JsonProperty("dataShape")
  public APIVariableDataShape getDataShape() {
    return this.dataShape;
  }

  @JsonProperty("dataShape")
  public void setDataShape(APIVariableDataShape dataShape) {
    this.dataShape = dataShape;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("displayRangeMin")
  public Object getDisplayRangeMin() {
    return this.displayRangeMin;
  }

  @JsonProperty("displayRangeMin")
  public void setDisplayRangeMin(Object displayRangeMin) {
    this.displayRangeMin = displayRangeMin;
  }

  @JsonProperty("displayRangeMax")
  public Object getDisplayRangeMax() {
    return this.displayRangeMax;
  }

  @JsonProperty("displayRangeMax")
  public void setDisplayRangeMax(Object displayRangeMax) {
    this.displayRangeMax = displayRangeMax;
  }

  @JsonProperty("vocabulary")
  public List<String> getVocabulary() {
    return this.vocabulary;
  }

  @JsonProperty("vocabulary")
  public void setVocabulary(List<String> vocabulary) {
    this.vocabulary = vocabulary;
  }

  @JsonProperty("imputeZero")
  public Boolean getImputeZero() {
    return this.imputeZero;
  }

  @JsonProperty("imputeZero")
  public void setImputeZero(Boolean imputeZero) {
    this.imputeZero = imputeZero;
  }

  @JsonProperty("isCollection")
  public Boolean getIsCollection() {
    return this.isCollection;
  }

  @JsonProperty("isCollection")
  public void setIsCollection(Boolean isCollection) {
    this.isCollection = isCollection;
  }

  @JsonProperty("members")
  public List<VariableSpec> getMembers() {
    return this.members;
  }

  @JsonProperty("members")
  public void setMembers(List<VariableSpec> members) {
    this.members = members;
  }
}

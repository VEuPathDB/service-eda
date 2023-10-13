package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isRequired",
    "isTemporal",
    "isCollection",
    "isCompositional",
    "isProportion",
    "allowedNormalizationMethods",
    "minNumVars",
    "maxNumVars",
    "minNumValues",
    "maxNumValues",
    "allowedTypes",
    "allowedShapes",
    "description"
})
public class DataElementConstraintImpl implements DataElementConstraint {
  @JsonProperty("isRequired")
  private Boolean isRequired;

  @JsonProperty("isTemporal")
  private Boolean isTemporal;

  @JsonProperty("isCollection")
  private Boolean isCollection;

  @JsonProperty("isCompositional")
  private Boolean isCompositional;

  @JsonProperty("isProportion")
  private Boolean isProportion;

  @JsonProperty("allowedNormalizationMethods")
  private List<String> allowedNormalizationMethods;

  @JsonProperty("minNumVars")
  private Number minNumVars;

  @JsonProperty("maxNumVars")
  private Number maxNumVars;

  @JsonProperty("minNumValues")
  private Number minNumValues;

  @JsonProperty("maxNumValues")
  private Number maxNumValues;

  @JsonProperty("allowedTypes")
  private List<APIVariableType> allowedTypes;

  @JsonProperty("allowedShapes")
  private List<APIVariableDataShape> allowedShapes;

  @JsonProperty("description")
  private String description;

  @JsonProperty("isRequired")
  public Boolean getIsRequired() {
    return this.isRequired;
  }

  @JsonProperty("isRequired")
  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  @JsonProperty("isTemporal")
  public Boolean getIsTemporal() {
    return this.isTemporal;
  }

  @JsonProperty("isTemporal")
  public void setIsTemporal(Boolean isTemporal) {
    this.isTemporal = isTemporal;
  }

  @JsonProperty("isCollection")
  public Boolean getIsCollection() {
    return this.isCollection;
  }

  @JsonProperty("isCollection")
  public void setIsCollection(Boolean isCollection) {
    this.isCollection = isCollection;
  }

  @JsonProperty("isCompositional")
  public Boolean getIsCompositional() {
    return this.isCompositional;
  }

  @JsonProperty("isCompositional")
  public void setIsCompositional(Boolean isCompositional) {
    this.isCompositional = isCompositional;
  }

  @JsonProperty("isProportion")
  public Boolean getIsProportion() {
    return this.isProportion;
  }

  @JsonProperty("isProportion")
  public void setIsProportion(Boolean isProportion) {
    this.isProportion = isProportion;
  }

  @JsonProperty("allowedNormalizationMethods")
  public List<String> getAllowedNormalizationMethods() {
    return this.allowedNormalizationMethods;
  }

  @JsonProperty("allowedNormalizationMethods")
  public void setAllowedNormalizationMethods(List<String> allowedNormalizationMethods) {
    this.allowedNormalizationMethods = allowedNormalizationMethods;
  }

  @JsonProperty("minNumVars")
  public Number getMinNumVars() {
    return this.minNumVars;
  }

  @JsonProperty("minNumVars")
  public void setMinNumVars(Number minNumVars) {
    this.minNumVars = minNumVars;
  }

  @JsonProperty("maxNumVars")
  public Number getMaxNumVars() {
    return this.maxNumVars;
  }

  @JsonProperty("maxNumVars")
  public void setMaxNumVars(Number maxNumVars) {
    this.maxNumVars = maxNumVars;
  }

  @JsonProperty("minNumValues")
  public Number getMinNumValues() {
    return this.minNumValues;
  }

  @JsonProperty("minNumValues")
  public void setMinNumValues(Number minNumValues) {
    this.minNumValues = minNumValues;
  }

  @JsonProperty("maxNumValues")
  public Number getMaxNumValues() {
    return this.maxNumValues;
  }

  @JsonProperty("maxNumValues")
  public void setMaxNumValues(Number maxNumValues) {
    this.maxNumValues = maxNumValues;
  }

  @JsonProperty("allowedTypes")
  public List<APIVariableType> getAllowedTypes() {
    return this.allowedTypes;
  }

  @JsonProperty("allowedTypes")
  public void setAllowedTypes(List<APIVariableType> allowedTypes) {
    this.allowedTypes = allowedTypes;
  }

  @JsonProperty("allowedShapes")
  public List<APIVariableDataShape> getAllowedShapes() {
    return this.allowedShapes;
  }

  @JsonProperty("allowedShapes")
  public void setAllowedShapes(List<APIVariableDataShape> allowedShapes) {
    this.allowedShapes = allowedShapes;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }
}

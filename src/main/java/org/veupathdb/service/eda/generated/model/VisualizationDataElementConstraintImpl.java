package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isRequired",
    "isTemporal",
    "minNumVars",
    "maxNumVars",
    "maxNumValues",
    "allowedTypes",
    "allowedShapes",
    "description"
})
public class VisualizationDataElementConstraintImpl implements VisualizationDataElementConstraint {
  @JsonProperty("isRequired")
  private Boolean isRequired;

  @JsonProperty("isTemporal")
  private Boolean isTemporal;

  @JsonProperty("minNumVars")
  private Number minNumVars;

  @JsonProperty("maxNumVars")
  private Number maxNumVars;

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

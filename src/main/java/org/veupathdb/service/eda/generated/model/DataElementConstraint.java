package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DataElementConstraintImpl.class
)
public interface DataElementConstraint {
  @JsonProperty("isRequired")
  Boolean getIsRequired();

  @JsonProperty("isRequired")
  void setIsRequired(Boolean isRequired);

  @JsonProperty("isTemporal")
  Boolean getIsTemporal();

  @JsonProperty("isTemporal")
  void setIsTemporal(Boolean isTemporal);

  @JsonProperty("minNumVars")
  Number getMinNumVars();

  @JsonProperty("minNumVars")
  void setMinNumVars(Number minNumVars);

  @JsonProperty("maxNumVars")
  Number getMaxNumVars();

  @JsonProperty("maxNumVars")
  void setMaxNumVars(Number maxNumVars);

  @JsonProperty("minNumValues")
  Number getMinNumValues();

  @JsonProperty("minNumValues")
  void setMinNumValues(Number minNumValues);

  @JsonProperty("maxNumValues")
  Number getMaxNumValues();

  @JsonProperty("maxNumValues")
  void setMaxNumValues(Number maxNumValues);

  @JsonProperty("allowedTypes")
  List<APIVariableType> getAllowedTypes();

  @JsonProperty("allowedTypes")
  void setAllowedTypes(List<APIVariableType> allowedTypes);

  @JsonProperty("allowedShapes")
  List<APIVariableDataShape> getAllowedShapes();

  @JsonProperty("allowedShapes")
  void setAllowedShapes(List<APIVariableDataShape> allowedShapes);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);
}

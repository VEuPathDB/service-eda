package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = RelatedObservationMinTimeIntervalConfigImpl.class
)
public interface RelatedObservationMinTimeIntervalConfig {
  @JsonProperty("relatedObservationsSubset")
  List<APIFilter> getRelatedObservationsSubset();

  @JsonProperty("relatedObservationsSubset")
  void setRelatedObservationsSubset(List<APIFilter> relatedObservationsSubset);

  @JsonProperty("anchorVariable")
  VariableSpec getAnchorVariable();

  @JsonProperty("anchorVariable")
  void setAnchorVariable(VariableSpec anchorVariable);

  @JsonProperty("anchorVariableTrueValues")
  List<String> getAnchorVariableTrueValues();

  @JsonProperty("anchorVariableTrueValues")
  void setAnchorVariableTrueValues(List<String> anchorVariableTrueValues);

  @JsonProperty("anchorTimestampVariable")
  VariableSpec getAnchorTimestampVariable();

  @JsonProperty("anchorTimestampVariable")
  void setAnchorTimestampVariable(VariableSpec anchorTimestampVariable);

  @JsonProperty("targetVariable")
  VariableSpec getTargetVariable();

  @JsonProperty("targetVariable")
  void setTargetVariable(VariableSpec targetVariable);

  @JsonProperty("targetVariableTrueValues")
  List<String> getTargetVariableTrueValues();

  @JsonProperty("targetVariableTrueValues")
  void setTargetVariableTrueValues(List<String> targetVariableTrueValues);

  @JsonProperty("targetTimestampVariable")
  VariableSpec getTargetTimestampVariable();

  @JsonProperty("targetTimestampVariable")
  void setTargetTimestampVariable(VariableSpec targetTimestampVariable);

  @JsonProperty("minimumTimeIntervalDays")
  Integer getMinimumTimeIntervalDays();

  @JsonProperty("minimumTimeIntervalDays")
  void setMinimumTimeIntervalDays(Integer minimumTimeIntervalDays);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}

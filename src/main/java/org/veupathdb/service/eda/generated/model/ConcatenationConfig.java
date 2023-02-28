package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = ConcatenationConfigImpl.class
)
public interface ConcatenationConfig {
  @JsonProperty("prefix")
  String getPrefix();

  @JsonProperty("prefix")
  void setPrefix(String prefix);

  @JsonProperty("delimiter")
  String getDelimiter();

  @JsonProperty("delimiter")
  void setDelimiter(String delimiter);

  @JsonProperty("suffix")
  String getSuffix();

  @JsonProperty("suffix")
  void setSuffix(String suffix);

  @JsonProperty("inputVariables")
  List<VariableSpec> getInputVariables();

  @JsonProperty("inputVariables")
  void setInputVariables(List<VariableSpec> inputVariables);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}

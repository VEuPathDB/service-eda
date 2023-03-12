package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "prefix",
    "delimiter",
    "suffix",
    "inputVariables"
})
public class ConcatenationConfigImpl implements ConcatenationConfig {
  @JsonProperty("prefix")
  private String prefix;

  @JsonProperty("delimiter")
  private String delimiter;

  @JsonProperty("suffix")
  private String suffix;

  @JsonProperty("inputVariables")
  private List<VariableSpec> inputVariables;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("prefix")
  public String getPrefix() {
    return this.prefix;
  }

  @JsonProperty("prefix")
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  @JsonProperty("delimiter")
  public String getDelimiter() {
    return this.delimiter;
  }

  @JsonProperty("delimiter")
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  @JsonProperty("suffix")
  public String getSuffix() {
    return this.suffix;
  }

  @JsonProperty("suffix")
  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  @JsonProperty("inputVariables")
  public List<VariableSpec> getInputVariables() {
    return this.inputVariables;
  }

  @JsonProperty("inputVariables")
  public void setInputVariables(List<VariableSpec> inputVariables) {
    this.inputVariables = inputVariables;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}

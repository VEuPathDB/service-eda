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
    "variable",
    "groupA",
    "groupB"
})
public class ComparatorSpecImpl implements ComparatorSpec {
  @JsonProperty("variable")
  private VariableSpec variable;

  @JsonProperty("groupA")
  private List<BinRange> groupA;

  @JsonProperty("groupB")
  private List<BinRange> groupB;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("variable")
  public VariableSpec getVariable() {
    return this.variable;
  }

  @JsonProperty("variable")
  public void setVariable(VariableSpec variable) {
    this.variable = variable;
  }

  @JsonProperty("groupA")
  public List<BinRange> getGroupA() {
    return this.groupA;
  }

  @JsonProperty("groupA")
  public void setGroupA(List<BinRange> groupA) {
    this.groupA = groupA;
  }

  @JsonProperty("groupB")
  public List<BinRange> getGroupB() {
    return this.groupB;
  }

  @JsonProperty("groupB")
  public void setGroupB(List<BinRange> groupB) {
    this.groupB = groupB;
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

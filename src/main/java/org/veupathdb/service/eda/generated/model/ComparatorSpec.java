package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = ComparatorSpecImpl.class
)
public interface ComparatorSpec {
  @JsonProperty("variable")
  VariableSpec getVariable();

  @JsonProperty("variable")
  void setVariable(VariableSpec variable);

  @JsonProperty("groupA")
  List<BinRange> getGroupA();

  @JsonProperty("groupA")
  void setGroupA(List<BinRange> groupA);

  @JsonProperty("groupB")
  List<BinRange> getGroupB();

  @JsonProperty("groupB")
  void setGroupB(List<BinRange> groupB);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}

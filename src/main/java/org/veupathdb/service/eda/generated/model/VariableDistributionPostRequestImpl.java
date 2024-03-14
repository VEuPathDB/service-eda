package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "filters",
    "binSpec",
    "valueSpec"
})
public class VariableDistributionPostRequestImpl implements VariableDistributionPostRequest {
  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("binSpec")
  private BinSpecWithRange binSpec;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("filters")
  public List<APIFilter> getFilters() {
    return this.filters;
  }

  @JsonProperty("filters")
  public void setFilters(List<APIFilter> filters) {
    this.filters = filters;
  }

  @JsonProperty("binSpec")
  public BinSpecWithRange getBinSpec() {
    return this.binSpec;
  }

  @JsonProperty("binSpec")
  public void setBinSpec(BinSpecWithRange binSpec) {
    this.binSpec = binSpec;
  }

  @JsonProperty("valueSpec")
  public ValueSpec getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ValueSpec valueSpec) {
    this.valueSpec = valueSpec;
  }
}

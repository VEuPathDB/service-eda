package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = VariableDistributionPostRequestImpl.class
)
public interface VariableDistributionPostRequest {
  @JsonProperty("filters")
  List<APIFilter> getFilters();

  @JsonProperty("filters")
  void setFilters(List<APIFilter> filters);

  @JsonProperty("binSpec")
  BinSpecWithRange getBinSpec();

  @JsonProperty("binSpec")
  void setBinSpec(BinSpecWithRange binSpec);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);
}

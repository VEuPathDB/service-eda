package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BoxplotStatsTableImpl.class
)
public interface BoxplotStatsTable {
  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("xVariableDetails")
  StrataVariableDetails getXVariableDetails();

  @JsonProperty("xVariableDetails")
  void setXVariableDetails(StrataVariableDetails xVariableDetails);

  @JsonProperty("statistic")
  String getStatistic();

  @JsonProperty("statistic")
  void setStatistic(String statistic);

  @JsonProperty("pvalue")
  Number getPvalue();

  @JsonProperty("pvalue")
  void setPvalue(Number pvalue);

  @JsonProperty("parameter")
  List<Number> getParameter();

  @JsonProperty("parameter")
  void setParameter(List<Number> parameter);

  @JsonProperty("method")
  String getMethod();

  @JsonProperty("method")
  void setMethod(String method);

  @JsonProperty("statsError")
  String getStatsError();

  @JsonProperty("statsError")
  void setStatsError(String statsError);
}

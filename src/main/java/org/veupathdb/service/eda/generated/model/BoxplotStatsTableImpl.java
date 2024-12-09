package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "facetVariableDetails",
    "xVariableDetails",
    "statistic",
    "pvalue",
    "parameter",
    "method",
    "statsError"
})
public class BoxplotStatsTableImpl implements BoxplotStatsTable {
  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("xVariableDetails")
  private StrataVariableDetails xVariableDetails;

  @JsonProperty("statistic")
  private String statistic;

  @JsonProperty("pvalue")
  private Number pvalue;

  @JsonProperty("parameter")
  private List<Number> parameter;

  @JsonProperty("method")
  private String method;

  @JsonProperty("statsError")
  private String statsError;

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("xVariableDetails")
  public StrataVariableDetails getXVariableDetails() {
    return this.xVariableDetails;
  }

  @JsonProperty("xVariableDetails")
  public void setXVariableDetails(StrataVariableDetails xVariableDetails) {
    this.xVariableDetails = xVariableDetails;
  }

  @JsonProperty("statistic")
  public String getStatistic() {
    return this.statistic;
  }

  @JsonProperty("statistic")
  public void setStatistic(String statistic) {
    this.statistic = statistic;
  }

  @JsonProperty("pvalue")
  public Number getPvalue() {
    return this.pvalue;
  }

  @JsonProperty("pvalue")
  public void setPvalue(Number pvalue) {
    this.pvalue = pvalue;
  }

  @JsonProperty("parameter")
  public List<Number> getParameter() {
    return this.parameter;
  }

  @JsonProperty("parameter")
  public void setParameter(List<Number> parameter) {
    this.parameter = parameter;
  }

  @JsonProperty("method")
  public String getMethod() {
    return this.method;
  }

  @JsonProperty("method")
  public void setMethod(String method) {
    this.method = method;
  }

  @JsonProperty("statsError")
  public String getStatsError() {
    return this.statsError;
  }

  @JsonProperty("statsError")
  public void setStatsError(String statsError) {
    this.statsError = statsError;
  }
}

package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "facetVariableDetails",
    "pvalue",
    "degreesFreedom",
    "chisq"
})
public class ContTableStatsTableImpl implements ContTableStatsTable {
  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("pvalue")
  private List<Number> pvalue;

  @JsonProperty("degreesFreedom")
  private List<Number> degreesFreedom;

  @JsonProperty("chisq")
  private List<Number> chisq;

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("pvalue")
  public List<Number> getPvalue() {
    return this.pvalue;
  }

  @JsonProperty("pvalue")
  public void setPvalue(List<Number> pvalue) {
    this.pvalue = pvalue;
  }

  @JsonProperty("degreesFreedom")
  public List<Number> getDegreesFreedom() {
    return this.degreesFreedom;
  }

  @JsonProperty("degreesFreedom")
  public void setDegreesFreedom(List<Number> degreesFreedom) {
    this.degreesFreedom = degreesFreedom;
  }

  @JsonProperty("chisq")
  public List<Number> getChisq() {
    return this.chisq;
  }

  @JsonProperty("chisq")
  public void setChisq(List<Number> chisq) {
    this.chisq = chisq;
  }
}

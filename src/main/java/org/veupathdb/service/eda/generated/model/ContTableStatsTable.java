package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ContTableStatsTableImpl.class
)
public interface ContTableStatsTable {
  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("pvalue")
  List<Number> getPvalue();

  @JsonProperty("pvalue")
  void setPvalue(List<Number> pvalue);

  @JsonProperty("degreesFreedom")
  List<Number> getDegreesFreedom();

  @JsonProperty("degreesFreedom")
  void setDegreesFreedom(List<Number> degreesFreedom);

  @JsonProperty("chisq")
  List<Number> getChisq();

  @JsonProperty("chisq")
  void setChisq(List<Number> chisq);
}

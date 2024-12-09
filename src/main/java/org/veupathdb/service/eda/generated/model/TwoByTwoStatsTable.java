package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = TwoByTwoStatsTableImpl.class
)
public interface TwoByTwoStatsTable {
  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("chisq")
  Statistic getChisq();

  @JsonProperty("chisq")
  void setChisq(Statistic chisq);

  @JsonProperty("fisher")
  Statistic getFisher();

  @JsonProperty("fisher")
  void setFisher(Statistic fisher);

  @JsonProperty("prevalence")
  Statistic getPrevalence();

  @JsonProperty("prevalence")
  void setPrevalence(Statistic prevalence);

  @JsonProperty("oddsratio")
  Statistic getOddsratio();

  @JsonProperty("oddsratio")
  void setOddsratio(Statistic oddsratio);

  @JsonProperty("relativerisk")
  Statistic getRelativerisk();

  @JsonProperty("relativerisk")
  void setRelativerisk(Statistic relativerisk);

  @JsonProperty("sensitivity")
  Statistic getSensitivity();

  @JsonProperty("sensitivity")
  void setSensitivity(Statistic sensitivity);

  @JsonProperty("specificity")
  Statistic getSpecificity();

  @JsonProperty("specificity")
  void setSpecificity(Statistic specificity);

  @JsonProperty("posPredictiveValue")
  Statistic getPosPredictiveValue();

  @JsonProperty("posPredictiveValue")
  void setPosPredictiveValue(Statistic posPredictiveValue);

  @JsonProperty("negPredictiveValue")
  Statistic getNegPredictiveValue();

  @JsonProperty("negPredictiveValue")
  void setNegPredictiveValue(Statistic negPredictiveValue);
}

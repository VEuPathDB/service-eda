package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "facetVariableDetails",
    "chisq",
    "fisher",
    "prevalence",
    "oddsratio",
    "relativerisk",
    "sensitivity",
    "specificity",
    "posPredictiveValue",
    "negPredictiveValue"
})
public class TwoByTwoStatsTableImpl implements TwoByTwoStatsTable {
  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("chisq")
  private Statistic chisq;

  @JsonProperty("fisher")
  private Statistic fisher;

  @JsonProperty("prevalence")
  private Statistic prevalence;

  @JsonProperty("oddsratio")
  private Statistic oddsratio;

  @JsonProperty("relativerisk")
  private Statistic relativerisk;

  @JsonProperty("sensitivity")
  private Statistic sensitivity;

  @JsonProperty("specificity")
  private Statistic specificity;

  @JsonProperty("posPredictiveValue")
  private Statistic posPredictiveValue;

  @JsonProperty("negPredictiveValue")
  private Statistic negPredictiveValue;

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("chisq")
  public Statistic getChisq() {
    return this.chisq;
  }

  @JsonProperty("chisq")
  public void setChisq(Statistic chisq) {
    this.chisq = chisq;
  }

  @JsonProperty("fisher")
  public Statistic getFisher() {
    return this.fisher;
  }

  @JsonProperty("fisher")
  public void setFisher(Statistic fisher) {
    this.fisher = fisher;
  }

  @JsonProperty("prevalence")
  public Statistic getPrevalence() {
    return this.prevalence;
  }

  @JsonProperty("prevalence")
  public void setPrevalence(Statistic prevalence) {
    this.prevalence = prevalence;
  }

  @JsonProperty("oddsratio")
  public Statistic getOddsratio() {
    return this.oddsratio;
  }

  @JsonProperty("oddsratio")
  public void setOddsratio(Statistic oddsratio) {
    this.oddsratio = oddsratio;
  }

  @JsonProperty("relativerisk")
  public Statistic getRelativerisk() {
    return this.relativerisk;
  }

  @JsonProperty("relativerisk")
  public void setRelativerisk(Statistic relativerisk) {
    this.relativerisk = relativerisk;
  }

  @JsonProperty("sensitivity")
  public Statistic getSensitivity() {
    return this.sensitivity;
  }

  @JsonProperty("sensitivity")
  public void setSensitivity(Statistic sensitivity) {
    this.sensitivity = sensitivity;
  }

  @JsonProperty("specificity")
  public Statistic getSpecificity() {
    return this.specificity;
  }

  @JsonProperty("specificity")
  public void setSpecificity(Statistic specificity) {
    this.specificity = specificity;
  }

  @JsonProperty("posPredictiveValue")
  public Statistic getPosPredictiveValue() {
    return this.posPredictiveValue;
  }

  @JsonProperty("posPredictiveValue")
  public void setPosPredictiveValue(Statistic posPredictiveValue) {
    this.posPredictiveValue = posPredictiveValue;
  }

  @JsonProperty("negPredictiveValue")
  public Statistic getNegPredictiveValue() {
    return this.negPredictiveValue;
  }

  @JsonProperty("negPredictiveValue")
  public void setNegPredictiveValue(Statistic negPredictiveValue) {
    this.negPredictiveValue = negPredictiveValue;
  }
}

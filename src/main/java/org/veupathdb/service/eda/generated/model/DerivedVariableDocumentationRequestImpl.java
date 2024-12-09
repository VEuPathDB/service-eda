package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mean",
    "sum",
    "concatenation",
    "subsetMembership",
    "advancedSubset",
    "unitConversion",
    "bodyMassIndex",
    "categoricalRecoding",
    "continuousToOrdinal",
    "ecmaScriptExpressionEval",
    "relativeObservationMinTimeInterval"
})
public class DerivedVariableDocumentationRequestImpl implements DerivedVariableDocumentationRequest {
  @JsonProperty("mean")
  private SingleNumericVarReductionConfig mean;

  @JsonProperty("sum")
  private SingleNumericVarReductionConfig sum;

  @JsonProperty("concatenation")
  private ConcatenationConfig concatenation;

  @JsonProperty("subsetMembership")
  private SubsetMembershipConfig subsetMembership;

  @JsonProperty("advancedSubset")
  private AdvancedSubsetConfig advancedSubset;

  @JsonProperty("unitConversion")
  private UnitConversionConfig unitConversion;

  @JsonProperty("bodyMassIndex")
  private BodyMassIndexConfig bodyMassIndex;

  @JsonProperty("categoricalRecoding")
  private CategoricalRecodingConfig categoricalRecoding;

  @JsonProperty("continuousToOrdinal")
  private ContinuousNumericRecodingConfig continuousToOrdinal;

  @JsonProperty("ecmaScriptExpressionEval")
  private EcmaScriptExpressionEvalConfig ecmaScriptExpressionEval;

  @JsonProperty("relativeObservationMinTimeInterval")
  private RelatedObservationMinTimeIntervalConfig relativeObservationMinTimeInterval;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("mean")
  public SingleNumericVarReductionConfig getMean() {
    return this.mean;
  }

  @JsonProperty("mean")
  public void setMean(SingleNumericVarReductionConfig mean) {
    this.mean = mean;
  }

  @JsonProperty("sum")
  public SingleNumericVarReductionConfig getSum() {
    return this.sum;
  }

  @JsonProperty("sum")
  public void setSum(SingleNumericVarReductionConfig sum) {
    this.sum = sum;
  }

  @JsonProperty("concatenation")
  public ConcatenationConfig getConcatenation() {
    return this.concatenation;
  }

  @JsonProperty("concatenation")
  public void setConcatenation(ConcatenationConfig concatenation) {
    this.concatenation = concatenation;
  }

  @JsonProperty("subsetMembership")
  public SubsetMembershipConfig getSubsetMembership() {
    return this.subsetMembership;
  }

  @JsonProperty("subsetMembership")
  public void setSubsetMembership(SubsetMembershipConfig subsetMembership) {
    this.subsetMembership = subsetMembership;
  }

  @JsonProperty("advancedSubset")
  public AdvancedSubsetConfig getAdvancedSubset() {
    return this.advancedSubset;
  }

  @JsonProperty("advancedSubset")
  public void setAdvancedSubset(AdvancedSubsetConfig advancedSubset) {
    this.advancedSubset = advancedSubset;
  }

  @JsonProperty("unitConversion")
  public UnitConversionConfig getUnitConversion() {
    return this.unitConversion;
  }

  @JsonProperty("unitConversion")
  public void setUnitConversion(UnitConversionConfig unitConversion) {
    this.unitConversion = unitConversion;
  }

  @JsonProperty("bodyMassIndex")
  public BodyMassIndexConfig getBodyMassIndex() {
    return this.bodyMassIndex;
  }

  @JsonProperty("bodyMassIndex")
  public void setBodyMassIndex(BodyMassIndexConfig bodyMassIndex) {
    this.bodyMassIndex = bodyMassIndex;
  }

  @JsonProperty("categoricalRecoding")
  public CategoricalRecodingConfig getCategoricalRecoding() {
    return this.categoricalRecoding;
  }

  @JsonProperty("categoricalRecoding")
  public void setCategoricalRecoding(CategoricalRecodingConfig categoricalRecoding) {
    this.categoricalRecoding = categoricalRecoding;
  }

  @JsonProperty("continuousToOrdinal")
  public ContinuousNumericRecodingConfig getContinuousToOrdinal() {
    return this.continuousToOrdinal;
  }

  @JsonProperty("continuousToOrdinal")
  public void setContinuousToOrdinal(ContinuousNumericRecodingConfig continuousToOrdinal) {
    this.continuousToOrdinal = continuousToOrdinal;
  }

  @JsonProperty("ecmaScriptExpressionEval")
  public EcmaScriptExpressionEvalConfig getEcmaScriptExpressionEval() {
    return this.ecmaScriptExpressionEval;
  }

  @JsonProperty("ecmaScriptExpressionEval")
  public void setEcmaScriptExpressionEval(EcmaScriptExpressionEvalConfig ecmaScriptExpressionEval) {
    this.ecmaScriptExpressionEval = ecmaScriptExpressionEval;
  }

  @JsonProperty("relativeObservationMinTimeInterval")
  public RelatedObservationMinTimeIntervalConfig getRelativeObservationMinTimeInterval() {
    return this.relativeObservationMinTimeInterval;
  }

  @JsonProperty("relativeObservationMinTimeInterval")
  public void setRelativeObservationMinTimeInterval(
      RelatedObservationMinTimeIntervalConfig relativeObservationMinTimeInterval) {
    this.relativeObservationMinTimeInterval = relativeObservationMinTimeInterval;
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

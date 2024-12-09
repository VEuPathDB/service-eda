package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = DerivedVariableDocumentationRequestImpl.class
)
public interface DerivedVariableDocumentationRequest {
  @JsonProperty("mean")
  SingleNumericVarReductionConfig getMean();

  @JsonProperty("mean")
  void setMean(SingleNumericVarReductionConfig mean);

  @JsonProperty("sum")
  SingleNumericVarReductionConfig getSum();

  @JsonProperty("sum")
  void setSum(SingleNumericVarReductionConfig sum);

  @JsonProperty("concatenation")
  ConcatenationConfig getConcatenation();

  @JsonProperty("concatenation")
  void setConcatenation(ConcatenationConfig concatenation);

  @JsonProperty("subsetMembership")
  SubsetMembershipConfig getSubsetMembership();

  @JsonProperty("subsetMembership")
  void setSubsetMembership(SubsetMembershipConfig subsetMembership);

  @JsonProperty("advancedSubset")
  AdvancedSubsetConfig getAdvancedSubset();

  @JsonProperty("advancedSubset")
  void setAdvancedSubset(AdvancedSubsetConfig advancedSubset);

  @JsonProperty("unitConversion")
  UnitConversionConfig getUnitConversion();

  @JsonProperty("unitConversion")
  void setUnitConversion(UnitConversionConfig unitConversion);

  @JsonProperty("bodyMassIndex")
  BodyMassIndexConfig getBodyMassIndex();

  @JsonProperty("bodyMassIndex")
  void setBodyMassIndex(BodyMassIndexConfig bodyMassIndex);

  @JsonProperty("categoricalRecoding")
  CategoricalRecodingConfig getCategoricalRecoding();

  @JsonProperty("categoricalRecoding")
  void setCategoricalRecoding(CategoricalRecodingConfig categoricalRecoding);

  @JsonProperty("continuousToOrdinal")
  ContinuousNumericRecodingConfig getContinuousToOrdinal();

  @JsonProperty("continuousToOrdinal")
  void setContinuousToOrdinal(ContinuousNumericRecodingConfig continuousToOrdinal);

  @JsonProperty("ecmaScriptExpressionEval")
  EcmaScriptExpressionEvalConfig getEcmaScriptExpressionEval();

  @JsonProperty("ecmaScriptExpressionEval")
  void setEcmaScriptExpressionEval(EcmaScriptExpressionEvalConfig ecmaScriptExpressionEval);

  @JsonProperty("relativeObservationMinTimeInterval")
  RelatedObservationMinTimeIntervalConfig getRelativeObservationMinTimeInterval();

  @JsonProperty("relativeObservationMinTimeInterval")
  void setRelativeObservationMinTimeInterval(
      RelatedObservationMinTimeIntervalConfig relativeObservationMinTimeInterval);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}

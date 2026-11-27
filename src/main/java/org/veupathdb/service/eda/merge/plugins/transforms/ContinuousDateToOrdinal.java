package org.veupathdb.service.eda.merge.plugins.transforms;

import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.ContinuousDateRecodingConfig;
import org.veupathdb.service.eda.generated.model.ContinuousDateRule;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.merge.core.derivedvars.Transform;
import org.veupathdb.service.eda.subset.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Recodes a date variable into ordinal categories by inclusive date ranges,
 * matching the semantics of the Subset service's date range filter.
 */
public class ContinuousDateToOrdinal extends Transform<ContinuousDateRecodingConfig> {

  private static class RuleApplier {

    private final LocalDateTime _minInclusive;
    private final LocalDateTime _maxInclusive;
    private final String _outputValue;

    public RuleApplier(ContinuousDateRule rule) {
      _minInclusive = parseBound(rule.getMinInclusive(), LocalDateTime.MIN);
      _maxInclusive = parseBound(rule.getMaxInclusive(), LocalDateTime.MAX);
      _outputValue = rule.getOutputValue();
    }

    private static LocalDateTime parseBound(String bound, LocalDateTime defaultValue) {
      return bound == null || bound.isEmpty()
        ? defaultValue
        : FormatUtil.parseDateTime(Utils.standardizeLocalDateTime(bound));
    }

    public boolean matches(LocalDateTime d) {
      return !d.isBefore(_minInclusive) && !d.isAfter(_maxInclusive);
    }

    public String getOutputValue() {
      return _outputValue;
    }
  }

  private VariableSpec _inputVar;
  private String _inputColumn;
  private List<RuleApplier> _codingRules;
  private String _unmappedValue;

  @Override
  protected Class<ContinuousDateRecodingConfig> getConfigClass() {
    return ContinuousDateRecodingConfig.class;
  }

  @Override
  protected void acceptConfig(ContinuousDateRecodingConfig config) {
    _inputVar = config.getInputVariable();
    _inputColumn = VariableDef.toDotNotation(_inputVar);
    _codingRules = config.getRules().stream().map(RuleApplier::new).toList();
    _unmappedValue = Optional.ofNullable(config.getUnmappedValue()).orElse(EMPTY_VALUE);
  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {
    checkVariable("Input", _inputVar, List.of(APIVariableType.DATE), List.of(APIVariableDataShape.CONTINUOUS));
  }

  @Override
  public String getFunctionName() {
    return "continuousDateToOrdinal";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_inputVar);
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.STRING;
  }

  @Override
  public APIVariableDataShape getDataShape() {
    return APIVariableDataShape.ORDINAL;
  }

  @Override
  public List<String> getVocabulary() {
    return RecodingVocabulary.of(_codingRules.stream().map(RuleApplier::getOutputValue).toList(), _unmappedValue);
  }

  @Override
  public String getValue(Map<String, String> row) {
    String value = row.get(_inputColumn);
    if (value == null || value.isEmpty()) return _unmappedValue;
    LocalDateTime d = value.contains("T")
      ? LocalDateTime.parse(value)
      : LocalDate.parse(value).atStartOfDay();
    for (RuleApplier rule : _codingRules) {
      if (rule.matches(d)) return rule.getOutputValue();
    }
    return _unmappedValue;
  }
}

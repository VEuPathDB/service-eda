package eda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gusdb.fgputil.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.ContinuousDateRecodingConfig;
import org.veupathdb.service.eda.generated.model.ContinuousDateRecodingConfigImpl;
import org.veupathdb.service.eda.generated.model.ContinuousDateRule;
import org.veupathdb.service.eda.generated.model.ContinuousDateRuleImpl;
import org.veupathdb.service.eda.generated.model.ContinuousNumericRecodingConfig;
import org.veupathdb.service.eda.generated.model.ContinuousNumericRecodingConfigImpl;
import org.veupathdb.service.eda.generated.model.ContinuousNumericRule;
import org.veupathdb.service.eda.generated.model.ContinuousNumericRuleImpl;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpecImpl;
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariableFactory;
import org.veupathdb.service.eda.merge.plugins.transforms.ContinuousDateToOrdinal;
import org.veupathdb.service.eda.merge.plugins.transforms.ContinuousNumericToOrdinal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the continuousToOrdinal and continuousDateToOrdinal transform plugins
 */
public class RangeRecodingTest {

  private static ContinuousNumericRule numRule(Double min, Double max, String output) {
    ContinuousNumericRule rule = new ContinuousNumericRuleImpl();
    rule.setMinInclusive(min);
    rule.setMaxInclusive(max);
    rule.setOutputValue(output);
    return rule;
  }

  private static ContinuousDateRule dateRule(String min, String max, String output) {
    ContinuousDateRule rule = new ContinuousDateRuleImpl();
    rule.setMinInclusive(min);
    rule.setMaxInclusive(max);
    rule.setOutputValue(output);
    return rule;
  }

  @SuppressWarnings("unchecked")
  private static <T> T build(String functionName, Object config, Class<T> pluginClass) throws Exception {
    DerivedVariableSpec spec = new DerivedVariableSpecImpl();
    spec.setEntityId("e");
    spec.setVariableId("out");
    spec.setDisplayName("Out");
    spec.setFunctionName(functionName);
    spec.setConfig(config);
    // round-trip so the config is a Map, as it is for real requests
    String json = JsonUtil.Jackson.writeValueAsString(spec);
    DerivedVariableFactory factory =
      new DerivedVariableFactory(null, List.of(JsonUtil.Jackson.readValue(json, DerivedVariableSpecImpl.class)));
    return (T) factory.getAllDerivedVars().stream()
      .filter(pluginClass::isInstance).findFirst().orElseThrow();
  }

  private static ContinuousNumericToOrdinal numeric(List<ContinuousNumericRule> rules, String unmapped) throws Exception {
    ContinuousNumericRecodingConfig config = new ContinuousNumericRecodingConfigImpl();
    config.setInputVariable(VariableDef.newVariableSpec("e", "v"));
    config.setRules(rules);
    config.setUnmappedValue(unmapped);
    return build("continuousToOrdinal", config, ContinuousNumericToOrdinal.class);
  }

  private static ContinuousDateToOrdinal date(List<ContinuousDateRule> rules, String unmapped) throws Exception {
    ContinuousDateRecodingConfig config = new ContinuousDateRecodingConfigImpl();
    config.setInputVariable(VariableDef.newVariableSpec("e", "v"));
    config.setRules(rules);
    config.setUnmappedValue(unmapped);
    return build("continuousDateToOrdinal", config, ContinuousDateToOrdinal.class);
  }

  private static Map<String, String> row(String value) {
    Map<String, String> row = new HashMap<>();
    row.put("e.v", value);
    return row;
  }

  @Test
  void numericEndsAreInclusiveFirstRuleWins() throws Exception {
    ContinuousNumericToOrdinal p = numeric(List.of(
      numRule(0.0, 10.0, "A"),
      numRule(10.0, 20.0, "B")), "Other");
    assertEquals("A", p.getValue(row("10")));
    assertEquals("B", p.getValue(row("20")));
    assertEquals("Other", p.getValue(row("20.5")));
  }

  @Test
  void numericMissingBoundsAreOpen() throws Exception {
    ContinuousNumericToOrdinal p = numeric(List.of(
      numRule(null, 0.0, "Low"),
      numRule(100.0, null, "High")), "Other");
    assertEquals("Low", p.getValue(row("-5")));
    assertEquals("Low", p.getValue(row("0")));
    assertEquals("High", p.getValue(row("1e9")));
    assertEquals("Other", p.getValue(row("50")));
  }

  @Test
  void numericMissingInputGetsUnmappedValue() throws Exception {
    List<ContinuousNumericRule> rules = List.of(numRule(0.0, 10.0, "A"));
    assertEquals("Other", numeric(rules, "Other").getValue(row("")));
    assertEquals("", numeric(rules, null).getValue(row("")));
  }

  @Test
  void numericVocabularyIncludesUnmapped() throws Exception {
    List<ContinuousNumericRule> rules = List.of(numRule(0.0, 10.0, "A"), numRule(10.0, 20.0, "B"));
    assertEquals(List.of("A", "B", "Other"), numeric(rules, "Other").getVocabulary());
    assertEquals(List.of("A", "B"), numeric(rules, null).getVocabulary());
  }

  @Test
  void dateRulesUseFilterParsingInclusive() throws Exception {
    ContinuousDateToOrdinal p = date(List.of(
      dateRule("2020-01-01T00:00:00Z", "2020-01-31T00:00:00Z", "Jan")), "Other");
    assertEquals("Jan", p.getValue(row("2020-01-01")));
    assertEquals("Jan", p.getValue(row("2020-01-31")));
    assertEquals("Other", p.getValue(row("2020-01-31T12:00:00")));
    assertEquals("Other", p.getValue(row("2019-12-31T23:59:59")));
  }

  @Test
  void dateMissingInputAndVocabulary() throws Exception {
    ContinuousDateToOrdinal p = date(List.of(
      dateRule("2020-01-01T00:00:00Z", "2020-01-31T00:00:00Z", "Jan")), "Other");
    assertEquals("Other", p.getValue(row("")));
    assertEquals(List.of("Jan", "Other"), p.getVocabulary());
  }
}

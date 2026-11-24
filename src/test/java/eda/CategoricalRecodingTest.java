package eda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.CategoricalRecodingConfig;
import org.veupathdb.service.eda.generated.model.CategoricalRecodingConfigImpl;
import org.veupathdb.service.eda.generated.model.CategoricalRecodingRule;
import org.veupathdb.service.eda.generated.model.CategoricalRecodingRuleImpl;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpecImpl;
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariableFactory;
import org.veupathdb.service.eda.merge.plugins.transforms.CategoricalRecoding;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the categoricalRecoding transform plugin
 */
public class CategoricalRecodingTest {

  private static CategoricalRecodingRule rule(List<String> values, List<String> prefixes, String output) {
    CategoricalRecodingRule rule = new CategoricalRecodingRuleImpl();
    rule.setInputValues(values);
    rule.setInputPrefixes(prefixes);
    rule.setOutputValue(output);
    return rule;
  }

  private static CategoricalRecoding plugin(List<CategoricalRecodingRule> rules, String unmappedValue) throws Exception {
    CategoricalRecodingConfig config = new CategoricalRecodingConfigImpl();
    config.setInputVariable(VariableDef.newVariableSpec("e", "v"));
    config.setRules(rules);
    config.setUnmappedValue(unmappedValue);
    DerivedVariableSpec spec = new DerivedVariableSpecImpl();
    spec.setEntityId("e");
    spec.setVariableId("out");
    spec.setDisplayName("Out");
    spec.setFunctionName("categoricalRecoding");
    spec.setConfig(config);
    // round-trip so the config is a Map, as it is for real requests
    String json = JsonUtil.Jackson.writeValueAsString(spec);
    DerivedVariableFactory factory =
      new DerivedVariableFactory(null, List.of(JsonUtil.Jackson.readValue(json, DerivedVariableSpecImpl.class)));
    return (CategoricalRecoding) factory.getAllDerivedVars().stream()
      .filter(CategoricalRecoding.class::isInstance).findFirst().orElseThrow();
  }

  private static Map<String, String> row(String value) {
    Map<String, String> row = new HashMap<>();
    row.put("e.v", value);
    return row;
  }

  private static List<CategoricalRecodingRule> abaRules() {
    return List.of(
      rule(List.of("x"), null, "A"),
      rule(List.of("y"), null, "B"),
      rule(List.of("z"), null, "A"));
  }

  @Test
  void matchesValuesAndPrefixesFirstRuleWins() throws Exception {
    CategoricalRecoding p = plugin(List.of(
      rule(List.of("x"), List.of("dqc"), "East"),
      rule(null, List.of("dq"), "West")), "Other");
    assertEquals("East", p.getValue(row("x")));
    assertEquals("East", p.getValue(row("dqcr1")));
    assertEquals("West", p.getValue(row("dqb")));
    assertEquals("Other", p.getValue(row("zz")));
  }

  @Test
  void missingInputGetsUnmappedValue() throws Exception {
    assertEquals("Other", plugin(abaRules(), "Other").getValue(row("")));
    assertEquals("", plugin(abaRules(), null).getValue(row("")));
  }

  @Test
  void vocabularyIsDistinctOutputsThenNonEmptyUnmapped() throws Exception {
    assertEquals(List.of("A", "B", "Other"), plugin(abaRules(), "Other").getVocabulary());
    assertEquals(List.of("A", "B"), plugin(abaRules(), "").getVocabulary());
    assertEquals(List.of("A", "B"), plugin(abaRules(), null).getVocabulary());
    assertEquals(List.of("A", "B"), plugin(abaRules(), "A").getVocabulary());
  }

  @Test
  void rejectsRuleWithNeitherValuesNorPrefixes() {
    for (CategoricalRecodingRule bad : List.of(
        rule(null, null, "A"),
        rule(List.of(), List.of(), "A"))) {
      ValidationException e = assertThrows(ValidationException.class, () -> plugin(List.of(bad), null));
      assertTrue(e.getMessage().contains("Each rule needs inputValues or inputPrefixes"));
    }
  }

  @Test
  void rejectsMultiValuedInput() {
    ValidationException e = assertThrows(ValidationException.class,
      () -> CategoricalRecoding.checkNotMultiValued(varDef(true)));
    assertTrue(e.getMessage().contains("Input variable must not be multi-valued"));
    assertDoesNotThrow(() -> CategoricalRecoding.checkNotMultiValued(varDef(false)));
  }

  private static VariableDef varDef(boolean multiValued) {
    return new VariableDef("e", "v", APIVariableType.STRING, APIVariableDataShape.CATEGORICAL,
      multiValued, false, Optional.empty(), Optional.empty(), null, List.of(), false, null, VariableSource.NATIVE);
  }
}

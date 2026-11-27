package eda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIStringSetFilter;
import org.veupathdb.service.eda.generated.model.APIStringSetFilterImpl;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpecImpl;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroup;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroupImpl;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroupsConfig;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroupsConfigImpl;
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariable;
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariableFactory;
import org.veupathdb.service.eda.merge.plugins.transforms.FilterDefinedGroups;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the filterDefinedGroups transform plugin
 */
public class FilterDefinedGroupsTest {

  private static FilterDefinedGroup group(String label, boolean withFilter) {
    FilterDefinedGroup group = new FilterDefinedGroupImpl();
    group.setLabel(label);
    List<org.veupathdb.service.eda.generated.model.APIFilter> filters = new ArrayList<>();
    if (withFilter) {
      APIStringSetFilter filter = new APIStringSetFilterImpl();
      filter.setEntityId("e");
      filter.setVariableId("region");
      filter.setStringSet(List.of(label));
      filters.add(filter);
    }
    group.setFilters(filters);
    return group;
  }

  private static DerivedVariableFactory factory(List<FilterDefinedGroup> groups, String unmappedValue) throws Exception {
    FilterDefinedGroupsConfig config = new FilterDefinedGroupsConfigImpl();
    config.setGroups(groups);
    config.setUnmappedValue(unmappedValue);
    DerivedVariableSpec spec = new DerivedVariableSpecImpl();
    spec.setEntityId("e");
    spec.setVariableId("v");
    spec.setDisplayName("V");
    spec.setFunctionName(FilterDefinedGroups.FUNCTION_NAME);
    spec.setConfig(config);
    // round-trip so the config is a Map, as it is for real requests
    String json = JsonUtil.Jackson.writeValueAsString(spec);
    return new DerivedVariableFactory(null, List.of(JsonUtil.Jackson.readValue(json, DerivedVariableSpecImpl.class)));
  }

  private static FilterDefinedGroups plugin(DerivedVariableFactory factory) {
    return (FilterDefinedGroups) factory.getAllDerivedVars().stream()
      .filter(FilterDefinedGroups.class::isInstance).findFirst().orElseThrow();
  }

  private static List<FilterDefinedGroup> eastWest() {
    return List.of(group("East", true), group("West", true));
  }

  private static Map<String, String> row(FilterDefinedGroups plugin, String... values) {
    Map<String, String> row = new HashMap<>();
    List<DerivedVariableSpec> helpers = plugin.getDependedDerivedVarSpecs();
    for (int i = 0; i < helpers.size(); i++) {
      row.put(VariableDef.toDotNotation(helpers.get(i)), values[i]);
    }
    return row;
  }

  @Test
  void declaresOneMembershipHelperPerGroupBeforeItself() throws Exception {
    List<DerivedVariable> all = factory(eastWest(), null).getAllDerivedVars();
    assertEquals(3, all.size());
    assertTrue(all.get(2) instanceof FilterDefinedGroups);
    assertEquals("subsetMembership", all.get(0).getFunctionName());
    assertEquals("v_group1", all.get(0).getVariableId());
    assertEquals("subsetMembership", all.get(1).getFunctionName());
    assertEquals("v_group2", all.get(1).getVariableId());
  }

  @Test
  void valueIsFirstMatchingGroup() throws Exception {
    FilterDefinedGroups plugin = plugin(factory(eastWest(), null));
    assertEquals("East", plugin.getValue(row(plugin, "1", "1")));
    assertEquals("West", plugin.getValue(row(plugin, "0", "1")));
  }

  @Test
  void unmatchedRowGetsUnmappedValueOrEmpty() throws Exception {
    FilterDefinedGroups withOther = plugin(factory(eastWest(), "Other"));
    assertEquals("Other", withOther.getValue(row(withOther, "0", "0")));
    FilterDefinedGroups without = plugin(factory(eastWest(), null));
    assertEquals("", without.getValue(row(without, "0", "0")));
  }

  @Test
  void vocabularyIsLabelsThenNonEmptyUnmappedValue() throws Exception {
    assertEquals(List.of("East", "West", "Other"), plugin(factory(eastWest(), "Other")).getVocabulary());
    assertEquals(List.of("East", "West"), plugin(factory(eastWest(), "")).getVocabulary());
    assertEquals(List.of("East", "West"), plugin(factory(eastWest(), null)).getVocabulary());
  }

  @Test
  void rejectsInvalidConfigs() {
    assertThrows(ValidationException.class, () -> factory(List.of(), null));
    assertThrows(ValidationException.class, () -> factory(List.of(group("", true)), null));
    assertThrows(ValidationException.class, () -> factory(List.of(group("East", true), group("East", true)), null));
    assertThrows(ValidationException.class, () -> factory(List.of(group("East", false)), null));
    assertThrows(ValidationException.class, () -> factory(eastWest(), "East"));
  }
}

package eda;

import org.gusdb.fgputil.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.generated.model.AnalysisDescriptor;
import org.veupathdb.service.eda.generated.model.AnalysisDescriptorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies the analysis descriptor stores full derived variable specs
 */
public class AnalysisDescriptorDerivedVariablesTest {

  private static final String DESCRIPTOR_JSON = "{" +
      "\"subset\":{\"descriptor\":[]}," +
      "\"computations\":[]," +
      "\"starredVariables\":[]," +
      "\"dataTableConfig\":{}," +
      "\"derivedVariables\":[{" +
      "\"entityId\":\"e\",\"variableId\":\"v\"," +
      "\"functionName\":\"filterDefinedGroups\",\"displayName\":\"Region\"," +
      "\"config\":{\"groups\":[]}}]}";

  @Test
  void descriptorRoundTripsDerivedVariableSpecs() throws Exception {
    AnalysisDescriptor descriptor = JsonUtil.Jackson.readValue(DESCRIPTOR_JSON, AnalysisDescriptorImpl.class);
    assertEquals(1, descriptor.getDerivedVariables().size());
    assertEquals("filterDefinedGroups", descriptor.getDerivedVariables().get(0).getFunctionName());
    assertEquals("v", descriptor.getDerivedVariables().get(0).getVariableId());
    assertEquals("Region", descriptor.getDerivedVariables().get(0).getDisplayName());
  }
}

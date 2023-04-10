package org.veupathdb.service.eda;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.ConcatenationConfig;
import org.veupathdb.service.eda.generated.model.ConcatenationConfigImpl;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpecImpl;

public class DerivedVarConfigTest {

  private final static Logger LOG = LogManager.getLogger(DerivedVarConfigTest.class);

  private static final List<DerivedVariableSpec> SPECS = createSpecs();

  private static List<DerivedVariableSpec> createSpecs() {
    try {
      DerivedVariableSpec spec = new DerivedVariableSpecImpl();
      spec.setEntityId("a");
      spec.setVariableId("b");
      spec.setDisplayName("blah");
      spec.setFunctionName("concatenation");
      ConcatenationConfig config = new ConcatenationConfigImpl();
      config.setInputVariables(List.of(
          VariableDef.newVariableSpec("a", "c"))
      );
      spec.setConfig(config);
      String json = JsonUtil.Jackson.writeValueAsString(spec);
      return List.of(JsonUtil.Jackson.readValue(json, DerivedVariableSpecImpl.class));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testConfigParsing() {
    // just trying to get the factory to build in this test; may mock a ReferenceMetadata later
    DerivedVariableFactory factory = new DerivedVariableFactory(null, SPECS);
  }
}

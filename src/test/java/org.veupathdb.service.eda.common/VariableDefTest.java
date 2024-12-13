package org.veupathdb.service.eda.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gusdb.fgputil.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.DataRanges;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

import java.util.List;
import java.util.Optional;

public class VariableDefTest {

  @Test
  public void testSerializeDeserialize() throws JsonProcessingException {
    VariableDef variableDef = new VariableDef(
        "entityId",
        "variableId",
        APIVariableType.INTEGER,
        APIVariableDataShape.CONTINUOUS,
        false,
        false,
        Optional.empty(),
        Optional.empty(),
        "parentId",
        List.of("1", "2"),
        false,
        null,
        VariableSource.NATIVE
    );
    String serialized = JsonUtil.serializeObject(variableDef);
    JsonUtil.Jackson.readValue(serialized, VariableSpecImpl.class);
  }
}

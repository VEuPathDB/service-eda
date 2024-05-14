package org.veupathdb.service.eda.common;

import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class EntityDefTest {

  @Test
  public void testPerformance() {
    EntityDef def = new EntityDef("id", "perf-test", "perf-test-colname", false);
    Instant start = Instant.now();
    for (int i = 0; i < 150_000; i++) {
      def.addVariable(new VariableDef(
          "id",
          "variableId" + i,
          APIVariableType.STRING,
          APIVariableDataShape.CATEGORICAL,
          false,
          false,
          Optional.empty(),
          Optional.empty(),
          "id",
          List.of("a", "b"),
          false,
          null,
          VariableSource.NATIVE
      ));
    }
    System.out.println(Duration.between(start, Instant.now()));
  }
}

package org.veupathdb.service.eda.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

public class Test {

  public static void main(String[] args) throws Exception {
    VariableSpec var = new VariableSpecImpl();
    var.setEntityId("myEntity");
    var.setVariableId("myVariable");
    System.out.println(new ObjectMapper().writerFor(VariableSpec.class).writeValueAsString(var));
  }
}

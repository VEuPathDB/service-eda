package org.veupathdb.service.eda.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gusdb.fgputil.FormatUtil;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

public class Test {

  public static String getSampleVariableJsonStringSize() {
    try {
      VariableSpec var = new VariableSpecImpl();
      var.setEntityId("myEntity");
      var.setVariableId("myVariable");
      String s = new ObjectMapper().writerFor(VariableSpec.class).writeValueAsString(var);
      return s + " has " + FormatUtil.getUtf8EncodedBytes(s).length + " UTF-8 encoded bytes!";
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

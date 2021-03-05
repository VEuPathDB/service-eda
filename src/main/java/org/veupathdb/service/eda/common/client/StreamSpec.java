package org.veupathdb.service.eda.common.client;

import java.util.ArrayList;
import java.util.Collection;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class StreamSpec extends ArrayList<VariableSpec> {

  private String _streamName;
  private String _entityId;

  public StreamSpec(String streamName, String entityId) {
    _streamName = streamName;
    _entityId = entityId;
  }

  public String getStreamName() {
    return _streamName;
  }

  public String getEntityId() {
    return _entityId;
  }

  public StreamSpec addVariable(VariableSpec variableSpec) {
    add(variableSpec);
    return this;
  }

  public StreamSpec addVariables(Collection<VariableDef> variableSpecs) {
    addAll(variableSpecs);
    return this;
  }
}

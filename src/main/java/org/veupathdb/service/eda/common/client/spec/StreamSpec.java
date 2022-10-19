package org.veupathdb.service.eda.common.client.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.gusdb.fgputil.json.JsonUtil;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class StreamSpec extends ArrayList<VariableSpec> {

  private final String _streamName;
  private final String _entityId;
  private boolean _includeComputedVars;

  public StreamSpec(String streamName, String entityId) {
    _streamName = streamName;
    _entityId = entityId;
    _includeComputedVars = false;
  }

  public String getStreamName() {
    return _streamName;
  }

  public String getEntityId() {
    return _entityId;
  }

  public StreamSpec setIncludeComputedVars(boolean includeComputedVars) {
    _includeComputedVars = includeComputedVars;
    return this;
  }

  public StreamSpec addVar(VariableSpec variableSpec) {
    if (variableSpec != null) {
      add(variableSpec);
    }
    return this;
  }

  public <T extends VariableSpec> StreamSpec addVars(Collection<T> variableSpecs) {
    if (variableSpecs != null) {
      addAll(variableSpecs);
    }
    return this;
  }

  @Override
  public String toString() {
    List<String> vars = stream()
        .map(var -> JsonUtil.serializeObject(var))
        .collect(Collectors.toList());
    return new JSONObject()
      .put("name", _streamName)
      .put("entityId", _entityId)
      .put("variables", vars)
      .toString(2);
  }
}

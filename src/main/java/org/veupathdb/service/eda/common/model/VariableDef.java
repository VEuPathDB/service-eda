package org.veupathdb.service.eda.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.gusdb.fgputil.json.JsonUtil;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.DerivationType;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

import static org.veupathdb.service.eda.common.model.VariableSource.DERIVED_BY_REDUCTION;
import static org.veupathdb.service.eda.common.model.VariableSource.DERIVED_BY_TRANSFORM;

/**
 * Wrapper containing only what a plugin would need to know about a variable for
 * the purposes of validation; its name, parent entity, and data type
 */
@JsonPropertyOrder({
    "entityId",
    "variableId",
    "type",
    "source"
})
public class VariableDef extends VariableSpecImpl {

  public static VariableSpec newVariableSpec(String entityId, String variableId) {
    VariableSpec spec = new VariableSpecImpl();
    spec.setEntityId(entityId);
    spec.setVariableId(variableId);
    return spec;
  }

  @JsonProperty("type")
  private final APIVariableType _type;

  @JsonProperty("source")
  private final VariableSource _source;

  public VariableDef(String entityId, String variableId, APIVariableType type, VariableSource source) {
    setEntityId(entityId);
    setVariableId(variableId);
    _type = type;
    _source = source;
  }

  public VariableDef(String entityId, String variableId, APIVariableType type, DerivationType derivationType) {
    setEntityId(entityId);
    setVariableId(variableId);
    _type = type;
    _source = switch(derivationType) {
      case REDUCTION -> DERIVED_BY_REDUCTION;
      case TRANSFORM -> DERIVED_BY_TRANSFORM;
    };
  }

  public static String toDotNotation(VariableSpec var) {
    return var.getEntityId() + "." + var.getVariableId();
  }

  @JsonProperty("type")
  public APIVariableType getType() {
    return _type;
  }

  @JsonProperty("source")
  public VariableSource getSource() {
    return _source;
  }

  @Override
  public String toString() {
    return JsonUtil.serializeObject(this);
  }

  public static boolean isSameVariable(VariableSpec v1, VariableSpec v2) {
    return v1.getEntityId().equals(v2.getEntityId()) && v1.getVariableId().equals(v2.getVariableId());
  }
}

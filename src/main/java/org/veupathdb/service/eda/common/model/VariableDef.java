package org.veupathdb.service.eda.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.stream.Collectors;
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
    "variableId"
})
public class VariableDef extends VariableSpecImpl {

  public static VariableSpec newVariableSpec(String entityId, String variableId) {
    VariableSpec spec = new VariableSpecImpl();
    spec.setEntityId(entityId);
    spec.setVariableId(variableId);
    return spec;
  }

  @JsonIgnore
  private final APIVariableType _type;

  @JsonIgnore
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

  @JsonIgnore
  public APIVariableType getType() {
    return _type;
  }

  @JsonIgnore
  public VariableSource getSource() {
    return _source;
  }

  @Override
  public String toString() {
    return JsonUtil.serializeObject(this);
  }

  public static String toDotNotation(VariableSpec var) {
    return var.getEntityId() + "." + var.getVariableId();
  }

  public static <T extends VariableSpec> List<String> toDotNotation(List<T> vars) {
    return vars.stream().map(var -> toDotNotation(var)).collect(Collectors.toList());
  }

  public static boolean isSameVariable(VariableSpec v1, VariableSpec v2) {
    return v1.getEntityId().equals(v2.getEntityId()) && v1.getVariableId().equals(v2.getVariableId());
  }
}

package org.veupathdb.service.eda.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.VariableSpec;

public class EntityDef extends ArrayList<VariableDef> {

  private final String _id;
  private final String _displayName;
  private final VariableDef _idColumnDef;

  public EntityDef(String id, String displayName, String idColumnName) {
    _id = id;
    _displayName = displayName;
    _idColumnDef = new VariableDef(_id, idColumnName, APIVariableType.STRING,
        APIVariableDataShape.CONTINUOUS ,VariableSource.ID);
    add(_idColumnDef);
  }

  public String getId() {
    return _id;
  }

  public VariableDef getIdColumnDef() {
    return _idColumnDef;
  }

  public Optional<VariableDef> getVariable(VariableSpec var) {
    return stream()
      .filter(v -> VariableDef.isSameVariable(v, var))
      .findFirst();
  }

  @Override
  public String toString() {
    return new JSONObject()
      .put("id", _id)
      .put("displayName", _displayName)
      .put("idColumnName", _idColumnDef.getVariableId())
      .put("variables", stream()
        .map(var -> VariableDef.toDotNotation(var) + ":" + var.getType().toString().toLowerCase())
        .collect(Collectors.toList()))
      .toString(2);
  }

}

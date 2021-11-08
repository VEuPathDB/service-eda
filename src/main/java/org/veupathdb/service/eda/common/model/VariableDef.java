package org.veupathdb.service.eda.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.json.JsonUtil;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
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

  public static class DataRange extends TwoTuple<String,String> {
    public DataRange(String start, String end) { super(start, end); }
    public String getStart() { return getFirst(); }
    public String getEnd() { return getSecond(); }
  }

  public static class DataRanges extends TwoTuple<DataRange,DataRange> {
    public DataRanges(DataRange dataRange, DataRange displayRange) { super(dataRange, displayRange); }
    public DataRange getDataRange() { return getFirst(); }
    public DataRange getDisplayRange() { return getSecond(); }
  }

  @JsonIgnore
  private final APIVariableType _type;

  @JsonIgnore
  private final APIVariableDataShape _dataShape;

  @JsonIgnore
  private final VariableSource _source;

  @JsonIgnore
  private final boolean _isMultiValue;

  @JsonIgnore
  private final Optional<DataRanges> _dataRanges;

  @JsonIgnore
  private final String _parentId;

  public VariableDef(
      String entityId,
      String variableId,
      APIVariableType type,
      APIVariableDataShape dataShape,
      boolean isMultiValue,
      Optional<DataRanges> dataRanges,
      String parentId,
      VariableSource source) {
    setEntityId(entityId);
    setVariableId(variableId);
    _type = type;
    _dataShape = dataShape;
    _isMultiValue = isMultiValue;
    _dataRanges = dataRanges;
    _parentId = parentId;
    _source = source;
  }

  public VariableDef(
      String entityId,
      String variableId,
      APIVariableType type,
      APIVariableDataShape dataShape,
      DerivationType derivationType) {
    this(entityId, variableId, type, dataShape, false, Optional.empty(), null,
      switch(derivationType) {
        case REDUCTION -> DERIVED_BY_REDUCTION;
        case TRANSFORM -> DERIVED_BY_TRANSFORM;
      }
    );
  }

  @JsonIgnore
  public APIVariableType getType() {
    return _type;
  }

  @JsonIgnore
  public APIVariableDataShape getDataShape() {
    return _dataShape;
  }

  @JsonIgnore
  public boolean isMultiValue() {
    return _isMultiValue;
  }

  @JsonIgnore
  public Optional<DataRanges> getDataRanges() {
    return _dataRanges;
  }

  @JsonIgnore
  public VariableSource getSource() {
    return _source;
  }

  @JsonIgnore
  public String getParentId() {
    return _parentId;
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

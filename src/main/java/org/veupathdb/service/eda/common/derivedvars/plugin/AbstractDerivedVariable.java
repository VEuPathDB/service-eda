package org.veupathdb.service.eda.common.derivedvars.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractDerivedVariable<T> extends VariableSpecImpl implements DerivedVariable {

  private static final Logger LOG = LogManager.getLogger(AbstractDerivedVariable.class);

  protected ReferenceMetadata _metadata;
  protected String _displayName;

  // essentially cached values so they are not calculated for each row
  private String _columnName;
  private List<String> _dependedColumnNames;

  protected abstract Class<T> getConfigClass();

  /**
   * Read and validate config, assigning local fields as needed to execute derived
   * variable logic later during data processing
   *
   * @param config config object sent as part of the request
   * @throws ValidationException if validation fails
   */
  protected abstract void acceptConfig(T config) throws ValidationException;

  @Override
  public void init(ReferenceMetadata metadata, DerivedVariableSpec spec) throws ValidationException {
    _metadata = metadata;
    setEntityId(spec.getEntityId());
    setVariableId(spec.getVariableId());
    _displayName = spec.getDisplayName();
    acceptConfig(convertConfig(spec.getConfig()));
  }

  private T convertConfig(Object configObject) {
    if (configObject instanceof Map) {
      try {
        Map<String,Object> map = (Map<String, Object>)configObject;
        JSONObject jsonObj = new JSONObject(map);
        LOG.info("Received the following config, to be converted to " + getConfigClass().getName() + ":" + FormatUtil.NL + jsonObj.toString(2));
        return JsonUtil.Jackson.readValue(jsonObj.toString(), getConfigClass());
      }
      catch (JSONException | JsonProcessingException e) {
        throw new BadRequestException("Could not coerce config object for spec " +
            "of derived variable " + getFunctionName() + " to type " + getConfigClass().getName());
      }
    }
    throw new BadRequestException("config property for spec of derived variable " +
        getFunctionName() + " must be an object");
  }

  @Override
  public EntityDef getEntity() {
    return _metadata.getEntity(getEntityId()).orElseThrow(
        () -> new BadRequestException("Could not find derived variable entity: " + getEntityId()));
  }

  @Override
  public String getColumnName() {
    // only calculate once
    if (_columnName == null) {
      _columnName = VariableDef.toDotNotation(this);
    }
    return _columnName;
  }

  @Override
  public List<String> getRequiredInputColumnNames() {
    // only calculate once
    if (_dependedColumnNames == null) {
      _dependedColumnNames = VariableDef.toDotNotation(getRequiredInputVars());
    }
    return _dependedColumnNames;
  }

  @Override
  public List<DerivedVariableSpec> getDependedDerivedVarSpecs() {
    return Collections.emptyList();
  }

  @Override
  public String getDisplayName() {
    return _displayName;
  }

  @Override
  public String toString() {
    return "{ functionName: " + getFunctionName() + ", variable: " + VariableDef.toDotNotation(this) + " }";
  }
}

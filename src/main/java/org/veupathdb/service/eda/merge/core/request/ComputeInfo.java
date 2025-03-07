package org.veupathdb.service.eda.merge.core.request;

import org.veupathdb.service.eda.generated.model.ComputeRequestBase;
import org.veupathdb.service.eda.generated.model.ComputedVariableMetadata;
import org.veupathdb.service.eda.generated.model.VariableMapping;

import java.util.List;

/**
 * Encapsulates information about a requested compute.  This includes compute
 * information provided by the incoming request (compute name and config) and
 * compute metadata information fetched from the compute service about the job
 * itself (computed entity and computed variables).
 */
public class ComputeInfo {

  // final values set by config
  private final String             _computeName;
  private final ComputeRequestBase _requestBody;

  // delayed population by metadata fetched from compute service
  private String _computeEntity;
  private List<VariableMapping> _variableMetadata;

  public ComputeInfo(String computeName, ComputeRequestBase requestBody) {
    _computeName = computeName;
    _requestBody = requestBody;
  }

  public void setMetadata(ComputedVariableMetadata jobVariableMetadata) {
    _variableMetadata = jobVariableMetadata.getVariables();
    for (VariableMapping mapping : _variableMetadata) {
      if (_computeEntity == null) {
        _computeEntity = mapping.getVariableSpec().getEntityId();
      }
      // make sure all vars have the same entity
      else if (!_computeEntity.equals(mapping.getVariableSpec().getEntityId())) {
        throw new IllegalStateException("Not all computed vars are for the same entity");
      }
    }
  }

  public String getComputeName() {
    return _computeName;
  }

  public ComputeRequestBase getRequestBody() {
    return _requestBody;
  }

  public String getComputeEntity() {
    return _computeEntity;
  }

  public List<VariableMapping> getVariables() {
    return _variableMetadata;
  }

}

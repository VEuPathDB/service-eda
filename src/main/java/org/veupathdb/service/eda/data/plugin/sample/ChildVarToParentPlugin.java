package org.veupathdb.service.eda.data.plugin.sample;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.generated.model.ChildVarToParentPostRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ChildVarToParentPlugin extends AbstractEmptyComputePlugin<ChildVarToParentPostRequest, VariableSpec> {

  @Override
  protected EmptyComputeClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(ChildVarToParentPostRequest.class, VariableSpec.class);
  }

  @Override
  protected void validateVisualizationSpec(VariableSpec pluginSpec) throws ValidationException {

  }

  @Override
  protected List<StreamSpec> getRequestedStreams(VariableSpec pluginSpec) {
    return null;
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) throws IOException {

  }
}

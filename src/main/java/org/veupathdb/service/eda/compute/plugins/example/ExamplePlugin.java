package org.veupathdb.service.eda.compute.plugins.example;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.ExamplePluginConfig;
import org.veupathdb.service.eda.generated.model.ExamplePluginRequest;

import java.util.List;

public class ExamplePlugin extends AbstractPlugin<ExamplePluginRequest, ExamplePluginConfig> {

  private static final String INPUT_DATA = "example-input";

  public ExamplePlugin(@NotNull PluginContext<ExamplePluginRequest, ExamplePluginConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getOutputEntityId()));
  }

  @Override
  protected void execute() {
    // TODO: actually do something.
    getWorkspace().writeDataResult(getWorkspace().openStream(INPUT_DATA));
  }
}

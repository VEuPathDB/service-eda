package org.veupathdb.service.eda.compute.plugins.example;

import org.gusdb.fgputil.DelimitedDataParser;
import org.gusdb.fgputil.functional.Functions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.ExamplePluginConfig;
import org.veupathdb.service.eda.generated.model.ExamplePluginRequest;

import java.io.*;
import java.util.List;

public class ExamplePlugin extends AbstractPlugin<ExamplePluginRequest, ExamplePluginConfig> {

  private static final String INPUT_DATA = "example-input";
  private static final String COMPUTED_COLUMN_NAME_SUFFIX = "WithSuffix";

  public ExamplePlugin(@NotNull PluginContext<ExamplePluginRequest, ExamplePluginConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    ExamplePluginConfig config = getConfig();
    return List.of(new StreamSpec(INPUT_DATA, config.getOutputEntityId())
        .addVars(List.of(config.getInputVariable())));
  }

  @Override
  protected void execute() {
    ExamplePluginConfig config = getConfig();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();
    EntityDef entity = metadata.getEntity(config.getOutputEntityId()).orElseThrow();
    int numIds = metadata.getAncestors(entity).size() + 1;
    getWorkspace().writeDataResult(outStream -> {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(getWorkspace().openStream(INPUT_DATA)))) {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream));
        // write header
        out.write(in.readLine() + COMPUTED_COLUMN_NAME_SUFFIX);
        out.newLine();
        while (in.ready()) {
          String line = in.readLine();
          if (!line.endsWith("\t")) {
            // a tab at the end means no value for this var on this record; value with suffix should also be empty
            line += config.getValueSuffix();
          }
          out.write(line);
          out.newLine();
        }
        out.flush();
      }
      catch (IOException e) {
        throw new RuntimeException("Unable to convert incoming tabular stream or write to output", e);
      }
    });
    getWorkspace().writeMetaResult(new JSONObject()
        .put("computeColumnName", config.getInputVariable().getVariableId() + COMPUTED_COLUMN_NAME_SUFFIX)
        .toString()
    );
    getWorkspace().writeStatisticsResult("{}");
  }
}

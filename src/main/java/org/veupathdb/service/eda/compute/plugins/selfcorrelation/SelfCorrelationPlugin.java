package org.veupathdb.service.eda.compute.plugins.selfcorrelation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.json.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.CollectionDef;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.SelfCorrelationConfig;
import org.veupathdb.service.eda.generated.model.SelfCorrelationPluginRequest;
import org.veupathdb.service.eda.generated.model.FeaturePrefilterThresholds;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.CollectionSpec;

import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class SelfCorrelationPlugin extends AbstractPlugin<SelfCorrelationPluginRequest, SelfCorrelationConfig> {
  private static final Logger LOG = LogManager.getLogger(SelfCorrelationPlugin.class);

  private static final String INPUT_DATA = "inputData";

  public SelfCorrelationPlugin(@NotNull PluginContext<SelfCorrelationPluginRequest, SelfCorrelationConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    // Get the collection variable and its entity
    SelfCorrelationConfig computeConfig = getConfig();
    CollectionSpec collection = computeConfig.getData1();

    return List.of(
      new StreamSpec(INPUT_DATA, getConfig().getData1().getEntityId())
        .addVars(getUtil().getCollectionMembers(collection))
    );
  }

  @Override
  protected void execute() {

    SelfCorrelationConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    // Get compute parameters
    String method = computeConfig.getCorrelationMethod().getValue();
    CollectionSpec collectionSpec = computeConfig.getData1();
    FeaturePrefilterThresholds featureFilterThresholds = computeConfig.getPrefilterThresholds();
    String proportionNonZeroThresholdRParam = 
      featureFilterThresholds != null &&
      featureFilterThresholds.getProportionNonZero() != null ? 
        ",proportionNonZeroThreshold=" + featureFilterThresholds.getProportionNonZero() : "";
    String varianceThresholdRParam = 
      featureFilterThresholds != null &&
      featureFilterThresholds.getVariance() != null ? 
        ",varianceThreshold=" + featureFilterThresholds.getVariance() : "";
    String stdDevThresholdRParam =
      featureFilterThresholds != null &&
      featureFilterThresholds.getStandardDeviation() != null ? 
        ",stdDevThreshold=" + featureFilterThresholds.getStandardDeviation() : "";

    String entityId = collectionSpec.getEntityId();
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();
    VariableDef entityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(entityIdVarSpec);
    CollectionDef collection = metadata.getCollection(collectionSpec).orElseThrow(); 

    // Get record id columns
    List<VariableDef> entityAncestorIdColumns = new ArrayList<>();
    for (EntityDef ancestor : metadata.getAncestors(entity)) {
      entityAncestorIdColumns.add(ancestor.getIdColumnDef());
    }    

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the collection data
      List<VariableSpec> collectionInputVars = ListBuilder.asList(entityIdVarSpec);
      collectionInputVars.addAll(util.getCollectionMembers(collection));
      collectionInputVars.addAll(entityAncestorIdColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, collectionInputVars));

      // Turn the list of id columns into an array of strings for R
      List<String> dotNotatedEntityIdColumns = entityAncestorIdColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedEntityIdColumnsString = util.listToRVector(dotNotatedEntityIdColumns);

      // are we mbio stuffs or eigengene?
      // presumably as we support more types in the future, this logic will become more complicated?
      // might even involve subclassing plugins?
      // i think we cross that bridge when we get there and know more.. 
      // NOTE: getMember tells us the member type, rather than gives us a literal member
      String collectionMemberType = collection.getMember() == null ? "unknown" : collection.getMember();
      String dataClassRString = "microbiomeData::AbundanceData";
      if (collectionMemberType.toLowerCase().contains("eigengene")) {
        dataClassRString = "veupathUtils::CollectionWithMetadata";
      }
      
      connection.voidEval("data <- " + dataClassRString + "(name=" + singleQuote(collectionMemberType) + ",data=collectionData" + 
                                  ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                  ", ancestorIdColumns=as.character(" + dotNotatedEntityIdColumnsString + ")" +
                                  ", imputeZero=TRUE)");

      // Run correlation!
      connection.voidEval("computeResult <- veupathUtils::selfCorrelation(data=data" +
                                                            ", method=" + singleQuote(method) +
                                                            proportionNonZeroThresholdRParam +
                                                            varianceThresholdRParam +
                                                            stdDevThresholdRParam +
                                                            ", verbose=TRUE)");

      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}

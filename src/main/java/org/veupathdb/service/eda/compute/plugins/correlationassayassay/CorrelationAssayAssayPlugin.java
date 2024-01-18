package org.veupathdb.service.eda.compute.plugins.correlationassayassay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.json.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.Correlation2Collections;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayPluginRequest;
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

public class CorrelationAssayAssayPlugin extends AbstractPlugin<CorrelationAssayAssayPluginRequest, Correlation2Collections> {
  private static final Logger LOG = LogManager.getLogger(CorrelationAssayAssayPlugin.class);

  private static final String ASSAY_1_DATA = "assay1Data";
  private static final String ASSAY_2_DATA = "assay2Data";

  public CorrelationAssayAssayPlugin(@NotNull PluginContext<CorrelationAssayAssayPluginRequest, Correlation2Collections> context) {
    super(context);
  }

  /*
  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
    // im going to try leaving the dependency order out for now and hope nothing breaks
    // the two collections can be from different branches or the same entity, which doesnt fit into the dependency order logic
    // im manually validating their relationship before requesting data streams
    // unfortunately this means the frontend needs to be manually validated as well and cant rely only on the constraints defined here
    // TODO: consider a better way to do this
    .dependencyOrder()
      .pattern()
        .element("collectionVariable1")
          .required(true)
          // right now all collections are on the assay nodes. if that changes it may break the manually validated entity relationships
          .isCollection()
          .description("Input data must be a variable collection.")
        .element("collectionVariable2")
          .required(true)
          .isCollection()
          .description("Input data must be a variable collection.")
      .done();
  }
  */

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    // Get the collection variable and its entity
    Correlation2Collections computeConfig = getConfig();
    CollectionSpec assay1 = computeConfig.getCollectionVariable1();
    String entity1Id = assay1.getEntityId();
    CollectionSpec assay2 = computeConfig.getCollectionVariable2();
    String entity2Id = assay2.getEntityId();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    EntityDef entity1 = metadata.getEntity(entity1Id).orElseThrow();
    EntityDef entity2 = metadata.getEntity(entity2Id).orElseThrow();

    // validate the collection variables are on the same entity or both are 1:1 with a shared parent entity
    if (!entity1Id.equals(entity2Id) &&
        !(metadata.getAncestors(entity1).get(0).getId().equals(metadata.getAncestors(entity2).get(0).getId()) &&
          !entity1.isManyToOneWithParent() && !entity2.isManyToOneWithParent())
    ) {
        throw new IllegalArgumentException("Collection variables must be on the same entity or both be 1:1 with a shared parent entity.");
    }

    return List.of(
      new StreamSpec(ASSAY_1_DATA, getConfig().getCollectionVariable1().getEntityId())
        .addVars(getUtil().getCollectionMembers(assay1)),
      new StreamSpec(ASSAY_2_DATA, getConfig().getCollectionVariable2().getEntityId())
        .addVars(getUtil().getCollectionMembers(assay2))
      );
  }

  @Override
  protected void execute() {

    Correlation2Collections computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    // Get compute parameters
    String method = computeConfig.getCorrelationMethod().getValue();
    CollectionSpec assay1 = computeConfig.getCollectionVariable1();
    CollectionSpec assay2 = computeConfig.getCollectionVariable2();
    FeaturePrefilterThresholds featureFilterThresholds = computeConfig.getFeatureFilterThresholds();
    String proportionNonZeroThresholdRParam = 
      featureFilterThresholds.getPropertionNonZero() != null ? 
        ",proportionNonZeroThreshold=" + featureFilterThresholds.getPropertionNonZero() : "";
    String varianceThresholdRParam = 
      featureFilterThresholds.getVariance() != null ? 
        ",varianceThreshold=" + featureFilterThresholds.getVariance() : "";
    String stdDevThresholdRParam =
      featureFilterThresholds.getStandardDeviation() != null ? 
        ",stdDevThreshold=" + featureFilterThresholds.getStandardDeviation() : "";

    // identify if were on the same entity, or need to work in the parent entity space
    String entity1Id = assay1.getEntityId();
    String entity2Id = assay2.getEntityId();
    boolean isSameEntity = entity1Id.equals(entity2Id);
    
    // Wrangle into helpful types
    EntityDef entity1 = metadata.getEntity(entity1Id).orElseThrow();
    EntityDef entity2 = metadata.getEntity(entity2Id).orElseThrow();
    EntityDef parentEntity = metadata.getAncestors(entity1).get(0);
    VariableDef entity1IdVarSpec = util.getEntityIdVarSpec(entity1Id);
    VariableDef entity2IdVarSpec = util.getEntityIdVarSpec(entity2Id);
    VariableDef parentEntityIdVarSpec = util.getEntityIdVarSpec(parentEntity.getId());
    VariableSpec computeEntityIdVarSpec = isSameEntity ? entity1IdVarSpec : parentEntityIdVarSpec;
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);

    // Get record id columns
    List<VariableDef> entity1AncestorIdColumns = new ArrayList<>();
    for (EntityDef ancestor : metadata.getAncestors(entity1)) {
      entity1AncestorIdColumns.add(ancestor.getIdColumnDef());
    }
    List<VariableDef> entity2AncestorIdColumns = new ArrayList<>();
    for (EntityDef ancestor : metadata.getAncestors(entity2)) {
      entity2AncestorIdColumns.add(ancestor.getIdColumnDef());
    }
    // if were not on the same entity, we can remove the assay entity id from the id columns
    if (!isSameEntity) {
      entity1AncestorIdColumns.remove(entity1IdVarSpec);
      entity2AncestorIdColumns.remove(entity2IdVarSpec);
    }
    

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(ASSAY_1_DATA, getWorkspace().openStream(ASSAY_1_DATA));
    dataStream.put(ASSAY_2_DATA, getWorkspace().openStream(ASSAY_2_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the assay data
      List<VariableSpec> assay1InputVars = ListBuilder.asList(computeEntityIdVarSpec);
      assay1InputVars.addAll(util.getCollectionMembers(assay1));
      assay1InputVars.addAll(entity1AncestorIdColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(ASSAY_1_DATA, assay1InputVars));

      List<VariableSpec> assay2InputVars = ListBuilder.asList(computeEntityIdVarSpec);
      assay2InputVars.addAll(util.getCollectionMembers(assay2));
      assay2InputVars.addAll(entity2AncestorIdColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(ASSAY_2_DATA, assay2InputVars));

      // Turn the list of id columns into an array of strings for R
      List<String> dotNotatedEntity1IdColumns = entity1AncestorIdColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedEntity1IdColumnsString = util.listToRVector(dotNotatedEntity1IdColumns);
      List<String> dotNotatedEntity2IdColumns = entity2AncestorIdColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedEntity2IdColumnsString = util.listToRVector(dotNotatedEntity2IdColumns);
            
      
      // Format inputs for R
      connection.voidEval("data1 <- AbundanceData(data=assay1Data" + 
                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                ", ancestorIdColumns=as.character(" + dotNotatedEntity1IdColumnsString + ")" +
                                ", imputeZero=TRUE)");
      
      connection.voidEval("data2 <- AbundanceData(data = assay2Data" +
                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                ", ancestorIdColumns=as.character(" + dotNotatedEntity2IdColumnsString + ")" +
                                ", imputeZero=TRUE)");
      
      // Run correlation!
      connection.voidEval("computeResult <- microbiomeComputations::correlation(data1=data1, data2=data2" +
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

package org.veupathdb.service.eda.compute.plugins.correlation;

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
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.Correlation1Collection;
import org.veupathdb.service.eda.generated.model.Correlation2Collections;
import org.veupathdb.service.eda.generated.model.CorrelationConfig;
import org.veupathdb.service.eda.generated.model.CorrelationPluginRequest;
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

public class CorrelationPlugin extends AbstractPlugin<CorrelationPluginRequest, CorrelationConfig> {
  private static final Logger LOG = LogManager.getLogger(CorrelationPlugin.class);

  private static final String ASSAY_1_DATA = "assay1Data";
  private static final String ASSAY_2_DATA = "assay2Data";
  private static final String INPUT_DATA = "inputData";

  public CorrelationPlugin(@NotNull PluginContext<CorrelationPluginRequest, CorrelationConfig> context) {
    super(context);
  }

  // Return all metadata variables appropriate for this correlation computation. When there are ancestor entities,
  // we only want those variables that are on a one-to-one path with the current entity. When there
  // are no ancestor entities, we'll take all variables that are not part of a collection as metadata variables.
  // Finally, we'll filter down to only the continuous variables.
  private List<VariableDef> filterMetadataVariables(EntityDef entity, ReferenceMetadata metadata) {

    List<EntityDef> ancestors = metadata.getAncestors(entity);

    List<VariableDef> metadataVariables;
  
    if (ancestors.isEmpty()) {
      LOG.info("No ancestors found, using all variables that are not part of a collection.");
  
      List<VariableDef> variablesInACollection = entity.getCollections().stream()
          .flatMap(collection -> collection.getMemberVariables().stream())
          .collect(Collectors.toList());
  
      metadataVariables = entity.getVariables().stream()
          .filter(var -> var.getSource() != VariableSource.ID)
          .filter(var -> !variablesInACollection.contains(var))
          .collect(Collectors.toList());
    } else {
      LOG.info("At least one ancestor found. Looking for variables on entities that are on a one-to-one path with entity: {}", entity.getId());
      metadataVariables = ancestors.stream()
          .filter(ancestor -> !getManyToOneWithDescendant(metadata, ancestor, entity))
          .flatMap(entityDef -> entityDef.getVariables().stream())
          .filter(var -> var.getSource() != VariableSource.ID)
          .collect(Collectors.toList());
    }
  
    metadataVariables = metadataVariables.stream()
      .filter(var -> var.getDataShape() == APIVariableDataShape.CONTINUOUS && var.getSource().isResident())
      .collect(Collectors.toList());
  
    return metadataVariables;
  }

  private Boolean getManyToOneWithDescendant(ReferenceMetadata metadata, EntityDef ancestor, EntityDef descendantToMatch) {
    return metadata.getChildren(ancestor).stream()
        .filter(child -> metadata.isEntityAncestorOf(child, descendantToMatch) || descendantToMatch.getId().equals(child.getId())) // Find child on path to descendant to match.
        .findFirst()
        .orElseThrow()
        .isManyToOneWithParent();
  }

  private List<StreamSpec> getAssayAssayStreamSpecs(Correlation2Collections computeConfig) {
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
      new StreamSpec(ASSAY_1_DATA, computeConfig.getCollectionVariable1().getEntityId())
        .addVars(getUtil().getCollectionMembers(assay1)),
      new StreamSpec(ASSAY_2_DATA, computeConfig.getCollectionVariable2().getEntityId())
        .addVars(getUtil().getCollectionMembers(assay2))
      );
  }

  private List<StreamSpec> getAssayMetadataStreamSpecs(Correlation1Collection computeConfig) {
    CollectionSpec collectionVariable = computeConfig.getCollectionVariable();
    String entityId = collectionVariable.getEntityId();

    // Wrangle into correct types for what follows
    EntityDef entity = getContext().getReferenceMetadata().getEntity(entityId).orElseThrow();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    // Filter metadata variables into only those that are appropriate for correlation
    List<VariableDef> metadataVariables = filterMetadataVariables(entity, metadata);

    LOG.info("Using the following metadata variables for correlation: {}",
        JsonUtil.serializeObject(metadataVariables));

    return List.of(new StreamSpec(INPUT_DATA, computeConfig.getCollectionVariable().getEntityId())
        .addVars(getUtil().getCollectionMembers(collectionVariable))
        .addVars(metadataVariables)
      );
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    CorrelationConfig computeConfig = getConfig();

    if (computeConfig.isCorrelation2Collections()) {
      return getAssayAssayStreamSpecs(computeConfig.getCorrelation2Collections());
    } else if (computeConfig.isCorrelation1Collection()) {
      return getAssayMetadataStreamSpecs(computeConfig.getCorrelation1Collection());
    } else {
      return(null);
    }
  }

  // TODO for seriously refactor these two monsters..
  private void executeAssayAssay(Correlation2Collections computeConfig, PluginUtil util, ReferenceMetadata metadata) {
    // Get compute parameters
    String method = computeConfig.getCorrelationMethod().getValue();
    CollectionSpec assay1 = computeConfig.getCollectionVariable1();
    CollectionSpec assay2 = computeConfig.getCollectionVariable2();
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
    CollectionDef collection1 = metadata.getCollection(assay1).orElseThrow();
    CollectionDef collection2 = metadata.getCollection(assay2).orElseThrow();

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
            
      // are we mbio stuffs or eigengene?
      // presumably as we support more types in the future, this logic will become more complicated?
      // might even involve subclassing plugins?
      // i think we cross that bridge when we get there and know more.. 
      // NOTE: getMember tells us the member type, rather than gives us a literal member
      String collection1MemberType = collection1.getMember() == null ? "unknown" : collection1.getMember();
      String collection2MemberType = collection2.getMember() == null ? "unknown" : collection2.getMember();
      boolean isEigengene = false;
      // If either collection is an eigengene, we'll use our base correlation function in veupathUtils,
      // so we want to set the isEigengene flag to true.
      if (collection1MemberType.toLowerCase().contains("eigengene") || collection2MemberType.toLowerCase().contains("eigengene")) {
        isEigengene = true;
      }      

      // Prep data and run correlation
      if (isEigengene) {
        // If we have eigenegene data, we'll use our base correlation function in veupathUtils, so we
        // only need to make data frames for the assay data and sample metadata.
        connection.voidEval("data1 <- assay1Data; " + 
          "data1 <- data1[order(" + computeEntityIdColName + ")]; " + 
          "data1 <- data1[, -as.character(" + dotNotatedEntity1IdColumnsString + "), with=FALSE];" +
          "data1 <- data1[, -" + singleQuote(computeEntityIdColName) + ", with=FALSE]");

        connection.voidEval("data2 <- assay2Data; " +
          "data2 <- data2[order(" + computeEntityIdColName + ")]; " + 
          "data2 <- data2[, -as.character(" + dotNotatedEntity2IdColumnsString + "), with=FALSE];" +
          "data2 <- data2[, -" + singleQuote(computeEntityIdColName) + ", with=FALSE]");

        connection.voidEval("computeResult <- veupathUtils::correlation(data1=data1, data2=data2" +
                                                            ", method=" + singleQuote(method) +
                                                            ", verbose=TRUE)");
      } else {
        // If we don't have eigengene data, for now we can assume the data is abundance data.
        // Abundance data can go through our microbiomeComputations pipeline.
        connection.voidEval("data1 <- microbiomeData::AbundanceData(data=assay1Data" + 
                                  ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                  ", ancestorIdColumns=as.character(" + dotNotatedEntity1IdColumnsString + ")" +
                                  ", imputeZero=TRUE)");
      
        connection.voidEval("data2 <- microbiomeData::AbundanceData(data = assay2Data" +
                                  ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                  ", ancestorIdColumns=as.character(" + dotNotatedEntity2IdColumnsString + ")" +
                                  ", imputeZero=TRUE)");

        // Run correlation!
        connection.voidEval("computeResult <- veupathUtils::correlation(data1=data1, data2=data2" +
                                                            ", method=" + singleQuote(method) +
                                                            proportionNonZeroThresholdRParam +
                                                            varianceThresholdRParam +
                                                            stdDevThresholdRParam +
                                                            ", verbose=TRUE)");
      }
      
      String statsCmd = "veupathUtils::writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }

  private void executeAssayMetadata(Correlation1Collection computeConfig, PluginUtil util, ReferenceMetadata metadata) {
    String method = computeConfig.getCorrelationMethod().getValue();
    CollectionSpec collectionVariable = computeConfig.getCollectionVariable();
    String entityId = collectionVariable.getEntityId();
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

    // Wrangle into helpful types
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    CollectionDef collection = metadata.getCollection(collectionVariable).orElseThrow(); 

    // Get record id columns
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : metadata.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    // Filter metadata variables into only those that are appropriate for correlation.
    List<VariableDef> metadataVariables = filterMetadataVariables(entity, metadata);

    LOG.info("Using the following metadata variables for correlation: {}",
        JsonUtil.serializeObject(metadataVariables));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the assay data
      List<VariableSpec> abundanceInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      abundanceInputVars.addAll(util.getCollectionMembers(collectionVariable));
      abundanceInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, abundanceInputVars));
      connection.voidEval("assayData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

      // Read in the sample metadata
      List<VariableSpec> metadataInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      metadataInputVars.addAll(metadataVariables);
      metadataInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, metadataInputVars));
      connection.voidEval("sampleMetadata <- " + INPUT_DATA); 

      // Turn the list of id columns into an array of strings for R
      List<String> dotNotatedIdColumns = idColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedIdColumnsString = util.listToRVector(dotNotatedIdColumns);

      // are we mbio stuffs or eigengene?
      // presumably as we support more types in the future, this logic will become more complicated?
      // might even involve subclassing plugins?
      // i think we cross that bridge when we get there and know more.. 
      // NOTE: getMember tells us the member type, rather than gives us a literal member
      String collectionMemberType = collection.getMember() == null ? "unknown" : collection.getMember();
      boolean isEigengene = false;
      if (collectionMemberType.toLowerCase().contains("eigengene")) {
        isEigengene = true;
      }
      
      // Prep data and run correlation
      if (isEigengene)  {
        // If we have eigenegene data, we'll use our base correlation function in veupathUtils, so we
        // only need to make data frames for the assay data and sample metadata.
        connection.voidEval("eigengeneData <- assayData[order(" + computeEntityIdColName + ")]; " + 
          "eigengeneData <- eigengeneData[, -as.character(" + dotNotatedIdColumnsString+ "), with=FALSE];" +
          "eigengeneData <- eigengeneData[, -" + singleQuote(computeEntityIdColName) + ", with=FALSE];");

        connection.voidEval("sampleMetadata <- sampleMetadata[order(" + computeEntityIdColName + ")]; " + 
          "sampleMetadata <- sampleMetadata[, -as.character(" + dotNotatedIdColumnsString + "), with=FALSE];" +
          "sampleMetadata <- sampleMetadata[, -" + singleQuote(computeEntityIdColName) + ", with=FALSE];");
        
        connection.voidEval("computeResult <- veupathUtils::correlation(data1=eigengeneData, data2=sampleMetadata" +
                                  ", method=" + singleQuote(method) +
                                  ", verbose=TRUE)");
      } else {
        // If we don't have eigengene data, for now we can assume the data is abundance data.
        // Abundance data can go through our microbiomeComputations pipeline.
        connection.voidEval("sampleMetadata <- microbiomeData::SampleMetadata(data = sampleMetadata" +
                                  ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                  ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + "))");

        connection.voidEval("abundanceData <- microbiomeData::AbundanceData(data=assayData" + 
                                  ", sampleMetadata=sampleMetadata" +
                                  ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                  ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                  ", imputeZero=TRUE)");       
                                  
        // Run correlation!
        connection.voidEval("computeResult <- veupathUtils::correlation(data1=abundanceData" +
                                                            ", method=" + singleQuote(method) +
                                                            proportionNonZeroThresholdRParam +
                                                            varianceThresholdRParam +
                                                            stdDevThresholdRParam +
                                                            ", verbose=TRUE)");
      }

      // Write results
      String statsCmd = "veupathUtils::writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }

  @Override
  protected void execute() {

    CorrelationConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    if (computeConfig.isCorrelation2Collections()) {
      executeAssayAssay(computeConfig.getCorrelation2Collections(), util, metadata);
    } else if (computeConfig.isCorrelation1Collection()) {
      executeAssayMetadata(computeConfig.getCorrelation1Collection(), util, metadata);
    } else {
      throw new IllegalArgumentException("Invalid correlation configuration.");
    }
  }
}

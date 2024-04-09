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
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class CorrelationPlugin extends AbstractPlugin<CorrelationPluginRequest, CorrelationConfig> {
  private static final Logger LOG = LogManager.getLogger(CorrelationPlugin.class);

  private static final String INPUT_DATA = "inputData";
  private static final String INPUT_2_DATA = "input2Data";

  public CorrelationPlugin(@NotNull PluginContext<CorrelationPluginRequest, CorrelationConfig> context) {
    super(context);
  }

  private boolean isSameCollectionSpec(CollectionSpec spec1, CollectionSpec spec2) {
    if (spec1 == null && spec2 == null) {
      return true;
    }

    if (spec1 == null || spec2 == null) {
      return false;
    }
    
    String entity1Id = spec1.getEntityId();
    String entity2Id = spec2.getEntityId();
    String collection1Id = spec1.getCollectionId();
    String collection2Id = spec2.getCollectionId();

    return entity1Id.equals(entity2Id) && collection1Id.equals(collection2Id);
  }

  private boolean isValidDataConfig(CorrelationInputData data1, CorrelationInputData data2) {
    String data1Type = data1.getDataType().toString().toLowerCase();
    CollectionSpec data1Spec = data1.getCollectionSpec();
    if (data1Type.equals("collection") && data1Spec == null) {
      LOG.info("Received CorrelationInputData for data1 with a data type of 'collection' but no collection specification. This is not allowed.");
      return false;
    }

    String data2Type = data2.getDataType().toString().toLowerCase();
    CollectionSpec data2Spec = data2.getCollectionSpec();
    if (data2Type.equals("collection") && data2Spec == null) {
      LOG.info("Received CorrelationInputData for data2 with a data type of 'collection' but no collection specification. This is not allowed.");
      return false;
    }
    
    if (data1Type.equals("metadata") && data2Type.equals("metadata")) {
      LOG.info("Received CorrelationInputData for both data1 and data2 with a data type of 'metadata'. This is not allowed.");
      return false;
    }

    if (data1Type.equals("collection") && data2Type.equals("collection")) {
      if (isSameCollectionSpec(data1Spec, data2Spec)) {
        LOG.info("Received CorrelationInputData for both data1 and data2 with the same collection specification. This is not allowed.");
        return false;
      }
    }

    return true;
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

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    CorrelationConfig computeConfig = getConfig();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    if (!isValidDataConfig(computeConfig.getData1(), computeConfig.getData2())) {
      throw new IllegalArgumentException("Invalid data configuration. Any data type of `collection` must have a collection specification and data 1 and data 2 must not be the same.");
    }

    // once validated we know at most one of these is metadata
    // if one is metadata, we have only one collection
    boolean hasSecondCollection = 
      computeConfig.getData1().getDataType().toString().toLowerCase().equals("metadata") ||
      computeConfig.getData2().getDataType().toString().toLowerCase().equals("metadata") ?
      false : true;

    // again bc of validation we know one or both is collection
    // if both, take the first. otherwise take whichever is collection.
    CollectionSpec collection = 
    computeConfig.getData1().getDataType().toString().toLowerCase().equals("collection") ?
    computeConfig.getData1().getCollectionSpec() : computeConfig.getData2().getCollectionSpec();

    String entityId = collection.getEntityId();
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();

    if (hasSecondCollection) {
      // The collection x collection case. Both data types are "collection".
      // we also know if both are collection, that we grabbed the first one already

      CollectionSpec collection2 = computeConfig.getData2().getCollectionSpec();
      String entity2Id = collection2.getEntityId();
      EntityDef entity2 = metadata.getEntity(entity2Id).orElseThrow();

      // validate the collection variables are on the same entity or both are 1:1 with a shared parent entity
      if (!entityId.equals(entity2Id) &&
          !(metadata.getAncestors(entity).get(0).getId().equals(metadata.getAncestors(entity2).get(0).getId()) &&
            !entity.isManyToOneWithParent() && !entity2.isManyToOneWithParent())
      ) {
          throw new IllegalArgumentException("Collection variables must be on the same entity or both be 1:1 with a shared parent entity.");
      }

      return List.of(
        new StreamSpec(INPUT_DATA, entityId)
          .addVars(getUtil().getCollectionMembers(collection)),
        new StreamSpec(INPUT_2_DATA, entity2Id)
          .addVars(getUtil().getCollectionMembers(collection2))
      );
    } else {
      // The collection x metadata case. Either data1 is metadata or data2 is metadata.
      // either way, we find metadata based on the collection chosen

      // Filter metadata variables into only those that are appropriate for correlation
      List<VariableDef> metadataVariables = filterMetadataVariables(entity, metadata);

      return List.of(new StreamSpec(INPUT_DATA, entityId)
          .addVars(getUtil().getCollectionMembers(collection))
          .addVars(metadataVariables)
        );
    }
  }

  @Override
  protected void execute() {

    // Get basic parameters of the correlation computation
    CorrelationConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata metadata = getContext().getReferenceMetadata();

    String method = computeConfig.getCorrelationMethod().getValue();
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

    // once validated we know at most one of these is metadata
    // if one is metadata, we have only one collection
    boolean hasSecondCollection = 
      computeConfig.getData1().getDataType().toString().toLowerCase().equals("metadata") ||
      computeConfig.getData2().getDataType().toString().toLowerCase().equals("metadata") ?
      false : true;

    // Wrangle the (first) collection into helpful types
    // again bc of validation we know one or both is collection
    // if both, take the first. otherwise take whichever is collection.
    CollectionSpec collectionSpec = 
    computeConfig.getData1().getDataType().toString().toLowerCase().equals("collection") ?
    computeConfig.getData1().getCollectionSpec() : computeConfig.getData2().getCollectionSpec();
    String entityId = collectionSpec.getEntityId();
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    CollectionDef collection = metadata.getCollection(collectionSpec).orElseThrow(); 

    // Get record id columns for the (first) collection
    List<VariableDef> entityIdColumns = new ArrayList<>();
    for (EntityDef ancestor : metadata.getAncestors(entity)) {
      entityIdColumns.add(ancestor.getIdColumnDef());
    }

    // Get data stream(s) for Rserve
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    if (hasSecondCollection) {
      dataStream.put(INPUT_2_DATA, getWorkspace().openStream(INPUT_2_DATA));
    }

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the (first) collection data
      List<VariableSpec> collectionInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      collectionInputVars.addAll(util.getCollectionMembers(collection));
      collectionInputVars.addAll(entityIdColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, collectionInputVars));

      List<String> dotNotatedEntityIdColumns = entityIdColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedEntityIdColumnsString = util.listToRVector(dotNotatedEntityIdColumns);

      // NOTE: getMember tells us the member type, rather than gives us a literal member
      String collectionType = collection.getMember() == null ? "unknown" : collection.getMember();

      String dataClassRString = "microbiomeData::AbundanceData";
      if (collectionType.toLowerCase().contains("eigengene")) {
        dataClassRString = "veupathUtils::CollectionWithMetadata";
      }

      if (hasSecondCollection) {
        // The collection x collection case. Both data types are "collection".

        // Get the second collection
        CollectionSpec collection2Spec = computeConfig.getData2().getCollectionSpec();
        String entity2Id = collection2Spec.getEntityId();
        boolean isSameEntity = entityId.equals(entity2Id);
    
        // Wrangle into helpful types
        EntityDef entity2 = metadata.getEntity(entity2Id).orElseThrow();
        EntityDef parentEntity = metadata.getAncestors(entity).get(0);
        VariableDef entity2IdVarSpec = util.getEntityIdVarSpec(entity2Id);
        VariableDef parentEntityIdVarSpec = util.getEntityIdVarSpec(parentEntity.getId());
        VariableDef revisedComputeEntityIdVarSpec = isSameEntity ? computeEntityIdVarSpec : parentEntityIdVarSpec;
        String revisedComputeEntityIdColName = util.toColNameOrEmpty(revisedComputeEntityIdVarSpec);
        CollectionDef collection2 = metadata.getCollection(collection2Spec).orElseThrow();

        List<VariableDef> entity2IdColumns = new ArrayList<>();
        for (EntityDef ancestor : metadata.getAncestors(entity2)) {
          entity2IdColumns.add(ancestor.getIdColumnDef());
        }
        // if were not on the same entity, we can remove the collection entity id from the id columns
        if (!isSameEntity) {
          entityIdColumns.remove(revisedComputeEntityIdVarSpec);
          entity2IdColumns.remove(entity2IdVarSpec);
        }

        // read both sets of collection data into R
        connection.voidEval("collectionData <- " + INPUT_DATA);
        List<VariableSpec> collection2InputVars = ListBuilder.asList(revisedComputeEntityIdVarSpec);
        collection2InputVars.addAll(util.getCollectionMembers(collection2));
        collection2InputVars.addAll(entity2IdColumns);
        connection.voidEval(util.getVoidEvalFreadCommand(INPUT_2_DATA, collection2InputVars));
        connection.voidEval("collection2Data <- " + INPUT_2_DATA);

        List<String> dotNotatedEntity2IdColumns = entity2IdColumns.stream().map(VariableDef::toDotNotation).toList();
        String dotNotatedEntity2IdColumnsString = util.listToRVector(dotNotatedEntity2IdColumns);

        String collection2MemberType = collection2.getMember() == null ? "unknown" : collection2.getMember();
        String data2ClassRString = collection2MemberType.toLowerCase().contains("eigengene") ? "veupathUtils::CollectionWithMetadata" : "microbiomeData::AbundanceData";

        connection.voidEval("data1 <- " + dataClassRString + "(name= " + singleQuote(collectionType) + ",data=collectionData" + 
                                    ", recordIdColumn=" + singleQuote(revisedComputeEntityIdColName) +
                                    ", ancestorIdColumns=as.character(" + dotNotatedEntityIdColumnsString + ")" +
                                    ", imputeZero=TRUE)");
      
        connection.voidEval("data2 <- " + data2ClassRString + "(name= " + singleQuote(collection2MemberType) + ",data = collection2Data" +
                                    ", recordIdColumn=" + singleQuote(revisedComputeEntityIdColName) +
                                    ", ancestorIdColumns=as.character(" + dotNotatedEntity2IdColumnsString + ")" +
                                    ", imputeZero=TRUE)");

        // Run correlation!
        connection.voidEval("computeResult <- veupathUtils::correlation(data1=data1, data2=data2" +
                                                              ", method=" + singleQuote(method) +
                                                              proportionNonZeroThresholdRParam +
                                                              varianceThresholdRParam +
                                                              stdDevThresholdRParam +
                                                              ", verbose=TRUE)");

      } else {
        // This is the collection x Metadata case. The second data type is metadata.

        // Filter metadata variables into only those that are appropriate for correlation.
        List<VariableDef> metadataVariables = filterMetadataVariables(entity, metadata);

        LOG.info("Using the following metadata variables for correlation: {}",
          JsonUtil.serializeObject(metadataVariables));

        connection.voidEval("collectionData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

        // Read in the sample metadata
        List<VariableSpec> metadataInputVars = ListBuilder.asList(computeEntityIdVarSpec);
        metadataInputVars.addAll(metadataVariables);
        metadataInputVars.addAll(entityIdColumns);
        connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, metadataInputVars));
        connection.voidEval("sampleMetadata <- " + INPUT_DATA); 

        connection.voidEval("sampleMetadata <- microbiomeData::SampleMetadata(data = sampleMetadata" +
                                    ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                    ", ancestorIdColumns=as.character(" + dotNotatedEntityIdColumnsString + "))");

        connection.voidEval("abundanceData <- " + dataClassRString + "(name= " + singleQuote(collectionType) + ",data=collectionData" + 
                                    ", sampleMetadata=sampleMetadata" +
                                    ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                    ", ancestorIdColumns=as.character(" + dotNotatedEntityIdColumnsString + ")" +
                                    ", imputeZero=TRUE)");       
                                  
        // Run correlation!
        String metadataIsFirst = computeConfig.getData1().getDataType().toString().toLowerCase().equals("metadata") ? "TRUE" : "FALSE";
        connection.voidEval("computeResult <- veupathUtils::correlation(data1=abundanceData, NULL," +
                                                              ", method=" + singleQuote(method) +
                                                              proportionNonZeroThresholdRParam +
                                                              varianceThresholdRParam +
                                                              stdDevThresholdRParam +
                                                              ", metadataIsFirst=" + metadataIsFirst +
                                                              ", verbose=TRUE)");
        
      }

      // Write results
      String statsCmd = "veupathUtils::writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
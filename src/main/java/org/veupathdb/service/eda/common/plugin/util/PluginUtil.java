package org.veupathdb.service.eda.common.plugin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.common.client.EdaMergingClient;
import org.veupathdb.service.eda.common.model.CollectionDef;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.CollectionSpec;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Shared plugin utilities
 */
public class PluginUtil {

  private static final Logger LOG = LogManager.getLogger(PluginUtil.class);

  private final ReferenceMetadata _metadata;
  private final EdaMergingClient _mergingClient;

  public PluginUtil(ReferenceMetadata metadata, EdaMergingClient mergingClient) {
    _metadata = metadata;
    _mergingClient = mergingClient;
  }

  /*****************************************************************
   *** Metadata access utilities
   ****************************************************************/

  // do the same w CollectionSpec as arg

  public VariableDef getEntityIdVarSpec(String entityId) {
    return _metadata.getEntity(entityId).orElseThrow().getIdColumnDef();
  }

  public VariableSpec getVariableSpecFromList(List<VariableSpec> vars, int index) {
    return vars == null || vars.size() <= index ? null : vars.get(index);
  }

  public String getVariableEntityId(VariableSpec var) {
    return var == null ? null : var.getEntityId();
  }

  public String getVariableEntityId(List<VariableSpec> vars, int index) {
    return getVariableEntityId(getVariableSpecFromList(vars, index));
  }

  private String getCollectionAttribute(Function<CollectionDef, ?> getter, CollectionSpec collection) {
    return collection == null ? "" : getter.apply(_metadata.getCollection(collection).orElseThrow()).toString();
  }

  private String getVariableAttribute(Function<VariableDef, ?> getter, VariableSpec var) {
    return var == null ? "" : getter.apply(_metadata.getVariable(var).orElseThrow()).toString();
  }

  public String getCollectionType(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::getDataType, collection);
  }

  public String getVariableType(VariableSpec var) {
    return getVariableAttribute(VariableDef::getType, var);
  }

  public String getVariableType(List<VariableSpec> vars, int index) {
    return getVariableType(getVariableSpecFromList(vars, index));
  }

  public String getCollectionDataShape(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::getDataShape, collection);
  }

  public String getVariableDataShape(VariableSpec var) {
    return getVariableAttribute(VariableDef::getDataShape, var);
  }

  public String getVariableDataShape(List<VariableSpec> vars, int index) {
    return getVariableDataShape(getVariableSpecFromList(vars, index));
  }

  public String getCollectionImputeZero(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::isImputeZero, collection);
  }

  public String getVariableImputeZero(VariableSpec var) {
    return getVariableAttribute(VariableDef::isImputeZero, var);
  }

  public String getVariableImputeZero(List<VariableSpec> vars, int index) {
    return getVariableImputeZero(getVariableSpecFromList(vars, index));
  }

  public String getHasStudyDependentVocabulary(VariableSpec var) {
    return getVariableAttribute(VariableDef::hasStudyDependentVocabulary, var);
  }

  public String getHasStudyDependentVocabulary(List<VariableSpec> vars, int index) {
    return getHasStudyDependentVocabulary(getVariableSpecFromList(vars, index));
  }

  public List<VariableDef> getCollectionMembers(CollectionSpec collection) {
    return collection == null ? null : _metadata.getCollection(collection).orElseThrow().getMemberVariables();
  }

  //deprecated
  public List<VariableDef> getChildrenVariables(VariableSpec collectionVar) {
    EntityDef collectionVarEntityDef = _metadata.getEntity(collectionVar.getEntityId()).orElseThrow();
    TreeNode<VariableDef> childVarsTree = collectionVarEntityDef.getNativeVariableTreeNode(collectionVar);
    // TODO: for now assume we only have leaves as children; revisit if that turns out to not be true
    return childVarsTree.findAndMap(TreeNode::isLeaf, v -> true, v -> v);
  }


  /*****************************************************************
   *** Utilities to ease calls to RServe
   ****************************************************************/

  public String toColNameOrEmpty(VariableSpec var) {
    return var == null ? "" : _mergingClient.varToColumnHeader(var);
  }

  public String toColNameOrEmpty(List<VariableSpec> vars, int index) {
    VariableSpec var = getVariableSpecFromList(vars, index);
    return toColNameOrEmpty(var);
  }

  public static String singleQuote(String unquotedString) {
    return "'" + unquotedString + "'";
  }

  public static String doubleQuote(String unquotedString) {
    return "\"" + unquotedString + "\"";
  }

  public static String listToRVector(List<String> values) {
    return
        "c(" +
        values.stream()
            .map(PluginUtil::doubleQuote)
            .collect(Collectors.joining(", ")) +
        ")";
  }

  public String getVoidEvalFreadCommand(String fileName, VariableSpec... vars) {
    return getVoidEvalFreadCommand(fileName, new ListBuilder<VariableSpec>().addAll(vars).toList());
  }

  public String getVoidEvalFreadCommand(String fileName, List<VariableSpec> vars) {
    boolean first = true;
    String namedTypes = "";

    for(VariableSpec var : vars) {
      String varName = toColNameOrEmpty(var);
      if (varName.equals("")) continue;
      String varType = getVariableType(var);
      String varShape = getVariableDataShape(var);
      String rBaseType;
      if (varType.equals("INTEGER")) {
        rBaseType = "integer";
      } else if (varShape.equals("CONTINUOUS") & !varType.equals("DATE")) {
        rBaseType = "double";
      } else {
        rBaseType = "character";
      }
      if (first) {
        first = false;
        namedTypes = singleQuote(varName) + "=" + singleQuote(rBaseType);
      } else {
        namedTypes = namedTypes + "," + singleQuote(varName) + "=" + singleQuote(rBaseType);
      }
    }

    return fileName +
        " <- fread(" + singleQuote(fileName) +
        ", select=c(" + namedTypes + ")" +
        ", na.strings=c(''))";
  }

  public String getDotNotatedIdColumnsAsRVectorString(List<String> dotNotatedIdColumns) {
    String dotNotatedIdColumnsString = "c(";
      boolean first = true;
      for (String idCol : dotNotatedIdColumns) {
        if (first) {
          first = false;
          dotNotatedIdColumnsString = dotNotatedIdColumnsString + singleQuote(idCol);
        } else {
          dotNotatedIdColumnsString = dotNotatedIdColumnsString + "," + singleQuote(idCol);
        }
      }
      dotNotatedIdColumnsString = dotNotatedIdColumnsString + ")";

    return dotNotatedIdColumnsString;
  }
  
  public String getIdColumnSpecsAsRVectorString(List<VariableSpec> idColumnSpecs) {
    List<String> dotNotatedIdColumns = idColumnSpecs.stream().map(VariableDef::toDotNotation).toList();
  
    return getDotNotatedIdColumnsAsRVectorString(dotNotatedIdColumns);
  }

  // this feels like it should be unnecessary.. what am i not getting?
  public String getIdColumnDefsAsRVectorString(List<VariableDef> idColumnDefs) {
    List<String> dotNotatedIdColumns = idColumnDefs.stream().map(VariableDef::toDotNotation).toList();
  
    return getDotNotatedIdColumnsAsRVectorString(dotNotatedIdColumns);
  }

  public String getEntityAncestorsAsRVectorString(EntityDef entity, ReferenceMetadata meta) {
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    return getIdColumnDefsAsRVectorString(idColumns);
  }

  public String getEntityAncestorsAsRVectorString(String entityId, ReferenceMetadata meta) {
    EntityDef entity = meta.getEntity(entityId).orElseThrow();

    return getEntityAncestorsAsRVectorString(entity, meta);
  }

  // TODO could be better named since this only deals w labels
  public String getRBinListAsString(List<String> labels) {
    String rBinList = "veupathUtils::BinList(S4Vectors::SimpleList(";

    boolean first = true;
    for (int i = 0; i < labels.size(); i++) {
      String rBin = "veupathUtils::Bin(binLabel='" + labels.get(i) + "'";
      rBin += ")";

      if (first) {
        rBinList += rBin;
        first = false;
      } else {
        rBinList += "," + rBin;
      }
    }

    return rBinList + "))";
  }
}

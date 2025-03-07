package org.veupathdb.service.eda.common.plugin.util;

import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.common.model.CollectionDef;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Shared plugin utilities
 */
public class PluginUtil {

  private final ReferenceMetadata _metadata;
  private final Function<VariableSpec, String> _varToColumnHeader;

  public PluginUtil(ReferenceMetadata metadata, Function<VariableSpec, String> varToColumnHeader) {
    _metadata = metadata;
    _varToColumnHeader = varToColumnHeader;
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
    return var == null ? "" : getter.apply(_metadata.getVariable(var)
      .orElseThrow(() -> new IllegalArgumentException(
        String.format("Unable to find variable with spec %s, %s", var.getEntityId(), var.getVariableId())))).toString();
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

  public boolean getCollectionIsCompositional(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::isCompositional, collection).equals("true");
  }

  public boolean getCollectionIsProportion(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::isProportion, collection).equals("true");
  }

  public String getCollectionNormalizationMethod(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::getNormalizationMethod, collection);
  }

  public String getVariableImputeZero(VariableSpec var) {
    return getVariableAttribute(VariableDef::isImputeZero, var);
  }

  public String getVariableImputeZero(List<VariableSpec> vars, int index) {
    return getVariableImputeZero(getVariableSpecFromList(vars, index));
  }

  public VariableSpec getVariableSpecToImputeZeroesFor(VariableSpec var) {
    VariableDef varDef = _metadata.getVariable(var).orElseThrow();
    return varDef.getVariableSpecToImputeZeroesFor();
  }

  public VariableSpec getVariableSpecToImputeZeroesFor(List<VariableSpec> vars, int index) {
    return getVariableSpecToImputeZeroesFor(getVariableSpecFromList(vars, index));
  }

  public boolean getHasStudyDependentVocabulary(VariableSpec var) {
    return getVariableAttribute(VariableDef::getHasStudyDependentVocabulary, var).equals("true");
  }

  public boolean getHasStudyDependentVocabulary(List<VariableSpec> vars, int index) {
    return getHasStudyDependentVocabulary(getVariableSpecFromList(vars, index));
  }

  public boolean getHasStudyDependentVocabulary(CollectionSpec collection) {
    return getCollectionAttribute(CollectionDef::getHasStudyDependentVocabulary, collection).equals("true");
  }

  public List<VariableDef> getCollectionMembers(CollectionSpec collection) {
    return collection == null ? null : _metadata.getCollection(collection).orElseThrow().getMemberVariables();
  }

  public List<String> getCollectionVocabulary(CollectionSpec collection) {
    return collection == null ? null : _metadata.getCollection(collection).orElseThrow().getVocabulary();

  }

  public static List<VariableSpec> variablesFromCollectionMembers(CollectionSpec collection, List<String> memberIds) {
    return memberIds.stream()
      .map(memberVarId -> {
        VariableSpec varSpec = new VariableSpecImpl();
        varSpec.setEntityId(collection.getEntityId());
        varSpec.setVariableId(memberVarId);
        return varSpec;
      }).toList();
  }

  //deprecated
  public List<VariableDef> getChildrenVariables(VariableSpec collectionVar) {
    EntityDef collectionVarEntityDef = _metadata.getEntity(collectionVar.getEntityId()).orElseThrow();
    TreeNode<VariableDef> childVarsTree = collectionVarEntityDef.getNativeVariableTreeNode(collectionVar);
    // TODO: for now assume we only have leaves as children; revisit if that turns out to not be true
    return childVarsTree.findAndMap(TreeNode::isLeaf, v -> true, v -> v);
  }

  public List<String> getVocabulary(VariableSpec var) {
    return var == null ? null :_metadata.getVariable(var).map(VariableDef::getVocabulary).orElseThrow();
  }

  /*****************************************************************
   *** Utilities to ease calls to RServe
   ****************************************************************/

  public String toColNameOrEmpty(VariableSpec var) {
    return var == null ? "" : _varToColumnHeader.apply(var);
  }

  public String toColNameOrEmpty(DynamicDataSpec data) {
    if (data.isCollectionSpec()) {
      // TODO and when we get there it might need an option to return either the dot notated collection spec directly vs a list of dot notated member var specs
      return "";
    } else if (data.isVariableSpec()) {
      return toColNameOrEmpty(data.getVariableSpec());
    } else {
      return "";
    }
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
    return getVoidEvalFreadCommand(fileName, Arrays.asList(vars));
  }

  public String getVoidEvalFreadCommand(String fileName, List<VariableSpec> vars) {
    boolean       first      = true;
    StringBuilder namedTypes = new StringBuilder();

    for(VariableSpec var : vars) {
      String varName = toColNameOrEmpty(var);
      if (varName.isEmpty()) continue;
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
        namedTypes = new StringBuilder(singleQuote(varName) + "=" + singleQuote(rBaseType));
      } else {
        namedTypes.append(",").append(singleQuote(varName)).append("=").append(singleQuote(rBaseType));
      }
    }

    return fileName +
      " <- data.table::fread(" + singleQuote(fileName) +
      ", select=c(" + namedTypes + ")" +
      ", na.strings=c(''))";
  }

  public String getDotNotatedIdColumnsAsRVectorString(List<String> dotNotatedIdColumns) {
    StringBuilder dotNotatedIdColumnsString = new StringBuilder("c(");
    boolean first = true;
    for (String idCol : dotNotatedIdColumns) {
      if (first) {
        first = false;
        dotNotatedIdColumnsString.append(singleQuote(idCol));
      } else {
        dotNotatedIdColumnsString.append(",").append(singleQuote(idCol));
      }
    }
    dotNotatedIdColumnsString.append(")");

    return dotNotatedIdColumnsString.toString();
  }

  public String getIdColumnSpecsAsRVectorString(List<VariableSpec> idColumnSpecs) {
    List<String> dotNotatedIdColumns = idColumnSpecs.stream().map(VariableDef::toDotNotation).toList();

    return getDotNotatedIdColumnsAsRVectorString(dotNotatedIdColumns);
  }

  public String getIdColumnDefsAsRVectorString(List<VariableDef> idColumnDefs) {
    List<String> dotNotatedIdColumns = idColumnDefs.stream().map(VariableDef::toDotNotation).toList();

    return getDotNotatedIdColumnsAsRVectorString(dotNotatedIdColumns);
  }

  public String getEntityAncestorsAsRVectorString(EntityDef entity, ReferenceMetadata meta) {
    return getEntityAncestorsAsRVectorString(entity, meta, false);
  }

  public String getEntityAncestorsAsRVectorString(String entityId, ReferenceMetadata meta) {
    EntityDef entity = meta.getEntity(entityId).orElseThrow();

    return getEntityAncestorsAsRVectorString(entity, meta);
  }

  public String getEntityAncestorsAsRVectorString(String entityId, ReferenceMetadata meta, boolean includeSelf) {
    EntityDef entity = meta.getEntity(entityId).orElseThrow();

    return getEntityAncestorsAsRVectorString(entity, meta, includeSelf);
  }

  public String getEntityAncestorsAsRVectorString(EntityDef entity, ReferenceMetadata meta, boolean includeSelf) {
    if (entity == null) {
      return "c()";
    }
    List<VariableDef> idColumns = new ArrayList<>();
    if (includeSelf) {
      idColumns.add(entity.getIdColumnDef());
    }
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    return getIdColumnDefsAsRVectorString(idColumns);
  }

  public String getRCategoricalBinListAsString(List<String> labels) {
    StringBuilder rBinList = new StringBuilder("veupathUtils::BinList(S4Vectors::SimpleList(");

    boolean first = true;
    for (String label : labels) {
      String rBin = "veupathUtils::Bin(binLabel='" + label + "'";
      rBin += ")";

      if (first) {
        rBinList.append(rBin);
        first = false;
      } else {
        rBinList.append(",").append(rBin);
      }
    }

    return rBinList + "))";
  }

  // Maps ranges in a LabeledRange to bins in an R veupathUtils::BinList object. Returns
  // a string that should be evaluated in R.
  public String getRBinListAsString(List<LabeledRange> labelledRangeList) {

    StringBuilder rString = new StringBuilder("veupathUtils::BinList(S4Vectors::SimpleList(");

    boolean first = true;
    for (LabeledRange labeledRange : labelledRangeList) {
      String rBin = "veupathUtils::Bin(binLabel='" + labeledRange.getLabel() + "'";

      // All bins in R BinList objects must have labels, but not necessarily bin starts and ends
      if (labeledRange.getMin() != null) {
        rBin += ",binStart=" + labeledRange.getMin() +
          ",binEnd=" + labeledRange.getMax();
      }
      rBin += ")";

      if (first) {
        rString.append(rBin);
        first = false;
      } else {
        rString.append(",").append(rBin);
      }
    }

    rString.append("))");

    return rString.toString();

  }

}

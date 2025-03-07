package org.veupathdb.service.eda.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.functional.TreeNode;
import org.json.JSONObject;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.CollectionSpec;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpecImpl;

/**
 * Encapsulates entity information and holds VariableDefs representing variables of all data types, computed vars, and
 * derived vars.  Provides methods to interrogate the data at an entity level.
 */
public class EntityDef {

  private static final Logger LOG = LogManager.getLogger(EntityDef.class);

  private final String _id;
  private final String _displayName;
  private final Boolean _isManyToOneWithParent;
  private final VariableDef _idColumnDef;

  // Use linked hash map to maintain ordering in which entries are inserted.
  private final LinkedHashMap<EntityChildUniquenessKey, VariableDef> _variables;
  private final LinkedHashMap<EntityChildUniquenessKey, VariableDef> _categories;
  private final LinkedHashMap<EntityChildUniquenessKey, CollectionDef> _collections;

  public EntityDef(String id, String displayName, String idColumnName, Boolean isManyToOneWithParent) {
    _id = id;
    _displayName = displayName;
    _idColumnDef = new VariableDef(_id, idColumnName, APIVariableType.STRING,
      APIVariableDataShape.CONTINUOUS, false, false, Optional.empty(), Optional.empty(), null, null, false, null, VariableSource.ID);
    _isManyToOneWithParent = isManyToOneWithParent;
    _variables = new LinkedHashMap<>();
    _variables.put(new EntityChildUniquenessKey(_idColumnDef.getEntityId(), _idColumnDef.getVariableId()), _idColumnDef);
    _categories = new LinkedHashMap<>();
    _collections = new LinkedHashMap<>();
  }

  public String getId() {
    return _id;
  }

  public VariableDef getIdColumnDef() {
    return _idColumnDef;
  }

  public Boolean isManyToOneWithParent() {
    return _isManyToOneWithParent;
  }

  public Collection<VariableDef> getVariables() {
    return _variables.values();
  }

  public Collection<CollectionDef> getCollections() {
    return _collections.values();
  }

  public Optional<VariableDef> getVariable(VariableSpec var) {
    return _variables.values().stream()
      .filter(v -> VariableDef.isSameVariable(v, var))
      .findFirst();
  }

  public Optional<CollectionDef> getCollection(CollectionSpec colSpec) {
    if (!colSpec.getEntityId().equals(_id)) {
      return Optional.empty();
    }
    return _collections.values().stream()
      .filter(c -> c.getCollectionId().equals(colSpec.getCollectionId()))
      .findFirst();
  }

  public TreeNode<VariableDef> getNativeVariableTreeNode(VariableSpec varSpec) {
    return Optional.ofNullable(
      getNativeVariableTree().findFirst(var -> VariableDef.isSameVariable(var, varSpec))
    ).orElseThrow(() ->
      new IllegalArgumentException("Variable tree requested on wrong entity or for non-native var.")
    );
  }

  public TreeNode<VariableDef> getNativeVariableTree() {

    // add only native vars (not IDs or inherited or derived vars)
    Map<String, TreeNode<VariableDef>> allVarNodes = _variables.values().stream()
      .filter(var -> var.getSource() == VariableSource.NATIVE)
      .collect(Collectors.toMap(
        VariableSpecImpl::getVariableId,
        TreeNode::new
      ));

    // add categories for proper tree structure
    _categories.values().forEach(cat -> allVarNodes.put(cat.getVariableId(), new TreeNode<>(cat)));

    List<TreeNode<VariableDef>> parentlessNodes = new ArrayList<>();
    for (TreeNode<VariableDef> varNode : allVarNodes.values()) {
      VariableDef var = varNode.getContents();
      String parentId = var.getParentId();
      if (parentId == null) {
        parentlessNodes.add(varNode);
      }
      else {
        TreeNode<VariableDef> parentNode = allVarNodes.get(parentId);
        if (parentNode == null) {
          // This can happen legally if parentId = this entity's ID OR if repeated measure is false,
          //   it can even be the ID of this entity's parent entity (!!).  For our purposes, any
          //   non-variable ID is not useful; treat as if parent is null and warn for debug purposes.
          LOG.warn("Variable {} contains parentId '{}' that does not map to a variable in this entity's tree.", var, parentId);
          parentlessNodes.add(varNode);
        }
        else {
          parentNode.addChildNode(varNode);
        }
      }
    }
    switch(parentlessNodes.size()) {
      case 0: throw new RuntimeException("Found no native vars in entity " +
        getId() + " with no parentId specified (illegal variable tree)");
      case 1: return parentlessNodes.getFirst();
      default:
        // create a dummy root containing all (>1) parentless nodes
        TreeNode<VariableDef> dummyRoot = new TreeNode<>(new VariableDef(
          "dummyRoot", "dummyRoot", APIVariableType.STRING, APIVariableDataShape.CATEGORICAL,
          false, false, Optional.empty(), Optional.empty(), null, null, false, null, VariableSource.NATIVE
        ));
        dummyRoot.addAllChildNodes(parentlessNodes);
        return dummyRoot;
    }
  }

  @Override
  public String toString() {
    return new JSONObject()
      .put("id", _id)
      .put("displayName", _displayName)
      .put("idColumnName", _idColumnDef.getVariableId())
      .put("variables", _variables.values().stream()
        .map(var -> VariableDef.toDotNotation(var) + ":" + var.getType().toString().toLowerCase())
        .collect(Collectors.toList()))
      .toString(2);
  }

  public void addVariable(VariableDef variable) {
    addIfUnique(_variables, EntityChildUniquenessKey::fromVar, variable);
  }

  public void addCategory(VariableDef category) {
    addIfUnique(_categories, EntityChildUniquenessKey::fromVar, category);
  }

  public void addCollection(CollectionDef collection) {
    addIfUnique(_collections, EntityChildUniquenessKey::fromCollection, collection);
  }

  private <T> void addIfUnique(LinkedHashMap<EntityChildUniquenessKey, T> entityChildHolder,
                               Function<T, EntityChildUniquenessKey> uniquenessKeyConstructor,
                               T child) {
    T existingItem = entityChildHolder.put(uniquenessKeyConstructor.apply(child), child);
    if (existingItem != null) {
      throw new BadRequestException("Tried to add element " + existingItem + " to entity def with name that already exists.");
    }
  }

  private record EntityChildUniquenessKey(String entityId, String childId) {
    static EntityChildUniquenessKey fromVar(VariableDef variableDef) {
      return new EntityChildUniquenessKey(variableDef.getEntityId(), variableDef.getVariableId());
    }

    static EntityChildUniquenessKey fromCollection(CollectionDef collectionDef) {
      return new EntityChildUniquenessKey(collectionDef.getEntityId(), collectionDef.getCollectionId());
    }
  }
}

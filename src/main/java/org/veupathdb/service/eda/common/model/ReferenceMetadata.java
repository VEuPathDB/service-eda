package org.veupathdb.service.eda.common.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.common.derivedvars.plugin.DerivedVariable;
import org.veupathdb.service.eda.common.derivedvars.DerivedVariableFactory;
import org.veupathdb.service.eda.generated.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.gusdb.fgputil.functional.Functions.getMapFromList;

public class ReferenceMetadata {

  private static final Logger LOG = LogManager.getLogger(ReferenceMetadata.class);

  private final String _studyId;
  private final TreeNode<EntityDef> _entityTree;
  private final Map<String,EntityDef> _entityMap;
  private final List<DerivedVariableSpec> _derivedVariableSpecs;
  private final List<DerivedVariable> _derivedVariables;

  public ReferenceMetadata(APIStudyDetail study, List<DerivedVariableSpec> derivedVariableSpecs) {
    _studyId = study.getId();
    _entityTree = buildEntityTree(study.getRootEntity(), new ArrayList<>());
    _entityMap = buildEntityMap(_entityTree);
    _derivedVariableSpecs = derivedVariableSpecs;
    _derivedVariables = new DerivedVariableFactory(this).createDerivedVariables(derivedVariableSpecs);
    incorporateDerivedVariables(_derivedVariables);
  }

  // note: incoming list will be in dependency order; i.e. only later derived vars
  //       will depend on earlier derived vars (plus no circular dependencies);
  //       name will also be validated for uniqueness within study
  private void incorporateDerivedVariables(List<DerivedVariable> derivedVariables) {
    // add derived variables for this entity to itself and all children (who can inherit the derived var)
    for (DerivedVariable derivedVariable: derivedVariables) {
      // get this DR's entity and descendents
      List<EntityDef> entities = new ArrayList<>();
      entities.add(derivedVariable.getEntity());
      entities.addAll(getDescendants(derivedVariable.getEntity()));
      for (EntityDef entity : entities) {
        entity.add(new VariableDef(
            derivedVariable.getEntity().getId(),
            derivedVariable.getVariableId(),
            derivedVariable.getVariableType(),
            derivedVariable.getVariableDataShape(),
            false,
            false,
            derivedVariable.getDataRanges(),
            null,
            entity == derivedVariable.getEntity()
              ? VariableSource.DERIVED
              : VariableSource.INHERITED));
      }
    }
  }

  private static Map<String, EntityDef> buildEntityMap(TreeNode<EntityDef> entityTree) {
    return getMapFromList(
      entityTree.findAll(e -> true),
      n -> new TwoTuple<>(n.getContents().getId(), n.getContents()));
  }

  /**
   * Creates a single entity def node from the passed entity, with all its
   * available variables (native, inherited, and derived).  Then recursively
   * calls itself on the entity's children to produce child nodes.
   *
   * @param entity an entity to convert to a def node, supplement vars of, and convert children of
   * @param ancestorVars vars supplied by ancestor entities
   * @return root of a tree of entity defs containing all available vars on those entities
   */
  private static TreeNode<EntityDef> buildEntityTree(APIEntity entity, List<VariableDef> ancestorVars) {

    EntityDef entityDef = new EntityDef(
        entity.getId(),
        entity.getDisplayName(),
        entity.getIdColumnName()
    );

    entity.getCollections().stream().map(col ->
        new CollectionDef(
            entityDef,
            col.getId(),
            col.getDisplayName(),
            col.getType(),
            col.getDataShape(),
            col.getImputeZero(),
            col.getDistinctValuesCount(),
            col.getVocabulary(),
            col.getMemberVariableIds(),
            DataRanges.getDataRanges(col)
        )
    ).forEach(colDef -> entityDef.addCollection(colDef));

    // add inherited variables from parent
    ancestorVars.stream()
      .forEach(vd -> entityDef.add(new VariableDef(
          vd.getEntityId(),
          vd.getVariableId(),
          vd.getType(),
          vd.getDataShape(),
          vd.isMultiValue(),
          vd.isImputeZero(),
          vd.getDataRanges(),
          vd.getParentId(),
          VariableSource.INHERITED)));

    // process category vars (may still have children!)
    entity.getVariables().stream()
      .filter(var -> var.getType().equals(APIVariableType.CATEGORY))
      .map(var -> (APIVariablesCategory)var)
      .map(var -> new VariableDef(
          entity.getId(),
          var.getId(),
          APIVariableType.CATEGORY,
          null,
          false,
          false,
          Optional.empty(),
          var.getParentId(),
          VariableSource.NATIVE))
      .forEach(cat -> {
        // add category vars for this entity
        entityDef.addCategory(cat);
      });

    // process this entity's native vars
    entity.getVariables().stream()
      .filter(var -> !var.getType().equals(APIVariableType.CATEGORY))
      .map(var -> (APIVariableWithValues)var)
      .map(var -> new VariableDef(
          entity.getId(),
          var.getId(),
          var.getType(),
          var.getDataShape(),
          var.getIsMultiValued(),
          var.getImputeZero(),
          DataRanges.getDataRanges(var),
          var.getParentId(),
          VariableSource.NATIVE))
      .forEach(vd -> {
        // add variables for this entity
        entityDef.add(vd);

        // add this entity's native vars to ancestorVars list (copy will be passed to children)
        ancestorVars.add(vd);
      });

    // put this entity in a node
    TreeNode<EntityDef> node = new TreeNode<>(entityDef);

    // add child entities
    for (APIEntity childEntity : entity.getChildren()) {
      // create new array list each time; don't want branches of entity tree polluting each other
      node.addChildNode(buildEntityTree(childEntity, new ArrayList<>(ancestorVars)));
    }

    return node;
  }

  public String getStudyId() {
    return _studyId;
  }

  public Optional<EntityDef> getEntity(String entityId) {
    return Optional.ofNullable(_entityMap.get(entityId));
  }

  public Optional<VariableDef> getVariable(VariableSpec varSpec) {
    return getEntity(varSpec.getEntityId()).flatMap(e -> e.getVariable(varSpec));
  }

  public Optional<CollectionDef> getCollection(CollectionSpec colSpec) {
    return getEntity(colSpec.getEntityId()).flatMap(e -> e.getCollection(colSpec));
  }

  public List<DerivedVariableSpec> getDerivedVariableSpecs() {
    return _derivedVariableSpecs;
  }

  public Optional<DerivedVariable> findDerivedVariable(VariableSpec var) {
    return _derivedVariables.stream().filter(dr -> VariableDef.isSameVariable(dr, var)).findFirst();
  }

  /**
   * Returns the child entities of the passed entity
   *
   * @param targetEntity
   * @return
   */
  public List<EntityDef> getChildren(EntityDef targetEntity) {
    return _entityTree
        .findFirst(node -> node.getId().equals(targetEntity.getId()))
        .getChildNodes()
        .stream()
        .map(node -> node.getContents())
        .collect(Collectors.toList());
  }

  public List<EntityDef> getDescendants(EntityDef targetEntity) {
    return _entityTree
        .findFirst(node -> node.getId().equals(targetEntity.getId()))
        // find all nodes in this subtree except the root
        .findAll(entity -> !entity.getId().equals(targetEntity.getId()))
        .stream()
        .map(node -> node.getContents())
        .collect(Collectors.toList());
  }

  /**
   * Returns the ancestor entities of the passed entity, ordered from the bottom (parent) up to the root
   *
   * @param targetEntity
   * @return
   */
  public List<EntityDef> getAncestors(EntityDef targetEntity) {
    return getAncestors(targetEntity, _entityTree, new ArrayList<>())
        .orElseThrow(() -> new RuntimeException(
            "Target entity '" + targetEntity.getId() +
            "' could not be found in entity tree."));
  }

  public List<String> getTabularIdColumns(EntityDef targetEntity) {
    return new ListBuilder<String>()
        .add(targetEntity.getIdColumnDef().getVariableId())
        .addAll(getAncestors(targetEntity).stream()
            .map(entity -> entity.getIdColumnDef().getVariableId())
            .collect(Collectors.toList()))
        .toList();
  }

  private static Optional<List<EntityDef>> getAncestors(EntityDef targetEntity, TreeNode<EntityDef> entityTree, List<EntityDef> ancestors) {
    if (entityTree.getContents().getId().equals(targetEntity.getId())) {
      return Optional.of(ancestors); // entity found
    }
    for (TreeNode<EntityDef> child : entityTree.getChildNodes()) {
      List<EntityDef> supplementedAncestors = new ArrayList<>(ancestors);
      supplementedAncestors.add(0, entityTree.getContents()); // in ascending order (up the tree)
      Optional<List<EntityDef>> listForFoundEntity = getAncestors(targetEntity, child, supplementedAncestors);
      if (listForFoundEntity.isPresent()) {
        // entity found in this branch
        return listForFoundEntity;
      }
    }
    // entity not found in this branch
    return Optional.empty();
  }

  public List<VariableDef> getTabularColumns(EntityDef targetEntity, List<VariableSpec> requestedVars) {

    List<VariableDef> columns = new ArrayList<>();

    // first column is the ID col for the returned entity
    columns.add(targetEntity.getIdColumnDef());

    // next cols are the ID cols for ancestor entities (up the tree)
    for (EntityDef ancestor : getAncestors(targetEntity)) {
      columns.add(ancestor.getIdColumnDef());
    }

    // then add requested vars in the order requested
    for (VariableSpec requestedVar : requestedVars) {
      columns.add(getVariable(requestedVar).orElseThrow());
    }

    return columns;
  }
}

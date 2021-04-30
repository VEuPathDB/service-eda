package org.veupathdb.service.eda.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.generated.model.APIEntity;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.DerivedVariable;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import static org.gusdb.fgputil.functional.Functions.getMapFromList;

public class ReferenceMetadata {

  private static final Logger LOG = LogManager.getLogger(ReferenceMetadata.class);

  private final String _studyId;
  private final TreeNode<EntityDef> _entityTree;
  private final Map<String,EntityDef> _entityMap;
  private final List<DerivedVariable> _derivedVariables;

  public ReferenceMetadata(APIStudyDetail study, List<DerivedVariable> derivedVariables) {
    _studyId = study.getId();
    _entityTree = buildEntityTree(study.getRootEntity(), derivedVariables, new ArrayList<>());
    _entityMap = buildEntityMap(_entityTree);
    _derivedVariables = derivedVariables;
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
   * @param allSpecifiedDerivedVariables all available derived var definitions
   * @param ancestorVars vars supplied by ancestor entities
   * @return root of a tree of entity defs containing all available vars on those entities
   */
  private static TreeNode<EntityDef> buildEntityTree(APIEntity entity,
      List<DerivedVariable> allSpecifiedDerivedVariables, List<VariableDef> ancestorVars) {

    EntityDef entityDef = new EntityDef(entity.getId(), entity.getDisplayName(), entity.getIdColumnName());

    // add inherited variables from parent
    ancestorVars.stream()
      .forEach(vd -> entityDef.add(new VariableDef(
          vd.getEntityId(),
          vd.getVariableId(),
          vd.getType(),
          vd.getDataShape(),
          VariableSource.INHERITED)));

    // process this entity's native vars
    entity.getVariables().stream()
      .filter(var -> !var.getType().equals(APIVariableType.CATEGORY))
      .map(var -> new VariableDef(
          entity.getId(),
          var.getId(),
          var.getType(),
          var.getDataShape(),
          VariableSource.NATIVE))
      .forEach(vd -> {
        // add variables for this entity
        entityDef.add(vd);

        // add this entity's native vars to ancestorVars list (copy will be passed to children)
        ancestorVars.add(vd);
      });

    // add derived variables for this entity
    //  (for now, can only use derived vars declared on the current entity, not its parents)
    allSpecifiedDerivedVariables.stream()
      // only derived vars for this entity
      .filter(dr -> dr.getEntityId().equals(entity.getId()))
      // skip if entity already contains the variable; TODO: will throw later
      .filter(dr -> !entityDef.getVariable(dr).isPresent())
      .map(dr -> new VariableDef(
          entity.getId(),
          dr.getVariableId(),
          dr.getVariableType(),
          dr.getVariableDataShape(),
          dr.getDerivationType()))

      .forEach(vd -> entityDef.add(vd));

    // put this entity in a node
    TreeNode<EntityDef> node = new TreeNode<>(entityDef);

    // add child entities
    for (APIEntity childEntity : entity.getChildren()) {
      // create new array list each time; don't want branches of entity tree polluting each other
      node.addChildNode(buildEntityTree(childEntity, allSpecifiedDerivedVariables, new ArrayList<>(ancestorVars)));
    }

    return node;
  }

  public String getStudyId() {
    return _studyId;
  }

  public Optional<EntityDef> getEntity(String entityId) {
    return Optional.ofNullable(_entityMap.get(entityId));
  }

  public Optional<VariableDef> getVariable(VariableSpec var) {
    return getEntity(var.getEntityId()).flatMap(e -> e.getVariable(var));
  }

  public List<DerivedVariable> getDerivedVariables() {
    return _derivedVariables;
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

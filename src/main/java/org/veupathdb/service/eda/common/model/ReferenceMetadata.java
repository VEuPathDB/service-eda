package org.veupathdb.service.eda.common.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.TreeNode;
import org.gusdb.fgputil.validation.ValidationBundle;
import org.gusdb.fgputil.validation.ValidationException;
import org.gusdb.fgputil.validation.ValidationLevel;
import org.veupathdb.service.eda.generated.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.gusdb.fgputil.functional.Functions.getMapFromList;

public class ReferenceMetadata {

  private static final Logger LOG = LogManager.getLogger(ReferenceMetadata.class);

  private final String _studyId;
  private final TreeNode<EntityDef> _entityTree;
  private final Map<String,EntityDef> _entityMap;

  public ReferenceMetadata(APIStudyDetail study) {
    _studyId = study.getId();
    _entityTree = buildEntityTree(study.getRootEntity(), new ArrayList<>());
    _entityMap = buildEntityMap(_entityTree);
  }

  public void incorporateComputedVariables(List<VariableMapping> computedVariables) {
    if (computedVariables.isEmpty()) return;

    // all computed vars must be of the same entity (one compute per request)
    String entityId = computedVariables.get(0).getVariableSpec().getEntityId();
    EntityDef entity = getEntity(entityId).orElseThrow(() ->
        new RuntimeException("Cannot find compute's entity in tree (" + entityId + ")"));

    // get list of the entity and its descendants; will add computed vars to each
    List<EntityDef> entities = new ArrayList<>();
    entities.add(entity);
    entities.addAll(getDescendants(entity));

    // convert each variable mapping to a variable def and add to all entities it will be available on
    for (VariableMapping computedVar : computedVariables) {
      if (!computedVar.getVariableSpec().getEntityId().equals(entityId)) {
        throw new RuntimeException("Not all computed vars specs are delcared as the same entity");
      }
      for (EntityDef treeEntity : entities) {
        entity.addVariable(new VariableDef(
            entityId,
            computedVar.getVariableSpec().getVariableId(),
            computedVar.getDataType(),
            computedVar.getDataShape(),
            false,
            computedVar.getImputeZero(),
            determineCustomVarDataRanges(computedVar.getDisplayRange()),
            Optional.empty(),
            null,
            entityId.equals(treeEntity.getId())
                ? VariableSource.COMPUTED
                : VariableSource.INHERITED
        ));
      }
    }
  }

  private Optional<DataRanges> determineCustomVarDataRanges(LabeledValueRange range) {
    if (range == null || (range.getMin() == null && range.getMax() == null))
      return Optional.empty();
    if (range.getMin() == null || range.getMax() == null)
      throw new RuntimeException("Computed variable display range must contain both min and max or neither.");
    return Optional.of(new DataRanges(
        new DataRange(range.getMin(), range.getMax()),
        new DataRange(range.getMin(), range.getMax())
    ));
  }

  /**
   * Incorporates a derived variable into the reference metadata.
   *
   * Note: incoming derived vars must be in dependency order; i.e. only later derived vars
   *       depend on earlier derived vars (plus no circular dependencies);
   *       name will also be pre-validated for uniqueness within study
   */
  public void incorporateDerivedVariable(DerivedVariableMetadata derivedVariable) {

    // set custom source; easier to look up DV instance later
    VariableSource typedSource = switch(derivedVariable.getDerivationType()) {
      case TRANSFORM -> VariableSource.DERIVED_TRANSFORM;
      case REDUCTION -> VariableSource.DERIVED_REDUCTION;
    };

    // get this DR's entity and descendants and insert as available in all
    EntityDef specEntity = getEntity(derivedVariable.getEntityId()).orElseThrow(() ->
        new IllegalArgumentException("Derived variable entity '" + derivedVariable.getEntityId() +
            "' does not exist in study '" + _studyId + "'."));

    List<EntityDef> entities = new ArrayList<>();
    entities.add(specEntity);
    entities.addAll(getDescendants(specEntity));

    // add derived variables for this entity to itself and all children (who can inherit the derived var)
    for (EntityDef entity : entities) {
      entity.addVariable(new VariableDef(
          derivedVariable.getEntityId(),
          derivedVariable.getVariableId(),
          derivedVariable.getVariableType(),
          derivedVariable.getDataShape(),
          false,
          false,
          determineCustomVarDataRanges(derivedVariable.getDataRange()),
          Optional.ofNullable(derivedVariable.getUnits()),
          null,
          entity.getId().equals(derivedVariable.getEntityId())
            ? typedSource
            : VariableSource.INHERITED
      ));
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
    ancestorVars.forEach(vd -> entityDef.addVariable(
        new VariableDef(
          vd.getEntityId(),
          vd.getVariableId(),
          vd.getType(),
          vd.getDataShape(),
          vd.isMultiValue(),
          vd.isImputeZero(),
          vd.getDataRanges(),
          vd.getUnits(),
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
          getUnits(var),
          var.getParentId(),
          VariableSource.NATIVE))
      .forEach(vd -> {
        // add variables for this entity
        entityDef.addVariable(vd);

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

  private static Optional<String> getUnits(APIVariableWithValues var) {
    if (var instanceof APINumberVariable)
      return Optional.ofNullable(((APINumberVariable)var).getUnits());
    else if (var instanceof APIIntegerVariable)
      return Optional.ofNullable(((APIIntegerVariable)var).getUnits());
    else
      return Optional.empty();
  }

  public String getStudyId() {
    return _studyId;
  }

  public Optional<EntityDef> getEntity(String entityId) {
    return Optional.ofNullable(_entityMap.get(entityId));
  }

  /**
   * Returns the variable def for this variable spec.  Note that when vars can be
   * inherited, they have more than one spec (with the second spec living on the
   * inheriting entity and having source 'inherited'.  However, this method will
   * always return the spec for the var's 'native' entity.
   *
   * @param varSpec variable spec for which variable def is desired
   * @return variable def optional if found, else empty optional
   */
  public Optional<VariableDef> getVariable(VariableSpec varSpec) {
    return getEntity(varSpec.getEntityId()).flatMap(e -> e.getVariable(varSpec));
  }

  public Optional<CollectionDef> getCollection(CollectionSpec colSpec) {
    return getEntity(colSpec.getEntityId()).flatMap(e -> e.getCollection(colSpec));
  }

  /**
   * Returns the child entities of the passed entity
   *
   * @param targetEntity entity whose children should be returned
   * @return list of child entities
   */
  public List<EntityDef> getChildren(EntityDef targetEntity) {
    return _entityTree
        .findFirst(node -> node.getId().equals(targetEntity.getId()))
        .getChildNodes()
        .stream()
        .map(TreeNode::getContents)
        .collect(Collectors.toList());
  }

  public List<EntityDef> getDescendants(EntityDef targetEntity) {
    return _entityTree
        .findFirst(node -> node.getId().equals(targetEntity.getId()))
        // find all nodes in this subtree except the root
        .findAll(entity -> !entity.getId().equals(targetEntity.getId()))
        .stream()
        .map(TreeNode::getContents)
        .collect(Collectors.toList());
  }

  /**
   * Returns the ancestor entities of the passed entity, ordered from the bottom (parent) up to the root
   *
   * @param targetEntity entity whose ancestors should be returned
   * @return list of ancestor entities
   */
  public List<EntityDef> getAncestors(EntityDef targetEntity) {
    return getAncestors(targetEntity, _entityTree, new ArrayList<>())
        .orElseThrow(() -> new RuntimeException(
            "Target entity '" + targetEntity.getId() +
            "' could not be found in entity tree."));
  }

  /**
   * Returns the ID column names (headers) for the target entity and its ancestors.
   * Note this is NOT a dot notation with entity and variable names, but simply the
   * ID column defs as returned by the subsetting and compute services.
   *
   * @param targetEntity entity for which ID names are needed
   * @return list of ID column names (variable names only)
   */
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

  public List<VariableDef> toVariableDefs(List<VariableSpec> varSpecs) throws ValidationException {
    ValidationBundle.ValidationBundleBuilder validation = ValidationBundle.builder(ValidationLevel.RUNNABLE);
    List<VariableDef> vars = new ArrayList<>();
    for (VariableSpec varSpec : varSpecs) {
      getVariable(varSpec).ifPresentOrElse(
          vars::add,
          () -> validation.addError(VariableDef.toDotNotation(varSpec), "Variable does not exist.")
      );
    }
    validation.build().throwIfInvalid();
    return vars;
  }
}

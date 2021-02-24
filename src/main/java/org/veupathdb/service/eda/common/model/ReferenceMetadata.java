package org.veupathdb.service.eda.common.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.TreeNode;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationBundle.ValidationBundleBuilder;
import org.gusdb.fgputil.validation.ValidationException;
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
          VariableSource.INHERITED)));

    // process this entity's native vars
    entity.getVariables().stream()
      .filter(var -> !var.getType().equals(APIVariableType.CATEGORY))
      .map(var -> new VariableDef(
          entity.getId(),
          var.getId(),
          var.getType(),
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
      .filter(dr -> !entityDef.hasVariable(dr))
      .map(dr -> new VariableDef(
          entity.getId(),
          dr.getVariableId(),
          dr.getVariableType(),
          dr.getDerivationType()))

      .forEach(vd -> entityDef.add(vd));

    // put this entity in a node
    TreeNode<EntityDef> node = new TreeNode<>(entityDef);

    // log resulting list
    LOG.info("Supplemented Entity: " + entityDef);

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

  public List<DerivedVariable> getDerivedVariables() {
    return _derivedVariables;
  }

  public Optional<DerivedVariable> findDerivedVariable(VariableSpec var) {
    return _derivedVariables.stream().filter(dr -> VariableDef.isSameVariable(dr, var)).findFirst();
  }

  public boolean containsEntity(String entityId) {
    return _entityMap.containsKey(entityId);
  }

  public boolean isNativeVarOfEntity(String variableId, String entityId) {
    return _entityMap.get(entityId).getVariableOpt(VariableDef.newVariableSpec(entityId, variableId))
        .map(foundVar -> foundVar.getSource().equals(VariableSource.NATIVE)).orElse(false);
  }

  public EntityDef getValidEntity(ValidationBundleBuilder validation, String entityId) throws ValidationException {
    EntityDef entity = _entityMap.get(entityId);
    if (entity == null) {
      validation.addError(getNoEntityMsg(entityId));
      validation.build().throwIfInvalid();
    }
    return entity;
  }

  public EntityDef getEntity(String entityId) {
    return Optional.ofNullable(_entityMap.get(entityId))
      .orElseThrow(() -> new RuntimeException(getNoEntityMsg(entityId)));
  }

  public VariableDef getVariableDef(VariableSpec var) {
    return getEntity(var.getEntityId()).getVariable(var);
  }

  private String getNoEntityMsg(String entityId) {
    return "No entity exists on study '" + getStudyId() + "' with ID '" + entityId + "'.";
  }

  public void validateVariableName(ValidationBundleBuilder validation,
                                   EntityDef entity, String variableUse, VariableSpec variable) {
    List<APIVariableType> nonCategoryTypes = Arrays.stream(APIVariableType.values())
        .filter(type -> !type.equals(APIVariableType.CATEGORY))
        .collect(Collectors.toList());
    validateVariableNameAndType(validation, entity, variableUse, variable, nonCategoryTypes.toArray(new APIVariableType[0]));
  }

  public void validateVariableNameAndType(ValidationBundleBuilder validation,
                                          EntityDef entity, String variableUse, VariableSpec varSpec, APIVariableType... allowedTypes) {
    List<APIVariableType> allowedTypesList = Arrays.asList(allowedTypes);
    if (allowedTypesList.contains(APIVariableType.CATEGORY)) {
      throw new RuntimeException("Plugin should not be using categories as variables.");
    }
    String varDesc = "Variable " + JsonUtil.serializeObject(varSpec) + ", used for " + variableUse + ", ";
    if (!entity.hasVariable(varSpec)) {
      validation.addError(varDesc + "does not exist in entity " + entity.getId());
    }
    else if (!allowedTypesList.contains(entity.getVariable(varSpec).getType())) {
      validation.addError(varDesc + "must be one of the following types: " + FormatUtil.join(allowedTypes, ", "));
    }
  }

  public List<EntityDef> getAncestors(EntityDef targetEntity) {
    return Optional.ofNullable(getAncestors(targetEntity, _entityTree, new ArrayList<>()))
        .orElseThrow(() -> new RuntimeException("Target entity '" + targetEntity.getId() + "' could not be found in entity tree."));
  }

  private static List<EntityDef> getAncestors(EntityDef targetEntity, TreeNode<EntityDef> entityTree, List<EntityDef> ancestors) {
    if (entityTree.getContents().getId().equals(targetEntity.getId())) {
      return ancestors; // done
    }
    for (TreeNode<EntityDef> child : entityTree.getChildNodes()) {
      List<EntityDef> supplementedAncestors = new ArrayList<>(ancestors);
      supplementedAncestors.add(0, entityTree.getContents()); // in ascending order (up the tree)
      List<EntityDef> listForFoundEntity = getAncestors(targetEntity, child, supplementedAncestors);
      if (listForFoundEntity != null) {
        return listForFoundEntity;
      }
    }
    return null;
  }
}

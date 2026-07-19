package org.veupathdb.service.eda.common.model;

import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.UUID;

/** Builds minimal generated-model study graphs that survive ReferenceMetadata.buildEntityTree. */
public final class TestStudies {

  private TestStudies() {}

  public static final String ROOT_ID = "EUPA_ROOT";
  public static final String CHILD_ID = "EUPA_CHILD";
  public static final String HOUSEHOLD_ID = "EUPA_HH";
  public static final String PARTICIPANT_ID = "EUPA_PART";

  // ---- variable builders ----

  /** STRING categorical variable (getDataRanges returns empty → safe). */
  public static APIStringVariable stringVar(String varId, List<String> vocab) {
    APIStringVariableImpl v = new APIStringVariableImpl(); // type is final STRING
    v.setId(varId);
    v.setDisplayName(varId);
    v.setDataShape(APIVariableDataShape.CATEGORICAL);
    v.setVocabulary(vocab);
    v.setDistinctValuesCount(vocab == null ? null : (long) vocab.size());
    v.setIsMultiValued(false);               // unboxed → must be non-null
    v.setImputeZero(false);                  // unboxed → must be non-null
    v.setHasStudyDependentVocabulary(false); // unboxed → must be non-null
    return v;
  }

  /** INTEGER CONTINUOUS variable with a populated range (safe through buildEntityTree). */
  public static APIIntegerVariable intVar(String varId, long rangeMin, long rangeMax) {
    APIIntegerVariableImpl v = new APIIntegerVariableImpl(); // type is final INTEGER
    v.setId(varId);
    v.setDisplayName(varId);
    v.setUnits("years");
    v.setDataShape(APIVariableDataShape.CONTINUOUS);
    v.setIsMultiValued(false);
    v.setImputeZero(false);
    v.setHasStudyDependentVocabulary(false);
    APIIntegerDistributionDefaultsImpl dd = new APIIntegerDistributionDefaultsImpl();
    dd.setRangeMin(rangeMin);
    dd.setRangeMax(rangeMax);
    dd.setDisplayRangeMin(rangeMin);
    dd.setDisplayRangeMax(rangeMax);
    v.setDistributionDefaults(dd);
    return v;
  }

  // ---- entity / study builders ----

  public static APIEntity entity(String id, String idColumnName, boolean manyToOne,
                                 List<APIVariable> vars, List<APIEntity> children) {
    APIEntityImpl e = new APIEntityImpl();
    e.setId(id);
    e.setIdColumnName(idColumnName);
    e.setDisplayName(id);
    e.setDisplayNamePlural(id + "s");
    e.setDescription(id);
    e.setIsManyToOneWithParent(manyToOne);
    e.setVariables(vars);           // must be non-null
    e.setCollections(List.of());    // must be non-null
    e.setChildren(children);        // must be non-null
    return e;
  }

  public static APIStudyDetail study(String id, APIEntity root) {
    APIStudyDetailImpl s = new APIStudyDetailImpl();
    s.setId(id);
    s.setIsUserStudy(false);
    s.setRootEntity(root);
    return s;
  }

  // ---- named single-study fixtures ----

  public static APIStudyDetail oneEntityOneStringVar(String studyId, String entityId, String varId, List<String> vocab) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(stringVar(varId, vocab)), List.of()));
  }

  public static APIStudyDetail oneEntityOneIntVar(String studyId, String entityId, String varId) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(intVar(varId, 0L, 100L)), List.of()));
  }

  public static APIStudyDetail oneEntityIntVarWithRange(String studyId, String entityId, String varId, long min, long max) {
    return study(studyId, entity(entityId, entityId + "_id", true,
        List.<APIVariable>of(intVar(varId, min, max)), List.of()));
  }

  /** Root + one child; distinct entity ids so it is structurally different from the single-entity fixtures. */
  public static APIStudyDetail twoEntities(String studyId) {
    APIEntity child = entity(CHILD_ID, "child_id", true,
        List.<APIVariable>of(stringVar("cvar", List.of("x", "y"))), List.of());
    APIEntity root = entity(ROOT_ID, "root_id", false,
        List.<APIVariable>of(stringVar("rvar", List.of("a", "b"))), List.of(child));
    return study(studyId, root);
  }

  // ---- union ReferenceMetadata fixtures ----

  public static ReferenceMetadata unionMetadata(String token, List<String> members) {
    return new ReferenceMetadata(twoEntities(token), members);
  }

  /** household (root) -> participant (child); household_id / participant_id PK columns. */
  public static ReferenceMetadata householdParticipantUnion(String token, List<String> members) {
    APIEntity participant = entity(PARTICIPANT_ID, "participant_id", true,
        List.<APIVariable>of(stringVar("pvar", List.of("x"))), List.of());
    APIEntity household = entity(HOUSEHOLD_ID, "household_id", false,
        List.<APIVariable>of(stringVar("region", List.of("north", "south"))), List.of(participant));
    return new ReferenceMetadata(study(token, household), members);
  }

  // ---- read-back helpers ----

  public static List<String> firstVarVocabulary(APIStudyDetail study) {
    return ((APIVariableWithValues) study.getRootEntity().getVariables().get(0)).getVocabulary();
  }

  public static long[] firstVarIntRange(APIStudyDetail study) {
    APIIntegerDistributionDefaults dd =
        ((APIIntegerVariable) study.getRootEntity().getVariables().get(0)).getDistributionDefaults();
    return new long[]{ dd.getRangeMin(), dd.getRangeMax() };
  }

  // ---- misc ----

  public static VariableSpec varSpec(String entityId, String varId) {
    return VariableDef.newVariableSpec(entityId, varId); // verified factory on VariableDef
  }

  public static StreamSpec streamSpec(String entityId) {
    return new StreamSpec("test-" + UUID.randomUUID(), entityId);
  }
}

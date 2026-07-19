package org.veupathdb.service.eda.common.model;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.generated.model.*;

import java.util.*;

/**
 * Tier 1 metadata merge for union meta-studies. Combines per-study summaries
 * (vocabularies, ranges) already present in each study's metadata; does not read
 * row data. Produces one merged APIStudyDetail whose id is the union token.
 */
public final class MetaStudyMerger {

  private MetaStudyMerger() {}

  public static APIStudyDetail merge(StudyId unionId, List<APIStudyDetail> members) {
    if (members.size() < 2)
      throw new BadRequestException("A union needs at least two members: " + unionId);

    // 1. structural compatibility: every member's tree fingerprint must match member[0]
    String reference = structuralFingerprint(members.get(0).getRootEntity());
    for (int i = 1; i < members.size(); i++) {
      String fp = structuralFingerprint(members.get(i).getRootEntity());
      if (!fp.equals(reference)) {
        throw new BadRequestException(
          "Union members are structurally incompatible. '" + members.get(0).getId()
          + "' vs '" + members.get(i).getId() + "'. Fingerprints differ:\n  "
          + reference + "\n  " + fp);
      }
    }

    // 2. build merged detail from member[0]'s shape, merging summaries across members
    APIStudyDetailImpl merged = new APIStudyDetailImpl();
    merged.setId(unionId.getRawToken());
    merged.setIsUserStudy(members.get(0).getIsUserStudy());
    merged.setHasMap(members.get(0).getHasMap());
    merged.setRootEntity(mergeEntity(members.stream().map(APIStudyDetail::getRootEntity).toList()));
    return merged;
  }

  /**
   * Deterministic structural signature: entity id, id column, and each variable's
   * id:type:dataShape:units, recursed over children in declared order.
   */
  public static String structuralFingerprint(APIEntity entity) {
    StringBuilder sb = new StringBuilder();
    appendFingerprint(entity, sb);
    return sb.toString();
  }

  private static void appendFingerprint(APIEntity e, StringBuilder sb) {
    sb.append('[').append(e.getId()).append('|').append(e.getIdColumnName()).append('{');
    e.getVariables().stream()
      .sorted(Comparator.comparing(APIVariable::getId))
      .forEach(v -> sb.append(v.getId()).append(':').append(v.getType())
        .append(':').append(dataShapeOf(v)).append(':').append(unitsOf(v)).append(';'));
    sb.append('}');
    e.getChildren().stream()
      .sorted(Comparator.comparing(APIEntity::getId))
      .forEach(c -> appendFingerprint(c, sb));
    sb.append(']');
  }

  private static String dataShapeOf(APIVariable v) {
    return (v instanceof APIVariableWithValues wv && wv.getDataShape() != null)
      ? wv.getDataShape().toString() : "-";
  }

  private static String unitsOf(APIVariable v) {
    // units live on integer/number variable subtypes; treat absent as "-"
    if (v instanceof APIIntegerVariable iv && iv.getUnits() != null) return iv.getUnits();
    if (v instanceof APINumberVariable nv && nv.getUnits() != null) return nv.getUnits();
    return "-";
  }

  private static APIEntity mergeEntity(List<APIEntity> entityAcrossMembers) {
    APIEntity ref = entityAcrossMembers.get(0);
    APIEntityImpl out = new APIEntityImpl();
    out.setId(ref.getId());
    out.setIdColumnName(ref.getIdColumnName());
    out.setDisplayName(ref.getDisplayName());
    out.setDisplayNamePlural(ref.getDisplayNamePlural());
    out.setDescription(ref.getDescription());
    out.setIsManyToOneWithParent(ref.getIsManyToOneWithParent());
    out.setCollections(ref.getCollections());

    // merge variables by id (structural compat already guarantees identical id sets/types)
    List<APIVariable> mergedVars = new ArrayList<>();
    for (APIVariable refVar : ref.getVariables()) {
      List<APIVariable> sameVarAcrossMembers = entityAcrossMembers.stream()
        .map(e -> e.getVariables().stream().filter(v -> v.getId().equals(refVar.getId())).findFirst().orElseThrow())
        .toList();
      mergedVars.add(mergeVariable(sameVarAcrossMembers));
    }
    out.setVariables(mergedVars);

    // recurse children (matched by id)
    List<APIEntity> mergedChildren = new ArrayList<>();
    for (APIEntity refChild : ref.getChildren()) {
      List<APIEntity> sameChildAcrossMembers = entityAcrossMembers.stream()
        .map(e -> e.getChildren().stream().filter(c -> c.getId().equals(refChild.getId())).findFirst().orElseThrow())
        .toList();
      mergedChildren.add(mergeEntity(sameChildAcrossMembers));
    }
    out.setChildren(mergedChildren);
    return out;
  }

  private static APIVariable mergeVariable(List<APIVariable> across) {
    APIVariable ref = across.get(0);
    if (!(ref instanceof APIVariableWithValues)) {
      return ref; // categories / non-value vars carry no vocab/range to merge
    }
    // Vocabulary union (deduped, sorted for a normalized order)
    if (ref instanceof APIStringVariable) {
      APIStringVariableImpl out = copyStringVar((APIStringVariable) ref);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APIIntegerVariable) {
      APIIntegerVariableImpl out = copyIntVar((APIIntegerVariable) ref);
      mergeIntRange(out, across);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APIDateVariable) {
      APIDateVariableImpl out = copyDateVar((APIDateVariable) ref);
      mergeDateRange(out, across);
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APILongitudeVariable) {
      APILongitudeVariableImpl out = copyLongitudeVar((APILongitudeVariable) ref);
      // longitude carries no distributionDefaults on the generated model; vocab union only
      out.setVocabulary(unionVocab(across));
      return out;
    }
    if (ref instanceof APINumberVariable) {
      return mergeNumberVariable(across);
    }
    // Fallback for any other APIVariableWithValues subtype: pass through ref unchanged.
    return ref;
  }

  private static List<String> unionVocab(List<APIVariable> across) {
    TreeSet<String> union = new TreeSet<>(); // dedup + normalized (sorted) order
    for (APIVariable v : across) {
      List<String> vocab = ((APIVariableWithValues) v).getVocabulary();
      if (vocab != null) union.addAll(vocab);
    }
    return union.isEmpty() ? null : new ArrayList<>(union);
  }

  // Range merges: min(mins) / max(maxes) over each member's distributionDefaults.
  private static void mergeIntRange(APIIntegerVariableImpl out, List<APIVariable> across) {
    Long min = null, max = null, dMin = null, dMax = null;
    for (APIVariable v : across) {
      APIIntegerDistributionDefaults dd = ((APIIntegerVariable) v).getDistributionDefaults();
      if (dd == null) continue;
      min = minL(min, dd.getRangeMin());     max = maxL(max, dd.getRangeMax());
      dMin = minL(dMin, dd.getDisplayRangeMin()); dMax = maxL(dMax, dd.getDisplayRangeMax());
    }
    if (min == null && max == null && dMin == null && dMax == null) return;
    APIIntegerDistributionDefaultsImpl dd = new APIIntegerDistributionDefaultsImpl();
    dd.setRangeMin(min); dd.setRangeMax(max);
    dd.setDisplayRangeMin(dMin != null ? dMin : min);
    dd.setDisplayRangeMax(dMax != null ? dMax : max);
    out.setDistributionDefaults(dd);
  }

  private static void mergeDateRange(APIDateVariableImpl out, List<APIVariable> across) {
    String min = null, max = null, dMin = null, dMax = null;
    Integer binWidth = null, binWidthOverride = null;
    BinUnits binUnits = null;
    for (APIVariable v : across) {
      APIDateDistributionDefaults dd = ((APIDateVariable) v).getDistributionDefaults();
      if (dd == null) continue;
      min = minStr(min, dd.getRangeMin()); max = maxStr(max, dd.getRangeMax());
      dMin = minStr(dMin, dd.getDisplayRangeMin()); dMax = maxStr(dMax, dd.getDisplayRangeMax());
      if (binWidth == null) binWidth = dd.getBinWidth();
      if (binWidthOverride == null) binWidthOverride = dd.getBinWidthOverride();
      if (binUnits == null) binUnits = dd.getBinUnits();
    }
    if (min == null && max == null && dMin == null && dMax == null) return;
    APIDateDistributionDefaultsImpl dd = new APIDateDistributionDefaultsImpl();
    dd.setRangeMin(min); dd.setRangeMax(max);
    dd.setDisplayRangeMin(dMin != null ? dMin : min);
    dd.setDisplayRangeMax(dMax != null ? dMax : max);
    dd.setBinWidth(binWidth);
    dd.setBinWidthOverride(binWidthOverride);
    dd.setBinUnits(binUnits);
    out.setDistributionDefaults(dd);
  }

  private static APIVariable mergeNumberVariable(List<APIVariable> across) {
    APINumberVariableImpl out = copyNumberVar((APINumberVariable) across.get(0));
    mergeNumberRange(out, across);
    out.setVocabulary(unionVocab(across));
    return out;
  }

  private static void mergeNumberRange(APINumberVariableImpl out, List<APIVariable> across) {
    Number min = null, max = null, dMin = null, dMax = null;
    Number binWidth = null, binWidthOverride = null;
    for (APIVariable v : across) {
      APINumberDistributionDefaults dd = ((APINumberVariable) v).getDistributionDefaults();
      if (dd == null) continue;
      min = minN(min, dd.getRangeMin());     max = maxN(max, dd.getRangeMax());
      dMin = minN(dMin, dd.getDisplayRangeMin()); dMax = maxN(dMax, dd.getDisplayRangeMax());
      if (binWidth == null) binWidth = dd.getBinWidth();
      if (binWidthOverride == null) binWidthOverride = dd.getBinWidthOverride();
    }
    if (min == null && max == null && dMin == null && dMax == null) return;
    APINumberDistributionDefaultsImpl dd = new APINumberDistributionDefaultsImpl();
    dd.setRangeMin(min); dd.setRangeMax(max);
    dd.setDisplayRangeMin(dMin != null ? dMin : min);
    dd.setDisplayRangeMax(dMax != null ? dMax : max);
    dd.setBinWidth(binWidth);
    dd.setBinWidthOverride(binWidthOverride);
    out.setDistributionDefaults(dd);
  }

  // --- small helpers (null-safe min/max; shallow copies preserving all ref fields) ---
  private static Long minL(Long a, Long b) { return a == null ? b : (b == null ? a : Math.min(a, b)); }
  private static Long maxL(Long a, Long b) { return a == null ? b : (b == null ? a : Math.max(a, b)); }
  private static String minStr(String a, String b) { return a == null ? b : (b == null ? a : (a.compareTo(b) <= 0 ? a : b)); }
  private static String maxStr(String a, String b) { return a == null ? b : (b == null ? a : (a.compareTo(b) >= 0 ? a : b)); }
  private static Number minN(Number a, Number b) { return a == null ? b : (b == null ? a : (a.doubleValue() <= b.doubleValue() ? a : b)); }
  private static Number maxN(Number a, Number b) { return a == null ? b : (b == null ? a : (a.doubleValue() >= b.doubleValue() ? a : b)); }

  // copy*Var: build a *Impl, copy every getter of the ref var onto it (id, parentId,
  // displayName, type, dataShape, isMultiValued, imputeZero, distinctValuesCount, units,
  // hasStudyDependentVocabulary, etc.). Kept exhaustive so no metadata is dropped.
  private static APIStringVariableImpl copyStringVar(APIStringVariable v) {
    APIStringVariableImpl out = new APIStringVariableImpl();
    copyCommonWithValuesFields(v, out);
    return out;
  }

  private static APIIntegerVariableImpl copyIntVar(APIIntegerVariable v) {
    APIIntegerVariableImpl out = new APIIntegerVariableImpl();
    copyCommonWithValuesFields(v, out);
    out.setUnits(v.getUnits());
    // distributionDefaults set later by mergeIntRange
    return out;
  }

  private static APINumberVariableImpl copyNumberVar(APINumberVariable v) {
    APINumberVariableImpl out = new APINumberVariableImpl();
    copyCommonWithValuesFields(v, out);
    out.setUnits(v.getUnits());
    out.setPrecision(v.getPrecision());
    out.setScale(v.getScale());
    // distributionDefaults set later by mergeNumberRange
    return out;
  }

  private static APIDateVariableImpl copyDateVar(APIDateVariable v) {
    APIDateVariableImpl out = new APIDateVariableImpl();
    copyCommonWithValuesFields(v, out);
    // distributionDefaults set later by mergeDateRange
    return out;
  }

  private static APILongitudeVariableImpl copyLongitudeVar(APILongitudeVariable v) {
    APILongitudeVariableImpl out = new APILongitudeVariableImpl();
    copyCommonWithValuesFields(v, out);
    out.setPrecision(v.getPrecision());
    return out;
  }

  /** Copies every field common to all APIVariableWithValues subtypes (everything but vocabulary,
   * which is merged separately, and subtype-specific fields like units/distributionDefaults). */
  private static void copyCommonWithValuesFields(APIVariableWithValues src, APIVariableWithValues dest) {
    dest.setId(src.getId());
    dest.setParentId(src.getParentId());
    dest.setProviderLabel(src.getProviderLabel());
    dest.setDisplayName(src.getDisplayName());
    dest.setDefinition(src.getDefinition());
    dest.setDisplayType(src.getDisplayType());
    dest.setDisplayOrder(src.getDisplayOrder());
    dest.setIsCategory(src.getIsCategory());
    dest.setHideFrom(src.getHideFrom());
    dest.setDataShape(src.getDataShape());
    dest.setDistinctValuesCount(src.getDistinctValuesCount());
    dest.setIsTemporal(src.getIsTemporal());
    dest.setIsFeatured(src.getIsFeatured());
    dest.setIsMergeKey(src.getIsMergeKey());
    dest.setIsMultiValued(src.getIsMultiValued());
    dest.setImputeZero(src.getImputeZero());
    dest.setHasStudyDependentVocabulary(src.getHasStudyDependentVocabulary());
    dest.setVariableSpecToImputeZeroesFor(src.getVariableSpecToImputeZeroesFor());
  }
}

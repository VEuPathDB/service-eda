package org.veupathdb.service.eda.ss.model;

public class RdbmsColumnNames {

  static final String VARIABLE_VALUE_COL_NAME = "value";

  // common
  static final String DESCRIP_COL_NAME = "description";
  static final String DISPLAY_NAME_COL_NAME = "display_name";
  static final String DISPLAY_NAME_PLURAL_COL_NAME = "display_name_plural";

  // Study table
  static final String STUDY_TABLE_NAME = "study";
  static final String STUDY_ID_COL_NAME = "stable_id";
  static final String STUDY_ABBREV_COL_NAME = "internal_abbrev";

  // StudyIdDatasetId table
  static final String STUDY_DATASET_TABLE_NAME = "StudyIdDatasetId";
  static final String STUDY_DATASET_ID_COL_NAME = "dataset_id";
  static final String STUDY_DATASET_STUDY_ID_COL_NAME = "study_stable_id";

  // Entity table and EntityName table
  static final String ENTITY_TABLE_NAME = "EntityTypeGraph";
  static final String ENTITY_ID_COL_NAME = "stable_id";
  static final String ENTITY_STUDY_ID_COL_NAME = "study_stable_id";
  static final String ENTITY_PARENT_ID_COL_NAME = "parent_stable_id";
  static final String ENTITY_ABBREV_COL_NAME = "internal_abbrev";

  // AttributeGraph table
  static final String VARIABLE_ID_COL_NAME = "stable_id";
  static final String VARIABLE_TYPE_COL_NAME = "data_type";
  static final String PROVIDER_LABEL_COL_NAME = "provider_label";
  static final String DATA_SHAPE_COL_NAME = "data_shape";
  static final String DISPLAY_TYPE_COL_NAME = "display_type";
  static final String HAS_VALUES_COL_NAME = "has_values";
  static final String MULTIVALUED_COL_NAME = "is_multi_valued";
  static final String UNITS_COL_NAME = "unit";
  static final String PRECISION_COL_NAME = "precision";
  static final String VARIABLE_PARENT_ID_COL_NAME = "parent_stable_id";
  static final String DEFINITION_COL_NAME = "definition";
  static final String VOCABULARY_COL_NAME = "vocabulary";
  static final String DISPLAY_ORDER_COL_NAME = "display_order";
  static final String DISPLAY_RANGE_MIN_COL_NAME = "display_range_min";
  static final String DISPLAY_RANGE_MAX_COL_NAME = "display_range_max";
  static final String RANGE_MIN_COL_NAME = "range_min";
  static final String RANGE_MAX_COL_NAME = "range_max";
  static final String BIN_WIDTH_OVERRIDE_COL_NAME = "bin_width_override";
  static final String BIN_WIDTH_COMPUTED_COL_NAME = "bin_width_computed";
  static final String IS_TEMPORAL_COL_NAME = "is_temporal";
  static final String IS_FEATURED_COL_NAME = "is_featured";
  static final String IS_MERGE_KEY_COL_NAME = "is_merge_key";
  static final String IS_REPEATED_COL_NAME = "is_repeated";
  static final String DISTINCT_VALUES_COUNT_COL_NAME = "distinct_values_count";
  static final String IS_MULTI_VALUED_COL_NAME = "is_multi_valued";

  // Tall table
  public static final String TT_VARIABLE_ID_COL_NAME = "attribute_stable_id";
  public static final String STRING_VALUE_COL_NAME = "string_value";
  public static final String DATE_VALUE_COL_NAME = "date_value";
  public static final String NUMBER_VALUE_COL_NAME = "number_value";

  private RdbmsColumnNames(){}
}

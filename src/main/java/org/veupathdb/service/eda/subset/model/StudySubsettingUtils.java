package org.veupathdb.service.eda.ss.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import static org.gusdb.fgputil.FormatUtil.NL;
import static org.gusdb.fgputil.FormatUtil.TAB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.ListBuilder;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.db.runner.SQLRunner;
import org.gusdb.fgputil.db.runner.SingleIntResultSetHandler;
import org.gusdb.fgputil.db.stream.ResultSetIterator;
import org.gusdb.fgputil.db.stream.ResultSets;
import org.gusdb.fgputil.functional.TreeNode;
import org.gusdb.fgputil.iterator.GroupingIterator;
import org.veupathdb.service.eda.ss.Resources;
import org.veupathdb.service.eda.ss.model.filter.Filter;

import static org.gusdb.fgputil.iterator.IteratorUtil.toIterable;
import static org.veupathdb.service.eda.ss.model.RdbmsColumnNames.DATE_VALUE_COL_NAME;
import static org.veupathdb.service.eda.ss.model.RdbmsColumnNames.NUMBER_VALUE_COL_NAME;
import static org.veupathdb.service.eda.ss.model.RdbmsColumnNames.STRING_VALUE_COL_NAME;
import static org.veupathdb.service.eda.ss.model.RdbmsColumnNames.TT_VARIABLE_ID_COL_NAME;

/**
 * A class to perform subsetting operations on a study entity
 * 
 * @author Steve
 *
 */
public class StudySubsettingUtils {

  private static final Logger LOG = LogManager.getLogger(StudySubsettingUtils.class);

  private static final int FETCH_SIZE_FOR_TABULAR_QUERIES = 2000;

  private static final String COUNT_COLUMN_NAME = "count";

  /**
   * Writes to the passed output stream tab delimited records containing
   * the requested variables of the specified entity, reduced to the
   *
   * @param datasource DB to run against
   * @param study study context
   * @param outputEntity entity type to return
   * @param outputVariables variables requested
   * @param filters filters to apply to create a subset of records
   */
  public static void produceTabularSubset(DataSource datasource, Study study, Entity outputEntity,
                                          List<Variable> outputVariables, List<Filter> filters, 
                                          TabularReportConfig reportConfig, OutputStream outputStream) {

    TreeNode<Entity> prunedEntityTree = pruneTree(study.getEntityTree(), filters, outputEntity);

    String sql = reportConfig == null
        ? generateTabularSqlNoReportConfig(outputVariables, outputEntity, filters, prunedEntityTree)
        : generateTabularSqlWithReportConfig(outputVariables, outputEntity, filters, reportConfig, prunedEntityTree);
    LOG.debug("Generated the following tabular SQL: " + sql);

    List<String> outputColumns = getTabularOutputColumns(outputEntity, outputVariables);

    new SQLRunner(datasource, sql, "Produce tabular subset").executeQuery(rs -> {
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

        writer.write(String.join(TAB, outputColumns) + NL);
        if (reportConfig == null)
          writeWideRowsNoReportConfig(convertTallRowsResultSet(rs, outputEntity), writer, outputColumns, outputEntity);
        else
          writeWideRowsWithReportConfig(rs, writer, outputColumns, outputEntity);

        writer.flush();
        return null;
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }, FETCH_SIZE_FOR_TABULAR_QUERIES);
  }
  

  static List<String> getTabularOutputColumns(Entity outputEntity, List<Variable> outputVariables) {
	List<String> outputColumns = new ArrayList<>();
	    
	List<String> outputVariableIds = outputVariables.stream().map(Variable::getId).collect(Collectors.toList());
    outputColumns.add(outputEntity.getPKColName());
    outputColumns.addAll(outputEntity.getAncestorPkColNames());
    outputColumns.addAll(outputVariableIds);
    return outputColumns;
  }

  /**
   * @return an iterator of maps, each representing a single row in the tall table
   */
  private static Iterator<Map<String, String>> convertTallRowsResultSet(ResultSet rs, Entity outputEntity) {
    return new ResultSetIterator<>(rs, row -> Optional.of(EntityResultSetUtils.resultSetToTallRowMap(outputEntity, rs)));
  }

  static void writeWideRowsNoReportConfig(Iterator<Map<String, String>> tallRowsIterator,
      Writer writer, List<String> outputColumns, Entity outputEntity) throws IOException {

    // an iterator of lists of maps, each list being the rows of the tall table returned for a single entity id
    String pkCol = outputEntity.getPKColName();
    Iterator<List<Map<String, String>>> groupedTallRowsIterator = new GroupingIterator<>(
        tallRowsIterator, (row1, row2) -> {
          return row1.get(pkCol).equals(row2.get(pkCol));
        }
    );

    // iterate through groups and format into strings to be written to stream
    for (List<Map<String, String>> group : toIterable(groupedTallRowsIterator)) {
      Map<String, String> wideRowMap = EntityResultSetUtils.getTallToWideFunction(outputEntity).apply(group);
      List<String> wideRow = new ArrayList<>();
      for (String colName : outputColumns) {
        wideRow.add(wideRowMap.getOrDefault(colName, ""));
      }
      writer.write(String.join(TAB, wideRow) + NL);
    }
  }
  
	static void writeWideRowsWithReportConfig(ResultSet rs, Writer writer, List<String> outputColumns,
			Entity outputEntity) throws IOException, SQLException {

		while (rs.next()) {
			List<String> wideRow = new ArrayList<>();
			for (String colName : outputColumns) {
				wideRow.add(rs.getString(colName));
			}
			writer.write(String.join(TAB, wideRow) + NL);
		}
	}

  /**
   * NOTE! This stream MUST be closed by the caller once the stream has been processed.
   * The easiest way to do this is with a try-with-resources around this method's call.
   * @return stream of distribution tuples
   */
  public static Stream<TwoTuple<String,Long>> produceVariableDistribution(
      DataSource datasource, TreeNode<Entity> prunedEntityTree, Entity outputEntity,
      Variable distributionVariable, List<Filter> filters) {
    String sql = generateDistributionSql(outputEntity, distributionVariable, filters, prunedEntityTree);
    LOG.info("Generated the following distribution SQL: " + NL + sql + NL);
    return ResultSets.openStream(datasource, sql, "Produce variable distribution", row -> Optional.of(
        new TwoTuple<>(distributionVariable.getType().convertRowValueToStringValue(row), row.getLong(COUNT_COLUMN_NAME))));
  }

  public static int getVariableCount(
      DataSource datasource, TreeNode<Entity> prunedEntityTree, Entity outputEntity,
      Variable distributionVariable, List<Filter> filters) {
    String sql = generateVariableCountSql(outputEntity, distributionVariable, filters, prunedEntityTree);
    return new SQLRunner(datasource, sql, "Get variable count for distribution").executeQuery(new SingleIntResultSetHandler());
  }

  public static int getEntityCount(
      DataSource datasource, TreeNode<Entity> prunedEntityTree, Entity targetEntity, List<Filter> filters) {
    String sql = generateEntityCountSql(targetEntity, filters, prunedEntityTree);
    return new SQLRunner(datasource, sql, "Get entity count").executeQuery(new SingleIntResultSetHandler());
  }

  /**
   * Prune tree to include only active nodes, based on filters and output entity
   */
  public static TreeNode<Entity> pruneTree(TreeNode<Entity> tree, List<Filter> filters, Entity outputEntity) {

    List<String> entityIdsInFilters = getEntityIdsInFilters(filters);

    Predicate<Entity> isActive =
        e -> entityIdsInFilters.contains(e.getId()) ||
        e.getId().equals(outputEntity.getId());

    return pruneToActiveAndPivotNodes(tree, isActive);
  }
  
  static List<String> getEntityIdsInFilters(List<Filter> filters) {
    return filters.stream().map(f -> f.getEntity().getId()).collect(Collectors.toList());
  }

  /**
   * Generate SQL to produce a tall stream of Entity ID, ancestry IDs, variable ID and values.  
   */
  static String generateTabularSqlNoReportConfig(List<Variable> outputVariables, Entity outputEntity, List<Filter> filters, TreeNode<Entity> prunedEntityTree) {

    LOG.debug("--------------------- generateTabularSql ------------------");

    String tallTblAbbrev = "tall";
    String ancestorTblAbbrev = "subset";
    return
      // with clauses create an entity-named filtered result for each relevant entity
      generateFilterWithClauses(prunedEntityTree, filters) + NL +
      // select
      generateTabularSelectClause(outputEntity, ancestorTblAbbrev) + NL +
      generateTabularFromClause(outputEntity, prunedEntityTree, ancestorTblAbbrev) + NL +
      // left join to attributes table so we always get at least one row per subset
      //   record, even if no data exists for requested vars (or no vars requested).
      // null rows will be handled in the tall-to-wide rows conversion
      generateLeftJoin(outputEntity, outputVariables, ancestorTblAbbrev, tallTblAbbrev) + NL +
      generateTabularOrderByClause(outputEntity) + NL;
  }

  /**
   * Generate SQL to produce a multi-column tabular output (the requested variables), for the specified subset.
   * 
   * 
WITH
EUPATH_0000609 as (
  SELECT Observation_stable_id, Participant_stable_id, Household_stable_id, Sample_stable_id FROM apidb.Ancestors_GEMSCC0003_1_Sample
),
subset AS (
  SELECT EUPATH_0000609.Sample_stable_id
  FROM EUPATH_0000609
),
wide_tabular AS (
  select rownum as r, wt.*
  from (
    select Sample_stable_id, 
    Observation_stable_id, 
    Participant_stable_id, 
    Household_stable_id, 
    json_query(atts, '$.EUPATH_0000711') as EUPATH_0000711, 
    ea.stable_id
    from apidb.entityattributes ea, apidb.Ancestors_GEMSCC0003_1_Sample a
    where ea.stable_id in (select * from subset)
    and ea.stable_id = a.Sample_stable_id
    order by Sample_stable_id
  ) wt
)
select Sample_stable_id, Observation_stable_id, Participant_stable_id, Household_stable_id, EUPATH_0000711
from wide_tabular
where r > 2 and r <= 8
order by Sample_stable_id
   */
  static String generateTabularSqlWithReportConfig(List<Variable> outputVariables, Entity outputEntity, List<Filter> filters, 
		  TabularReportConfig reportConfig, TreeNode<Entity> prunedEntityTree) {
	    LOG.debug("--------------------- generateTabularSql paging sorting ------------------");

    String wideTabularWithClauseName = "wide_tabular"; 
    String subsetWithClauseName = "subset"; 
    String rowColName = "r";
    
    //
    // build up WITH clauses
    //
    String wideTabularInnerStmt = generateRawWideTabularInnerStmt(outputEntity, outputVariables, subsetWithClauseName, reportConfig);
    String wideTabularStmt = generateRawWideTabularOuterStmt(wideTabularInnerStmt);
    String subsetSelectClause = generateSubsetSelectClause(prunedEntityTree, outputEntity, false);

    List<String> filterWithClauses = prunedEntityTree.flatten().stream().map(e -> generateFilterWithClause(e, filters)).collect(Collectors.toList());
    List<String> withClausesList = new ArrayList<String>(filterWithClauses);
    withClausesList.add(subsetWithClauseName + " AS (" + NL + subsetSelectClause + ")");
    withClausesList.add(wideTabularWithClauseName + " AS (" + NL + wideTabularStmt + NL + ")");
    String withClauses = joinWithClauses(withClausesList);
    
    //
    // final select 
    //
    List<String> outputCols = getTabularOutputColumns(outputEntity, outputVariables);
    return withClauses + NL
        + "select " + String.join(", ", outputCols) + NL
        + "from "+ wideTabularWithClauseName + NL
        + reportConfigPagingWhereClause(reportConfig, rowColName) + NL
		+ reportConfigOrderByClause(reportConfig, "");
  }
    
  static String reportConfigPagingWhereClause(TabularReportConfig config, String rowColName) {
	  if (config == null || (config.getOffset() == null && config.getNumRows() == null)) 
		  return "";
	  
	  int start = 0;
	  if (config.getOffset() != null) start = config.getOffset();
	  String whereClause = "where " + rowColName + " > " + start;
	  if (config.getNumRows() != null) {
		  whereClause += " and " + rowColName + " <= " + (start + config.getNumRows());
	  }
	  return whereClause;
  }

  static String reportConfigOrderByClause(TabularReportConfig config, String indent) {
	  if (config == null || (config.getSortingColumns() == null))
		  return "";
	  return indent + "order by " + String.join(", ", config.getSortingColumns());
  }
  
  /*
   *       select json_query(atts, '$.EUPATH_0010077') as EUPATH_0010077
      , json_query(atts, '$.CMO_0000289') as CMO_0000289
      , json_query(atts, '$.EUPATH_0015125') as EUPATH_0015125
      , ea.stable_id, rownum as row
      from apidb.entityattributes ea
      where ea.stable_id in (
          select * from subset
        )
   */
	static String generateRawWideTabularInnerStmt(Entity outputEntity, List<Variable> outputVariables,
			String subsetWithClauseName, TabularReportConfig reportConfig) {
		
		String schema = Resources.getAppDbSchema(); 
		
		List<String> columns = new ArrayList<String>();
		columns.add(outputEntity.getPKColName());
		columns.addAll(outputEntity.getAncestorPkColNames());
		for (Variable var : outputVariables) {
			String oracleJsonQuery = "json_query(atts, '$." + var.getId() + "') as " + var.getId();
			String col = jsonQuery(oracleJsonQuery, null);
			columns.add(col);
		}
		columns.add("ea.stable_id");
	    return "    select " + String.join(", " + NL + "    ", columns) + NL +
    		 "    from " + schema + "entityattributes ea, " + schema + outputEntity.getAncestorsTableName() + " a" + NL + 
    		 "    where ea.stable_id in (select * from " + subsetWithClauseName + ")" + NL +
    		 "    and ea.stable_id = a." + outputEntity.getPKColName() + NL +
    		 reportConfigOrderByClause(reportConfig, "    ");
  }
  
  static String generateRawWideTabularOuterStmt(String innerStmt) {
		return "  select rownum as r, wt.*" + NL +
			   "  from (" + NL +
			   innerStmt + NL +
			   "  ) wt";			   
  }

	
  static String jsonQuery(String oracleQuery, String postgresQuery) {
	  return oracleQuery != null? oracleQuery : postgresQuery;
  }

  private static String generateLeftJoin(Entity outputEntity, List<Variable> outputVariables, String ancestorTblAbbrev, String tallTblAbbrev) {
    if (outputVariables.isEmpty()) {
      return " LEFT JOIN ( SELECT " +
          "null as " + TT_VARIABLE_ID_COL_NAME + ", "  +
          "null as " + STRING_VALUE_COL_NAME + ", " +
          "null as " + DATE_VALUE_COL_NAME + ", " +
          "null as " + NUMBER_VALUE_COL_NAME +
        " FROM DUAL ) ON 1 = 1 ";
    }
    String pkColName = outputEntity.getPKColName();
    return " LEFT JOIN (" + NL
        + " SELECT * FROM " + Resources.getAppDbSchema() + outputEntity.getTallTableName() + " " + NL
        + generateTabularWhereClause(outputVariables, pkColName) + NL
        + " ) " + tallTblAbbrev + NL
        + " ON " + ancestorTblAbbrev + "." + pkColName + " = " + tallTblAbbrev + "." + pkColName;
  }

  /*
   * Generate SQL to produce a multi-column tabular output (the requested variables), for the specified subset.
   */
  static String generateEntityCountSql(Entity outputEntity, List<Filter> filters, TreeNode<Entity> prunedEntityTree) {
    
    return generateFilterWithClauses(prunedEntityTree, filters) + NL
        + "SELECT count(distinct " + outputEntity.getPKColName() + ") as " + COUNT_COLUMN_NAME + NL
        + "FROM (" + NL
        + generateSubsetSelectClause(prunedEntityTree, outputEntity, false) + NL
        + ") t";
  }
  
  /**
   * Generate SQL to produce a count of the entities that have a value for a variable, for the specified subset.
   */
  static String generateVariableCountSql(Entity outputEntity, Variable variable, List<Filter> filters, TreeNode<Entity> prunedEntityTree) {
    return generateFilterWithClauses(prunedEntityTree, filters) + NL
        + generateVariableCountSelectClause(variable) + NL
        + generateDistributionFromClause(outputEntity) + NL
        + generateDistributionWhereClause(variable) + NL
        + generateSubsetInClause(prunedEntityTree, outputEntity, outputEntity.getTallTableName());

   }

  static String generateFilterWithClauses(TreeNode<Entity> prunedEntityTree, List<Filter> filters) {
	    List<String> withClauses = prunedEntityTree.flatten().stream().map(e -> generateFilterWithClause(e, filters)).collect(Collectors.toList());
	    return joinWithClauses(withClauses);
	  }
	  
  static String joinWithClauses(List<String> withClauses) {
	    return "WITH" + NL
	        + String.join("," + NL, withClauses);
	  }
	  
  /*
   * Get a with clause for this entity.  If the filters don't include any from this entity,
   * then the with clause will just select * from the entity's ancestor table
   */
  static String generateFilterWithClause(Entity entity, List<Filter> filters) {

    List<String> selectColsList = new ArrayList<>(entity.getAncestorPkColNames());
    selectColsList.add(entity.getPKColName());
    String selectCols = String.join(", ", selectColsList);

    // default WITH body assumes no filters. we use the ancestor table because it is small
    String withBody = "  SELECT " + selectCols + " FROM " + Resources.getAppDbSchema() + entity.getAncestorsTableName() + NL;

    List<Filter> filtersOnThisEntity = filters.stream().filter(f -> f.getEntity().getId().equals(entity.getId())).collect(Collectors.toList());

    if (!filtersOnThisEntity.isEmpty()) {
      List<String> filterSqls = filters.stream().filter(f -> f.getEntity().getId().equals(entity.getId())).map(Filter::getSql).collect(Collectors.toList());
      withBody = String.join("INTERSECT" + NL, filterSqls);
    } 

    return entity.getWithClauseName() + " as (" + NL + withBody + ")";
  }
  
  static String generateTabularSelectClause(Entity outputEntity, String ancestorTblAbbrev) {
    Set<String> valColNames = Arrays
        .stream(VariableType.values())
        .map(VariableType::getTallTableColumnName)
        .collect(Collectors.toSet());
    return "SELECT " + outputEntity.getAllPksSelectList(ancestorTblAbbrev) + ", " +
        TT_VARIABLE_ID_COL_NAME + ", " + String.join(", ", valColNames);
  }

  static String generateVariableCountSelectClause(Variable variable) {
    return "SELECT count(distinct " + variable.getEntity().getPKColName() + ") as " + COUNT_COLUMN_NAME;
  }

  /**
   * Generate SQL to produce a distribution for a single variable, for the specified subset.
   */
  /*
  -- WINNER!  This is the distribution SQL we want (includes null count)
WITH
EUPATH_0000096 as (
  SELECT Household_stable_id, Participant_stable_id FROM apidb.Ancestors_UMSP00001_1_Participant
)
select number_value, count(Participant_stable_id) from (
select subset.*, tall.number_value
from EUPATH_0000096 subset
left join (
  select * from apidb.AttributeValue_UMSP00001_1_Participant
  where attribute_stable_id = 'IAO_0000414'
) tall
on tall.Participant_stable_id = subset.Participant_stable_id
)
group by number_value
order by number_value desc;
   */

  static String generateDistributionSql(Entity outputEntity, Variable distributionVariable, List<Filter> filters, TreeNode<Entity> prunedEntityTree) {

    String tallTblAbbrev = "tall";
    String ancestorTblAbbrev = "subset";
    return
      // with clauses create an entity-named filtered result for each relevant entity
      generateFilterWithClauses(prunedEntityTree, filters) + NL +
      generateDistributionSelectClause(distributionVariable) + NL +
      " FROM ( " + NL +
        generateTabularSelectClause(outputEntity, ancestorTblAbbrev) + NL +
        generateTabularFromClause(outputEntity, prunedEntityTree, ancestorTblAbbrev) + NL +
        // left join to attributes table so we always get at least one row per subset
        //   record, even if no data exists for requested vars (or no vars requested).
        // null rows will be handled in the tall-to-wide rows conversion
        generateLeftJoin(outputEntity, ListBuilder.asList(distributionVariable), ancestorTblAbbrev, tallTblAbbrev) + NL +
      " ) " + NL +
      generateDistributionGroupByClause(distributionVariable) + NL +
      "ORDER BY " + distributionVariable.getType().getTallTableColumnName() + " ASC";
   }

  static String generateDistributionSelectClause(Variable distributionVariable) {
    return "SELECT " +
        distributionVariable.getType().getTallTableColumnName() +
        ", count(" + distributionVariable.getEntity().getPKColName() + ") as " + COUNT_COLUMN_NAME;
  }

  static String generateDistributionFromClause(Entity outputEntity) {
    return "FROM " + Resources.getAppDbSchema() + outputEntity.getTallTableName();
  }

  private static String generateTabularFromClause(Entity outputEntity, TreeNode<Entity> prunedEntityTree, String ancestorTblAbbrev) {
    return " FROM ( " + generateSubsetSelectClause(prunedEntityTree, outputEntity, true) + " ) " + ancestorTblAbbrev;
  }

  static String generateTabularWhereClause(List<Variable> outputVariables, String entityPkCol) {
    
    List<String> outputVariableExprs = outputVariables.stream()
        .map(Variable::getId)
        .map(varId -> " " + TT_VARIABLE_ID_COL_NAME + " = '" + varId + "'")
        .collect(Collectors.toList());

    return outputVariableExprs.isEmpty() ? "" :
        " WHERE (" + NL + String.join(" OR" + NL, outputVariableExprs) + NL + ")" + NL;
  }
  
  static String generateDistributionWhereClause(Variable outputVariable) {
    return "WHERE " + TT_VARIABLE_ID_COL_NAME + " = '" + outputVariable.getId() + "'";
  }

  static String generateSubsetInClause(TreeNode<Entity> prunedEntityTree, Entity outputEntity, String tallTblAbbrev) {
    return "AND" + " " + tallTblAbbrev + "." + outputEntity.getPKColName() + " IN (" + NL
    + generateSubsetSelectClause(prunedEntityTree, outputEntity, false) + NL
    + ")";
  }
  
  static String generateSubsetSelectClause(TreeNode<Entity> prunedEntityTree, Entity outputEntity, boolean returnAncestorIds) {
    return generateJoiningSelectClause(outputEntity, returnAncestorIds) + NL
    + generateJoiningFromClause(prunedEntityTree) + NL
    + generateJoiningJoinsClause(prunedEntityTree); 
  }
  
  static String generateJoiningSelectClause(Entity outputEntity, boolean returnAncestorIds) {
    List<String> returnedCols = ListBuilder.asList(outputEntity.getFullPKColName());
    if (returnAncestorIds) {
      returnedCols.addAll(
          outputEntity.getAncestorPkColNames().stream()
              .map(pk -> outputEntity.getId() + "." + pk)
              .collect(Collectors.toList()));
    }
    return "  SELECT " + returnedCols.stream().collect(Collectors.joining(", "));
  }
  
  static String generateJoiningFromClause(TreeNode<Entity> prunedEntityTree) {
    List<String> fromClauses = prunedEntityTree.flatten().stream().map(Entity::getWithClauseName).collect(Collectors.toList());
    return "  FROM " + String.join(", ", fromClauses);
  }

  static String generateJoiningJoinsClause(TreeNode<Entity> prunedEntityTree) {
    List<String> sqlJoinStrings = new ArrayList<>();
    addSqlJoinStrings(prunedEntityTree, sqlJoinStrings);
    return sqlJoinStrings.isEmpty()? "" : "  WHERE " + String.join(NL + "  AND ", sqlJoinStrings);
  }
  
  /*
   * Add to the input list the sql join of a parent entity with each of its children, plus, recursively, its
   * children's sql joins.  (Because the tree might have been pruned, the "parent" is some ancestor)
   */
  static void addSqlJoinStrings(TreeNode<Entity> parent, List<String> sqlJoinStrings) {
    for (TreeNode<Entity> child : parent.getChildNodes()) {
      sqlJoinStrings.add(getSqlJoinString(parent.getContents(), child.getContents()));
      addSqlJoinStrings(child, sqlJoinStrings);
    }
  }

  // this join is formed using the name from the WITH clause, which is the entity name
  static String getSqlJoinString(Entity parentEntity, Entity childEntity) {
    return parentEntity.getWithClauseName() + "." + parentEntity.getPKColName() + " = " +
        childEntity.getWithClauseName() + "." + parentEntity.getPKColName();
  }

  // need to order by the root of the tree first, then by each ID down the branch to the output entity,
  static String generateTabularOrderByClause(Entity outputEntity) {
    List<String> cols = new ArrayList<>();
    // reverse the order of the ancestor pk cols to go root first, parent last
    outputEntity.getAncestorPkColNames().stream().forEach(a -> cols.add(0, a));
    // add output entity last
    cols.add(outputEntity.getPKColName());
    return "ORDER BY " + String.join(", ", cols);
  }
  
  static String generateDistributionGroupByClause(Variable outputVariable) {
    return "GROUP BY " + outputVariable.getType().getTallTableColumnName();
  }
  

  /*
   * PRUNE THE COMPLETE TREE TO JUST THE "ACTIVE" ENTITIES WE WANT FOR OUR JOINS
   * 
   * definition: an active entity is one that must be included in the SQL definition: an active subtree is one
   * in which any entities in the subtree are active.
   * 
   * this entity is active if any of these apply: 1. it has filters 2. it is the output entity 3. it is
   * neither of the above, but has more than one child that is the root of an active subtree
   * 
   * (criterion 3 lets us join elements across connected subtrees)
   * 
   * ----X---- | | --I-- I | | | A I A
   * 
   * In the picture above the A entities are active and I are inactive. X has two children that are active
   * subtrees. We need to force X to be active so that we can join the lower A entities.
   *
   * So will we now have this:
   * 
   * ----A---- | | --I-- I | | | A I A
   * 
   * Finally, we want to prune the tree of inactive nodes, so we have the minimal active tree:
   * 
   * ----A---- | | A A
   * 
   * Now we can ascend the tree and form the concise SQL joins we need
   * 
   * Using a concrete example: ----H---- | | --P-- E | | | O S T
   * 
   * If O and and T are active (have filters or are the output entity), then we ultimately need this join:
   * where O.H_id = H.H_id and T.H_id = H.H_id
   * 
   * (The graceful implementation below is courtesy of Ryan)
   */
  private static TreeNode<Entity> pruneToActiveAndPivotNodes(TreeNode<Entity> root, Predicate<Entity> isActive) {
    return root.mapStructure((nodeContents, mappedChildren) -> {
      List<TreeNode<Entity>> activeChildren = mappedChildren.stream()
        .filter(Objects::nonNull) // filter dead branches
        .collect(Collectors.toList());
      return isActive.test(nodeContents) || activeChildren.size() > 1 ?
        // this node is active itself or a pivot node; return with any active children
        new TreeNode<>(nodeContents).addAllChildNodes(activeChildren) :
        // inactive, non-pivot node; return single active child or null
        activeChildren.isEmpty() ? null : activeChildren.get(0);
    });
  }

}

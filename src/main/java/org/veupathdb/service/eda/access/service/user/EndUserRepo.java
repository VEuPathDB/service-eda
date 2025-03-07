package org.veupathdb.service.eda.access.service.user;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.vulpine.lib.query.util.basic.*;
import org.slf4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.eda.access.model.*;
import org.veupathdb.service.eda.access.repo.DB;
import org.veupathdb.service.eda.access.repo.SQL;
import org.veupathdb.service.eda.access.service.QueryUtil;
import org.veupathdb.service.eda.access.util.PsBuilder;
import org.veupathdb.service.eda.access.util.SqlUtil;

public class EndUserRepo
{
  private static final Logger log = LogProvider.logger(EndUserRepo.class);

  public interface Delete
  {
    static void endUser(final EndUserRow user, final long causeUserId) throws Exception {
      log.trace("EndUserRepo$Delete#endUser(EndUserRow)");

      try (var con = QueryUtil.acctDbConnection()) {
        new BasicPreparedVoidQuery(SQL.Delete.EndUsers.ById, con, ps -> ps.setLong(1, user.getEndUserID())).execute();

        EndUserUtil.insertHistoryEvent(con, HistoryAction.DELETE, user, causeUserId);
      }
    }
  }

  public interface Insert
  {
    static void newEndUser(final EndUserRow row, final long creatorID) throws Exception {
      long endUserId = QueryUtil.performInsertWithIdGeneration(
        SQL.Insert.EndUser,
        QueryUtil::acctDbConnection,
        DB.Column.EndUser.EndUserID,
        new PsBuilder()
          .setLong(row.getUserId())
          .setString(row.getDatasetId())
          .setShort(RestrictionLevelCache.getInstance()
            .get(row.getRestrictionLevel()).orElseThrow())
          .setShort(ApprovalStatusCache.getInstance()
            .get(row.getApprovalStatus()).orElseThrow())
          .setObject(row.getStartDate(), Types.DATE)
          .setLong(row.getDuration())
          .setString(row.getPurpose())
          .setString(row.getResearchQuestion())
          .setString(row.getAnalysisPlan())
          .setString(row.getDisseminationPlan())
          .setString(row.getPriorAuth())
          .setString(row.getDenialReason())
          .setObject(row.getDateDenied(), Types.TIMESTAMP_WITH_TIMEZONE)
          .setBoolean(row.isAllowSelfEdits())
          ::build
      );

      row.setEndUserID(endUserId);

      // Insert history entry
      try (Connection con = QueryUtil.acctDbConnection()) {
        EndUserUtil.insertHistoryEvent(con, HistoryAction.CREATE, row, creatorID);
      }
    }
  }

  public interface Select
  {
    @SuppressWarnings("resource")
    static List<EndUserRow> find(final SearchQuery query) throws Exception {
      return new BasicPreparedListReadQuery<>(
        SQL.Select.EndUsers.ByQuery,
        QueryUtil.getInstance()::getAcctDbConnection,
        EndUserUtil::parseEndUserRow,
        ps -> {
          if (query.hasDatasetId())
            ps.setString(1, query.getDatasetId());
          else
            ps.setNull(1, Types.VARCHAR);
          if (query.hasLimit())
            ps.setLong(2, query.getLimit());
          else
            ps.setNull(2, Types.INTEGER);
          if (query.hasOffset())
            ps.setLong(3, query.getOffset());
          else
            ps.setNull(3, Types.INTEGER);
          if (query.hasApprovalStatus())
            ps.setShort(4, ApprovalStatusCache.getInstance()
              .get(query.getApprovalStatus())
              .orElseThrow());
          else
            ps.setNull(4, Types.SMALLINT);
        }
      ).execute().getValue();
    }

    @SuppressWarnings("resource")
    static int count(final SearchQuery query) throws Exception {
      return new BasicPreparedReadQuery<>(
        SQL.Select.EndUsers.CountByQuery,
        QueryUtil.getInstance()::getAcctDbConnection,
        SqlUtil.reqParser(SqlUtil::parseSingleInt),
        ps -> {
          if (query.hasDatasetId())
            ps.setString(1, query.getDatasetId());
          else
            ps.setNull(1, Types.VARCHAR);
          if (query.hasApprovalStatus())
            ps.setShort(2, ApprovalStatusCache.getInstance()
              .get(query.getApprovalStatus())
              .orElseThrow());
          else
            ps.setNull(2, Types.SMALLINT);
        }
      ).execute().getValue();
    }

    /**
     * Returns a list of {@link EndUserRow} instances representing the results
     * of a <code>SELECT</code> query searching for at most <code>limit</code>
     * end user records (starting from offset <code>offset+1</code>) matching
     * the given <code>datasetId</code>.
     */
    @SuppressWarnings("resource")
    static List<EndUserRow> list(final String datasetId, final int limit, final int offset) throws Exception {
      return new BasicPreparedListReadQuery<>(
        SQL.Select.EndUsers.ByDataset,
        QueryUtil.getInstance()::getAcctDbConnection,
        EndUserUtil::parseEndUserRow,
        new PsBuilder().setString(datasetId).setInt(offset).setInt(limit)::build
      ).execute().getValue();
    }

    @SuppressWarnings("resource")
    static Optional<EndUserRow> endUser(final long userId, final String datasetId) throws Exception {
      return new BasicPreparedReadQuery<>(
        SQL.Select.EndUsers.ById,
        QueryUtil.getInstance()::getAcctDbConnection,
        SqlUtil.optParser(EndUserUtil::parseEndUserRow),
        new PsBuilder().setLong(userId).setString(datasetId)::build
      ).execute().getValue();
    }

    /**
     * Returns a list of dataset IDs that the given user has been approved to
     * access.
     *
     * @param userId ID of the user to check
     *
     * @return a map of datasets to approval status for the given user
     */
    @SuppressWarnings("resource")
    static Map<String,ApprovalStatus> datasets(final long userId) throws Exception {
      return new BasicPreparedMapReadQuery<>(
        SQL.Select.EndUsers.Datasets,
        QueryUtil::acctDbConnection,
        rs -> rs.getString(DB.Column.EndUser.DatasetId),
        rs -> ApprovalStatus.valueOf(rs.getString("status").toUpperCase()),
        SqlUtil.prepareSingleLong(userId)
      ).execute().getValue();
    }
  }

  public interface Update
  {
    static void self(final EndUserRow row, final long updaterID) throws Exception {
      log.info("Approval status: {}", ApprovalStatusCache.getInstance().get(row.getApprovalStatus()));
      try (var con = QueryUtil.acctDbConnection()) {
        new BasicPreparedWriteQuery(
          SQL.Update.EndUser.SelfUpdate,
          con,
          new PsBuilder()
            .setString(row.getPurpose())
            .setString(row.getResearchQuestion())
            .setString(row.getAnalysisPlan())
            .setString(row.getDisseminationPlan())
            .setString(row.getPriorAuth())
            .setBoolean(row.isAllowSelfEdits())
            .setShort(ApprovalStatusCache.getInstance().get(row.getApprovalStatus()).orElseThrow())
            .setLong(row.getUserId())
            .setString(row.getDatasetId())
            ::build
        ).execute();

        // Insert history entry
        EndUserUtil.insertHistoryEvent(con, HistoryAction.UPDATE, row, updaterID);
      }
    }

    static void mod(final EndUserRow row, final long updaterID) throws Exception {
      try (var con = QueryUtil.acctDbConnection()) {
        new BasicPreparedWriteQuery(
          SQL.Update.EndUser.ModUpdate,
          con,
          new PsBuilder()
            .setObject(row.getStartDate(), Types.TIME_WITH_TIMEZONE)
            .setLong(row.getDuration())
            .setString(row.getPurpose())
            .setString(row.getResearchQuestion())
            .setString(row.getAnalysisPlan())
            .setString(row.getDisseminationPlan())
            .setString(row.getPriorAuth())
            .setShort(RestrictionLevelCache.getInstance()
              .get(row.getRestrictionLevel())
              .orElseThrow())
            .setShort(ApprovalStatusCache.getInstance().get(row.getApprovalStatus()).orElseThrow())
            .setString(row.getDenialReason())
            .setObject(row.getDateDenied(), Types.TIMESTAMP_WITH_TIMEZONE)
            .setBoolean(row.isAllowSelfEdits())
            .setLong(row.getUserId())
            .setString(row.getDatasetId())
            ::build
        ).execute();

        // Insert history entry
        EndUserUtil.insertHistoryEvent(con, HistoryAction.UPDATE, row, updaterID);
      }
    }
  }
}

package org.veupathdb.service.eda.access.service.history.queries;

import io.vulpine.lib.query.util.basic.BasicPreparedListReadQuery;
import org.veupathdb.service.eda.access.service.history.model.HistoryResultRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

public class SelectHistoryRows
{
  private static final String SQL = """
    SELECT
      end_user_id
    , user_id
    , dataset_presenter_id
    , history_action
    , history_timestamp
    , history_cause_user
    , restriction_level_id
    , approval_status_id
    , start_date
    , duration
    , purpose
    , research_question
    , analysis_plan
    , dissemination_plan
    , prior_auth
    , denial_reason
    , date_denied
    , allow_self_edits
    FROM
      studyaccess.end_user_history
    OFFSET ? ROWS
    FETCH FIRST ? ROWS ONLY""";

  private static final int HardLimit = 1000;

  private final Connection con;
  private final long limit;
  private final long offset;

  public SelectHistoryRows(Connection con, long limit, long offset) {
    this.con    = Objects.requireNonNull(con);
    this.limit  = Math.min(Math.max(1, limit), HardLimit);
    this.offset = Math.max(0, offset);
  }

  public List<HistoryResultRow> run() throws Exception {
    return new BasicPreparedListReadQuery<>(SQL, con, HistoryResultRow::new, this::prep)
      .execute()
      .getValue();
  }

  private void prep(PreparedStatement ps) throws Exception {
    ps.setLong(1, offset);
    ps.setLong(2, limit);
  }
}

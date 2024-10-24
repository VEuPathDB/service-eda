package org.veupathdb.service.eda.access.service;

import io.vulpine.lib.jcfi.CheckedBiFunction;
import io.vulpine.lib.jcfi.CheckedConsumer;
import io.vulpine.lib.jcfi.CheckedFunction;
import io.vulpine.lib.jcfi.CheckedSupplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.veupathdb.lib.container.jaxrs.utils.db.DbManager;

public class QueryUtil
{
  private static final QueryUtil instance = new QueryUtil();

  @SuppressWarnings("resource")
  public Connection getAcctDbConnection() throws Exception {
    return DbManager.accountDatabase().getDataSource().getConnection();
  }

  @SuppressWarnings("resource")
  public Connection getAppDbConnection() throws Exception {
    return DbManager.applicationDatabase().getDataSource().getConnection();
  }

  public ResultSet runQueryLogged(final Statement s, final String q) throws Exception {
    return exec(s, q, PreparedStatement::executeQuery, Statement::executeQuery);
  }

  public boolean runLogged(final Statement s, final String q) throws Exception {
    return exec(s, q, PreparedStatement::execute, Statement::execute);
  }

  public PreparedStatement prepareSqlStatement(final Connection c, final String sql)
  throws Exception {
    try {
      return c.prepareStatement(sql);
    } catch (Exception e) {
      throw new Exception("Failed to prepare query:\n" + sql, e);
    }
  }

  public PreparedStatement prepareSqlStatement(
    final Connection con,
    final String sql,
    final String[] returning
  ) throws Exception {
    try {
      return con.prepareStatement(sql, returning);
    } catch (Exception e) {
      throw new Exception("Failed to prepare query:\n" + sql, e);
    }
  }

  public < T > T exec(
    final Statement ps,
    final String sql,
    final CheckedFunction< PreparedStatement, T > fn1,
    final CheckedBiFunction< Statement, String, T > fn2
  ) throws Exception {
    try {
      return ps instanceof PreparedStatement
        ? fn1.apply((PreparedStatement) ps)
        : fn2.apply(ps, sql);
    } catch (Exception e) {
      throw new Exception("Query failed:\n" + sql, e);
    }
  }



  public static QueryUtil getInstance() {
    return instance;
  }

  public static Connection acctDbConnection() throws Exception {
    return getInstance().getAcctDbConnection();
  }

  public static Connection appDbConnection() throws Exception {
    return getInstance().getAppDbConnection();
  }

  public static ResultSet executeQueryLogged(final Statement s, final String q) throws Exception {
    return getInstance().runQueryLogged(s, q);
  }

  public static boolean executeLogged(final Statement ps, final String sql) throws Exception {
    return getInstance().runLogged(ps, sql);
  }

  public static PreparedStatement prepareStatement(final Connection con, final String sql)
  throws Exception {
    return getInstance().prepareSqlStatement(con, sql);
  }

  public static PreparedStatement prepareStatement(
    final Connection con,
    final String sql,
    final String[] returning
  ) throws Exception {
    return getInstance().prepareSqlStatement(con, sql, returning);
  }

  public static Long performInsertWithIdGeneration(
      final String sql,
      final CheckedSupplier<Connection> connection,
      final String idColumnName,
      final CheckedConsumer<PreparedStatement> statementPreparer) throws Exception {
    try (Connection conn = connection.get();
         PreparedStatement ps = conn.prepareStatement(sql, new String[]{ idColumnName })) {
      statementPreparer.accept(ps);
      int numRowsInserted = ps.executeUpdate();
      if (numRowsInserted > 0) {
        // only return the first ID
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            return rs.getLong(1);
          }
          else throw new RuntimeException("1: No ID generated with name " + idColumnName + " via SQL: " + sql);
        }
      }
      else throw new RuntimeException("2: No ID generated with name " + idColumnName + " via SQL: " + sql);
    }
  }
}

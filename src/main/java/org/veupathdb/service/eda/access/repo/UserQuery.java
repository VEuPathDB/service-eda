package org.veupathdb.service.eda.access.repo;

import java.sql.ResultSet;

import org.veupathdb.service.eda.access.model.UserRow;

abstract class UserQuery
{
  @Deprecated
  static void parseUser(UserRow row, ResultSet rs) throws Exception {
    row.setEmail(rs.getString(DB.Column.Accounts.Email));
    row.setFirstName(rs.getString(DB.Column.Misc.FirstName));
    row.setLastName(rs.getString(DB.Column.Misc.LastName));
    row.setOrganization(rs.getString(DB.Column.Misc.Organization));
  }
}

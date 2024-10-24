package org.veupathdb.service.eda.access.service.staff;

import java.sql.ResultSet;

import org.veupathdb.service.eda.access.model.StaffRow;
import org.veupathdb.service.eda.access.repo.DB;
import org.veupathdb.service.eda.access.service.user.UserUtil;

public class StaffUtil
{
  private static final StaffUtil instance = new StaffUtil();

  public StaffRow resultRowToStaffRow(final ResultSet rs) throws Exception {
    var out = new StaffRow();
    out.setStaffId(rs.getInt(DB.Column.Staff.StaffId));
    out.setOwner(rs.getBoolean(DB.Column.Staff.IsOwner));
    UserUtil.fillUser(rs, out);
    return out;
  }

  public static StaffUtil getInstance() {
    return instance;
  }
}

package org.veupathdb.service.eda.user.model;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.veupathdb.service.eda.user.stubdb.StubDb;

public class DbUpdateTest {
  
  private static DataSource datasource;
  
  @BeforeAll
  public static void setUp() {
    datasource = StubDb.getDataSource();
  }
  
  @Test
  @DisplayName("Sample Test")
  void sampleTest() {

  }
}

package org.veupathdb.service.eda.ss;

import static org.gusdb.fgputil.runtime.Environment.getOptionalVar;
import static org.gusdb.fgputil.runtime.Environment.getRequiredVar;

public class EnvironmentVars {

  protected boolean _developmentMode;
  protected String _appDbSchema;
  protected String _userStudySchema;
  protected String _datasetAccessServiceUrl;
  protected String _binaryFilesDirectory;

  public void load() {
    _developmentMode = Boolean.parseBoolean(getOptionalVar("DEVELOPMENT_MODE", "true"));
    _appDbSchema = getOptionalVar("APP_DB_SCHEMA", "eda.");
    _userStudySchema = getOptionalVar("USER_STUDY_SCHEMA", "apidbuserdatasets.");
    // TODO Make this mandatory once docker-compose changes are deployed.
    _binaryFilesDirectory = getOptionalVar("BINARY_FILES_DIR", "/tmp/binaryFilesDir");
    _datasetAccessServiceUrl = getRequiredVar("DATASET_ACCESS_SERVICE_URL");
  }

  public boolean isDevelopmentMode() {
    return _developmentMode;
  }

  public String getAppDbSchema() {
    return _appDbSchema;
  }

  public String getUserStudySchema() {
    return _userStudySchema;
  }

  public String getDatasetAccessServiceUrl() {
    return _datasetAccessServiceUrl;
  }

  public String getBinaryFilesDirectory() {
    return _binaryFilesDirectory;
  }
}

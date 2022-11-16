package org.veupathdb.service.eda.ss;

import static org.gusdb.fgputil.runtime.Environment.getOptionalVar;
import static org.gusdb.fgputil.runtime.Environment.getRequiredVar;

public class EnvironmentVars {

  protected boolean _developmentMode;
  protected String _appDbSchema;
  protected String _userStudySchema;
  protected String _datasetAccessServiceUrl;
  protected String _binaryFilesDirectory;
  protected String _availableBinaryFilesPaths;
  protected String _dbBuild;
  protected boolean _fileBasedSubsettingEnabled;

  public void load() {
    _developmentMode = Boolean.parseBoolean(getOptionalVar("DEVELOPMENT_MODE", "true"));
    _appDbSchema = getOptionalVar("APP_DB_SCHEMA", "eda.");
    _userStudySchema = getOptionalVar("USER_STUDY_SCHEMA", "apidbuserdatasets.");
    _availableBinaryFilesPaths = getOptionalVar("AVAILABLE_BINARY_FILES_PATHS" ,"");
    _dbBuild = getOptionalVar("DB_BUILD", "");
    _binaryFilesDirectory = getOptionalVar("BINARY_FILES_DIR", "");
    _datasetAccessServiceUrl = getRequiredVar("DATASET_ACCESS_SERVICE_URL");
    // Temporarily hard-code file-based subsetting to true to enable testing in development environments.
    _fileBasedSubsettingEnabled = true;
  }

  public boolean isDevelopmentMode() {
    return _developmentMode;
  }

  public boolean isFileBasedSubsettingEnabled() {
    return _fileBasedSubsettingEnabled;
  }

  public String getAppDbSchema() {
    return _appDbSchema;
  }

  public String getUserStudySchema() {
    return _userStudySchema;
  }

  public String getDbBuild() {
    return _dbBuild;
  }

  public String getDatasetAccessServiceUrl() {
    return _datasetAccessServiceUrl;
  }

  public String getBinaryFilesDirectory() {
    return _binaryFilesDirectory;
  }

  public String getAvailableBinaryFilesPaths() {
    return _availableBinaryFilesPaths;
  }
}

package org.veupathdb.service.eda.subset;

import static org.gusdb.fgputil.runtime.Environment.getOptionalVar;
import static org.gusdb.fgputil.runtime.Environment.getRequiredVar;

public class EnvironmentVars {

  protected final boolean _developmentMode;
  protected final String _appDbSchema;
  protected final String _userStudySchema;
  protected final String _datasetAccessServiceUrl;
  protected final String _binaryFilesDirectory;
  protected final String _availableBinaryFilesPaths;
  protected final String _dbBuild;
  protected final boolean _fileBasedSubsettingEnabled;
  private final String _binaryFilesMount;

  public EnvironmentVars() {
    _developmentMode = Boolean.parseBoolean(getOptionalVar("DEVELOPMENT_MODE", "true"));
    _appDbSchema = getOptionalVar("APP_DB_SCHEMA", "eda.");
    _userStudySchema = getOptionalVar("USER_STUDY_SCHEMA", "apidbuserdatasets.");
    _availableBinaryFilesPaths = getOptionalVar("AVAILABLE_BINARY_FILES_PATHS" ,"");
    // All of these file-based subsetting variables should be marked as required once docker-compose files are deployed.
    _dbBuild = getOptionalVar("DB_BUILD", "");
    _binaryFilesDirectory = getOptionalVar("BINARY_FILES_DIR", "");
    _binaryFilesMount = getOptionalVar("BINARY_FILES_MOUNT", "");
    _datasetAccessServiceUrl = getRequiredVar("DATASET_ACCESS_SERVICE_URL");
    _fileBasedSubsettingEnabled = Boolean.parseBoolean(getOptionalVar("FILE_SUBSETTING_ENABLED", "false"));
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

  public String getBinaryFilesMount() {
    return _binaryFilesMount;
  }
}

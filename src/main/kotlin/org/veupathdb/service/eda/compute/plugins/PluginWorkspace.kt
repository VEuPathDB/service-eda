package org.veupathdb.service.eda.compute.plugins

import org.veupathdb.lib.compute.platform.job.JobWorkspace
import org.veupathdb.service.eda.compute.jobs.ReservedFiles
import java.io.InputStream
import java.io.Reader

/**
 * Plugin Workspace
 *
 * Wrapper for a plugin job's local scratch workspace.  Provides methods for
 * reading and writing files in the job workspace.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 1.0.0
 */
class PluginWorkspace(val internalWorkspace: JobWorkspace) : JobWorkspace by internalWorkspace {

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result data.
   *
   * @param data Stream of data to write out to the result data file.
   */
  fun writeDataResult(data: InputStream) {
    write(ReservedFiles.OutputTabular, data)
  }

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result data.
   *
   * @param data Stream of data to write out to the result data file.
   */
  fun writeDataResult(data: Reader) {
    write(ReservedFiles.OutputTabular, data)
  }

  /**
   * Writes the given string out to the file expected by the service for plugin
   * result data.
   *
   * @param data Data to write out to the result data file.
   */
  fun writeDataResult(data: String) {
    write(ReservedFiles.OutputTabular, data)
  }

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result metadata.
   *
   * @param data Stream of data to write out to the result metadata file.
   */
  fun writeMetaResult(data: InputStream) {
    write(ReservedFiles.OutputMeta, data)
  }

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result metadata.
   *
   * @param data Stream of data to write out to the result metadata file.
   */
  fun writeMetaResult(data: Reader)  {
    write(ReservedFiles.OutputMeta, data)
  }

  /**
   * Writes the given string out to the file expected by the service for plugin
   * result metadata.
   *
   * @param data Data to write out to the result metadata file.
   */
  fun writeMetaResult(data: String)  {
    write(ReservedFiles.OutputMeta, data)
  }

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result statistics.
   *
   * @param data Stream of data to write out to the result statistics file.
   */
  fun writeStatisticsResult(data: InputStream) {
    write(ReservedFiles.OutputStats, data)
  }

  /**
   * Writes the given stream out to the file expected by the service for plugin
   * result statistics.
   *
   * @param data Stream of data to write out to the result statistics file.
   */
  fun writeStatisticsResult(data: Reader) {
    write(ReservedFiles.OutputStats, data)
  }

  /**
   * Writes the given string out to the file expected by the service for plugin
   * result statistics.
   *
   * @param data Data to write out to the result statistics file.
   */
  fun writeStatisticsResult(data: String) {
    write(ReservedFiles.OutputStats, data)
  }
}
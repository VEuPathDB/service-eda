package org.veupathdb.service.eda.compute.util

import com.fasterxml.jackson.databind.JsonNode
import org.veupathdb.lib.hash_id.HashID
import org.veupathdb.lib.jackson.Json
import org.veupathdb.service.eda.generated.model.ComputeRequestBase

object JobIDs {
  /**
   * Generates a job ID value from the given plugin name (url segment) and config
   * entity.
   *
   * Job IDs are generated from the MD5 hash of the following JSON array
   * structure:
   * ```json
   * [
   *   "plugin-url-segment",
   *   {
   *     "plugin": "configuration json",
   *     ...
   *   }
   * ]
   * ```
   *
   * @param pluginName URL segment of the plugin.
   *
   * @param entity HTTP compute request body.
   *
   * @return A [HashID] calculated from the given inputs.
   */
  fun of(pluginName: String, entity: ComputeRequestBase) =
    of(pluginName, Json.convert(entity))

  /**
   * Generates a job ID value from the given plugin name (url segment) and
   * serialized JSON config entity.
   *
   * Job IDs are generated from the MD5 hash of the following JSON array
   * structure:
   * ```json
   * [
   *   "plugin-url-segment",
   *   {
   *     "plugin": "configuration json",
   *     ...
   *   }
   * ]
   * ```
   *
   * @param pluginName URL segment of the plugin.
   *
   * @param entity Serialized JSON HTTP compute request body.
   *
   * @return A [HashID] calculated from the given inputs.
   */
  fun of(pluginName: String, entity: JsonNode) =
    HashID.ofMD5(Json.newArray(2) {
      add(pluginName)
      add(entity)
    })
}
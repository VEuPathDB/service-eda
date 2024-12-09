package org.veupathdb.service.eda.compute.exec

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

/**
 * Plugin Job Queue Payload Envelope
 *
 * Wrapper type for the raw json payload that will be passed through the job
 * queue on job submission.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
internal data class PluginJobPayload(
  /**
   * Name of the plugin that this job should be executed by.
   */
  @JsonProperty("plugin")
  val plugin: String,

  /**
   * Serialized HTTP request that triggered this job.
   */
  @JsonProperty("request")
  val request: JsonNode,
)

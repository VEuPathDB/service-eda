package org.veupathdb.service.eda.compute.service

import org.veupathdb.lib.container.jaxrs.server.ContainerResources
import org.veupathdb.service.eda.compute.controller.ComputeController

class Resources : ContainerResources(ServiceOptions) {
  override fun resources(): Array<Any> = arrayOf(
    ComputeController::class.java,
    JobsController,
  )
}
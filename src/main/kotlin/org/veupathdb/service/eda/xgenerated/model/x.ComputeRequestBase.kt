@file:JvmName("xComputeRequestBase")
package org.veupathdb.service.eda.xgenerated.model

import org.veupathdb.service.eda.generated.model.ComputeRequestBase

fun ComputeRequestBase.getConfig(): Any? =
  try {
    this::class.java.getDeclaredMethod("getConfig").invoke(this)
  } catch (e: Throwable) {
    null
  }

@file:JvmName("Exceptions")
package org.veupathdb.service.eda.util

import io.vulpine.lib.jcfi.CheckedRunnable
import io.vulpine.lib.jcfi.CheckedSupplier
import jakarta.ws.rs.BadRequestException

fun errToBadRequest(fn: CheckedRunnable) {
  try {
    fn.run()
  } catch (e: Throwable) {
    throw BadRequestException(e)
  }
}

fun <R> errToBadRequest(fn: CheckedSupplier<R>): R {
  try {
    return fn.get()
  } catch (e: Throwable) {
    throw BadRequestException(e)
  }
}

fun <R> errToRuntime(fn: CheckedSupplier<R>): R {
  try {
    return fn.get()
  } catch (e: Throwable) {
    throw RuntimeException(e)
  }
}

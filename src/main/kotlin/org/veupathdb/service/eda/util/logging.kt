package org.veupathdb.service.eda.util

import org.apache.logging.log4j.LogManager

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T: Any> T.logger() = LogManager.getLogger(javaClass)!!

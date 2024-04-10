package org.veupathdb.service.eda.compute.service

import org.veupathdb.lib.compute.platform.AsyncPlatform
import org.veupathdb.lib.compute.platform.job.JobExecutorFactory
import org.veupathdb.lib.container.jaxrs.config.Options
import org.veupathdb.lib.container.jaxrs.server.ContainerResources
import org.veupathdb.lib.container.jaxrs.server.Server
import org.veupathdb.service.eda.compute.exec.PluginExecutor

object Main : Server() {
  @JvmStatic
  fun main(args: Array<String>) {
    this.enableAccountDB()
    this.enableUserDB()

    this.start(args)
  }

  override fun newResourceConfig(opts: Options?): ContainerResources {
    val out = Resources()
    out.enableAuth()
    // Enabled by default for debugging purposes, this should be removed when
    // production ready.
    out.property("jersey.config.server.tracing.type", "ALL")
      .property("jersey.config.server.tracing.threshold", "VERBOSE")

    return out
  }

  override fun newOptions() = ServiceOptions

  override fun postCliParse(opts: Options) = initAsyncPlatform()

  private fun initAsyncPlatform() {
    AsyncPlatform.init {
      dbConfig {
        dbName   = Main.config.queueDBName
        host     = Main.config.queueDBHost
        port     = Main.config.queueDBPort
        username = Main.config.queueDBUsername
        password = Main.config.queueDBPassword
        poolSize = Main.config.queueDBPoolSize
      }

      s3Config {
        host        = Main.config.s3Host
        bucket      = Main.config.s3Bucket
        accessToken = Main.config.s3AccessToken
        secretKey   = Main.config.s3SecretKey
        port        = Main.config.s3Port
        https       = Main.config.s3UseHttps
      }

      jobConfig {
        executorFactory = JobExecutorFactory { PluginExecutor() }
        expirationDays  = Main.config.jobCacheTimeoutDays
      }

      addQueue {
        id       = Main.config.slowQueueName
        username = Main.config.jobQueueUsername
        password = Main.config.jobQueuePassword
        host     = Main.config.jobQueueHost
        port     = Main.config.jobQueuePort
        workers  = Main.config.slowQueueWorkers
      }

      addQueue {
        id       = Main.config.fastQueueName
        username = Main.config.jobQueueUsername
        password = Main.config.jobQueuePassword
        host     = Main.config.jobQueueHost
        port     = Main.config.jobQueuePort
        workers  = Main.config.fastQueueWorkers
      }
    }
  }
}
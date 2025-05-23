package org.veupathdb.service.eda;

import org.gusdb.fgputil.db.slowquery.QueryLogConfig;
import org.gusdb.fgputil.db.slowquery.QueryLogger;
import org.veupathdb.lib.compute.platform.AsyncPlatform;
import org.veupathdb.lib.compute.platform.config.AsyncDBConfig;
import org.veupathdb.lib.compute.platform.config.AsyncJobConfig;
import org.veupathdb.lib.compute.platform.config.AsyncPlatformConfig;
import org.veupathdb.lib.compute.platform.config.AsyncQueueConfig;
import org.veupathdb.lib.compute.platform.config.AsyncS3Config;
import org.veupathdb.lib.container.jaxrs.config.Options;
import org.veupathdb.lib.container.jaxrs.server.ContainerResources;
import org.veupathdb.lib.container.jaxrs.server.Server;
import org.veupathdb.service.eda.compute.exec.PluginExecutor;

public class Main extends Server {
  public static final EnvConfig config = new EnvConfig();

  public static void main(String[] args) {
    new Main().start(args);
  }

  public Main() {
    QueryLogger.initialize(new QLF() {});
  }

  @Override
  protected ContainerResources newResourceConfig(Options options) {
    return new Resources(config);
  }

  @Override
  protected Options newOptions() {
    return config;
  }

  @Override
  protected void onShutdown() {
    Resources.getDeserializerThreadPool().shutdown();
    Resources.getFileChannelThreadPool().shutdown();
    Resources.getMetadataCache().shutdown();
  }

  @Override
  protected void postStartup() {
    initAsyncPlatform();
  }

  public static class QLF implements QueryLogConfig {
    public double getBaseline() {
      return 0.05D;
    }

    public double getSlow() {
      return 1.0D;
    }

    public boolean isIgnoredSlow(String sql) {
      return false;
    }

    public boolean isIgnoredBaseline(String sql) {
      return false;
    }
  }

  public static final String S3_ROOT_PATH = "/";

  private void initAsyncPlatform() {
    AsyncDBConfig dbConfig = new AsyncDBConfig(
      config.getQueueDBName(),
      config.getQueueDBUsername(),
      config.getQueueDBPassword(),
      config.getQueueDBHost(),
      config.getQueueDBPort(),
      config.getQueueDBPoolSize()
    );
    AsyncS3Config s3Config = new AsyncS3Config(
      config.getS3Host(),
      config.getS3Port(),
      config.getS3UseHttps(),
      config.getS3Bucket(),
      config.getS3AccessToken(),
      config.getS3SecretKey(),
      S3_ROOT_PATH
    );
    AsyncJobConfig jobConfig = new AsyncJobConfig(
      (ctx) -> new PluginExecutor(),
      config.getJobCacheTimeoutDays()
    );
    AsyncPlatformConfig asyncConfig = AsyncPlatformConfig.builder()
      .dbConfig(dbConfig)
      .s3Config(s3Config)
      .jobConfig(jobConfig)
      .addQueue(AsyncQueueConfig.builder()
        .id(config.getSlowQueueName())
        .username(config.getJobQueueUsername())
        .password(config.getJobQueuePassword())
        .host(config.getJobQueueHost())
        .port(config.getJobQueuePort())
        .workers(config.getSlowQueueWorkers())
        .messageAckTimeoutMinutes(config.getSlowQueueJobTimeout())
        .build())
      .addQueue(AsyncQueueConfig.builder()
        .id(config.getFastQueueName())
        .username(config.getJobQueueUsername())
        .password(config.getJobQueuePassword())
        .host(config.getJobQueueHost())
        .port(config.getJobQueuePort())
        .workers(config.getFastQueueWorkers())
        .messageAckTimeoutMinutes(config.getFastQueueJobTimeout())
        .build())
      .build();
    AsyncPlatform.init(asyncConfig);
  }
}

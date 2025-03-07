package org.veupathdb.service.eda;

import org.veupathdb.lib.container.jaxrs.config.Options;
import picocli.CommandLine;

public class EnvConfig extends Options {
  private static final String
    DEFAULT_REGISTRATION_PATH = "/app/user/registration",
    DEFAULT_APPLICATION_PATH  = "/app/study-access";

  private static final String UnconfiguredStringValue = "unconfigured";

  // Database Defaults
  private static final int DefaultQueueDBPort = 5432;
  private static final int DefaultQueueDBPoolSize = 10;

  // Job Queue Defaults
  private static final int DefaultJobQueuePort = 5672;

  private static final String DefaultSlowQueueName = "slow-jobs";
  private static final int DefaultSlowQueueWorkers = 5;
  private static final int DefaultSlowQueueJobTimeoutMinutes = 30;

  private static final String DefaultFastQueueName = "fast-jobs";
  private static final int DefaultFastQueueWorkers = 5;
  private static final int DefaultFastQueueJobTimeoutMinutes = 30;

  // S3 Defaults
  private static final int DefaultS3Port     = 80;
  private static final boolean DefaultS3UseHttps = true;

  // Job Cache Defaults
  private static final int DefaultJobCacheTimeoutDays = 30;


  @CommandLine.Option(
    names = "--enable-email",
    defaultValue = "${env:ENABLE_EMAIL}",
    arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  private boolean enableEmail = true;

  @CommandLine.Option(
    names = "--smtp-host",
    defaultValue = "${env:SMTP_HOST}",
    required = true,
    arity = "1"
  )
  private String smtpHost;

  @CommandLine.Option(
    names = "--mail-debug",
    defaultValue = "${env:EMAIL_DEBUG}",
    arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  private boolean emailDebug = false;

  @CommandLine.Option(
    names = "--support-email",
    defaultValue = "${env:SUPPORT_EMAIL}",
    required = true,
    arity = "1"
  )
  private String supportEmail;

  @CommandLine.Option(
    names = "--site-url",
    defaultValue = "${env:SITE_URL}",
    required = true,
    arity = "1"
  )
  private String siteUrl;

  @CommandLine.Option(
    names = "--registration-path",
    defaultValue = "${env:REGISTRATION_PATH}",
    arity = "1",
    description = "Path to the user registration client app component relative to $SITE_URL."
  )
  @SuppressWarnings("CanBeFinal")
  private String registrationPath = DEFAULT_REGISTRATION_PATH;

  @CommandLine.Option(
    names = "--application-path",
    defaultValue = "${env:APP_PATH}",
    arity = "1",
    description = "Path to the client app component used to manage dataset access relative to $SITE_URL."
  )
  @SuppressWarnings("CanBeFinal")
  private String applicationPath = DEFAULT_APPLICATION_PATH;

  /*┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓*\
    ┃  Service URLs                                                        ┃
  \*┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/

  @CommandLine.Option(
    names = "--subsetting-service-url",
    defaultValue = "${env:SUBSETTING_SERVICE_URL}",
    arity = "1",
    description = "Base URL for internal subsettting service endpoints."
  )
  @SuppressWarnings("CanBeFinal")
  private String edaSubsettingHost = UnconfiguredStringValue;


  @CommandLine.Option(
    names = "--merging-service-url",
    defaultValue = "${env:MERGING_SERVICE_URL}",
    arity = "1",
    description = "Base URL for internal merge service endpoints."
  )
  @SuppressWarnings("CanBeFinal")
  private String edaMergeHost = UnconfiguredStringValue;

  @CommandLine.Option(
    names = "--dataset-access-service-url",
    defaultValue = "${env:DATASET_ACCESS_SERVICE_URL}",
    arity = "1",
    description = "Base URL for access service endpoints."
  )
  @SuppressWarnings("CanBeFinal")
  private String datasetAccessHost = UnconfiguredStringValue;


  @CommandLine.Option(
    names = "--rserve-url",
    defaultValue = "${env:RSERVE_URL}",
    arity = "1",
    description = "Base URL for rserve."
  )
  @SuppressWarnings("CanBeFinal")
  private String rserveHost = UnconfiguredStringValue;

  // region Postgres

  /*┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓*\
    ┃  Queue PostgreSQL                                                    ┃
  \*┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/

  @CommandLine.Option(
      names = "--queue-db-name",
      defaultValue = "${env:QUEUE_DB_NAME}",
      description = "Queue database name",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  private String queueDBName = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--queue-db-host",
      defaultValue = "${env:QUEUE_DB_HOST}",
      description = "Queue database hostname",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String queueDBHost = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--queue-db-port",
      defaultValue = "${env:QUEUE_DB_PORT}",
      description = "Queue database host port",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int queueDBPort = DefaultQueueDBPort;

  @CommandLine.Option(
      names = "--queue-db-username",
      defaultValue = "${env:QUEUE_DB_USERNAME}",
      description = "Queue database username",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String queueDBUsername = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--queue-db-password",
      defaultValue = "${env:QUEUE_DB_PASSWORD}",
      description = "Queue database password",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String queueDBPassword = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--queue-db-pool-size",
      defaultValue = "${env:QUEUE_DB_POOL_SIZE}",
      description = "Queue database pool size",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int queueDBPoolSize = DefaultQueueDBPoolSize;

  // endregion Postgres

  // region RabbitMQ

  /*┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓*\
    ┃  Queue RabbitMQ                                                      ┃
  \*┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/

  @CommandLine.Option(
      names = "--job-queue-username",
      defaultValue = "${env:JOB_QUEUE_USERNAME}",
      description = "Username for the RabbitMQ instance",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String jobQueueUsername;

  @CommandLine.Option(
      names = "--job-queue-password",
      defaultValue = "${env:JOB_QUEUE_PASSWORD}",
      description = "Password for the RabbitMQ instance",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String jobQueuePassword;

  @CommandLine.Option(
      names = "--job-queue-host",
      defaultValue = "${env:JOB_QUEUE_HOST}",
      description = "Hostname for the RabbitMQ instance.",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String jobQueueHost;

  @CommandLine.Option(
      names = "--job-queue-port",
      defaultValue = "${env:JOB_QUEUE_PORT}",
      description = "Host port for the RabbitMQ instance.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int jobQueuePort = DefaultJobQueuePort;

  @CommandLine.Option(
      names = "--slow-queue-name",
      defaultValue = "${env:SLOW_QUEUE_NAME}",
      description = "Name of the slow jobs queue.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  String slowQueueName = DefaultSlowQueueName;

  @CommandLine.Option(
      names = "--slow-queue-workers",
      defaultValue = "${env:SLOW_QUEUE_WORKERS}",
      description = "Number of worker threads used by the slow job queue.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int slowQueueWorkers = DefaultSlowQueueWorkers;

  @CommandLine.Option(
      names = "--fast-queue-name",
      defaultValue = "${env:FAST_QUEUE_NAME}",
      description = "Name of the fast jobs queue.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  String fastQueueName = DefaultFastQueueName;

  @CommandLine.Option(
      names = "--fast-queue-workers",
      defaultValue = "${env:FAST_QUEUE_WORKERS}",
      description = "Number of worker threads used by the fast job queue.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int fastQueueWorkers = DefaultFastQueueWorkers;

  // endregion RabbitMQ

  // region Minio (S3)

  /*┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓*\
    ┃  Queue S3 Instance                                                   ┃
  \*┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/

  @CommandLine.Option(
      names = "--s3-host",
      defaultValue = "${env:S3_HOST}",
      description = "S3 instance hostname",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String s3Host = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--s3-bucket",
      defaultValue = "${env:S3_BUCKET}",
      description = "S3 bucket name",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String s3Bucket = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--s3-access-token",
      defaultValue = "${env:S3_ACCESS_TOKEN}",
      description = "S3 access token",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String s3AccessToken = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--s3-secret-key",
      defaultValue = "${env:S3_SECRET_KEY}",
      description = "S3 secret key",
      arity = "1",
      required = true
  )
  @SuppressWarnings("CanBeFinal")
  String s3SecretKey = UnconfiguredStringValue;

  @CommandLine.Option(
      names = "--s3-port",
      defaultValue = "${env:S3_PORT}",
      description = "S3 host port",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int s3Port = DefaultS3Port;

  @CommandLine.Option(
      names = "--s3-use-https",
      defaultValue = "${env:S3_USE_HTTPS}",
      description = "Whether the platform should use HTTPS when connecting to S3",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  boolean s3UseHttps = DefaultS3UseHttps;

  // endregion Minio (S3)

  // region Job Configuration

  /*┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓*\
    ┃  Queue Job Configuration                                             ┃
  \*┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/

  @CommandLine.Option(
      names = "--job-cache-timeout-days",
      defaultValue = "${env:JOB_CACHE_TIMEOUT_DAYS}",
      description = "Number of days a job will be kept in the cache after it was last accessed.",
      arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int jobCacheTimeoutDays = DefaultJobCacheTimeoutDays;

  @CommandLine.Option(
    names = "--slow-queue-job-timeout",
    defaultValue = "${env:SLOW_QUEUE_JOB_TIMEOUT_MINUTES}",
    description = "Max number of minutes a slow queue job may take to complete before the job message from the queue is skipped.",
    arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int slowQueueJobTimeout = DefaultSlowQueueJobTimeoutMinutes;

  @CommandLine.Option(
    names = "--fast-queue-job-timeout",
    defaultValue = "${env:FAST_QUEUE_JOB_TIMEOUT_MINUTES}",
    description = "Max number of minutes a fast queue job may take to complete before the job message from the queue is skipped.",
    arity = "1"
  )
  @SuppressWarnings("CanBeFinal")
  int fastQueueJobTimeout = DefaultFastQueueJobTimeoutMinutes;

  // endregion Job Configuration

  public boolean isEmailEnabled() {
    return enableEmail;
  }

  public String getSmtpHost() {
    return smtpHost;
  }

  public boolean isEmailDebug() {
    return emailDebug;
  }

  public String getSupportEmail() {
    return supportEmail;
  }

  public String getSiteUrl() {
    return siteUrl;
  }

  public String getRegistrationPath() {
    return registrationPath;
  }

  public String getApplicationPath() {
    return applicationPath;
  }

  public String getQueueDBName() {
    return queueDBName;
  }

  public String getQueueDBUsername() {
    return queueDBUsername;
  }

  public String getQueueDBPassword() {
    return queueDBPassword;
  }

  public String getQueueDBHost() {
    return queueDBHost;
  }

  public int getQueueDBPort() {
    return queueDBPort;
  }

  public int getQueueDBPoolSize() {
    return queueDBPoolSize;
  }

  public String getS3Host() {
    return s3Host;
  }

  public int getS3Port() {
    return s3Port;
  }

  public boolean getS3UseHttps() {
    return s3UseHttps;
  }

  public String getS3Bucket() {
    return s3Bucket;
  }

  public String getS3AccessToken() {
    return s3AccessToken;
  }

  public String getS3SecretKey() {
    return s3SecretKey;
  }

  public String getSlowQueueName() {
    return slowQueueName;
  }

  public String getJobQueueUsername() {
    return jobQueueUsername;
  }

  public String getJobQueuePassword() {
    return jobQueuePassword;
  }

  public String getJobQueueHost() {
    return jobQueueHost;
  }

  public int getJobQueuePort() {
    return jobQueuePort;
  }

  public int getSlowQueueWorkers() {
    return slowQueueWorkers;
  }

  public String getFastQueueName() {
    return fastQueueName;
  }

  public int getFastQueueWorkers() {
    return fastQueueWorkers;
  }

  public int getJobCacheTimeoutDays() {
    return jobCacheTimeoutDays;
  }

  public String getEdaSubsettingHost() {
    return edaSubsettingHost;
  }

  public String getEdaMergeHost() {
    return edaMergeHost;
  }

  public String getDatasetAccessHost() {
    return datasetAccessHost;
  }

  public String getRServeHost() {
    return rserveHost;
  }

  public int getSlowQueueJobTimeout() {
    return slowQueueJobTimeout;
  }

  public int getFastQueueJobTimeout() {
    return fastQueueJobTimeout;
  }
}

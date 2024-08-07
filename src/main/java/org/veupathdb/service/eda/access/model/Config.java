package org.veupathdb.service.eda.access.model;

import org.veupathdb.lib.container.jaxrs.config.Options;
import picocli.CommandLine;

public class Config extends Options
{
  private static final String
    DEFAULT_REGISTRATION_PATH = "/app/user/registration",
    DEFAULT_APPLICATION_PATH  = "/app/study-access";

  @CommandLine.Option(
    names = "--enable-email",
    defaultValue = "true",   // do not use ENABLE_EMAIL, set true to permanently enable e-mail, ignoring env var. Testing can be done with fake smtp
    arity = "1"
  )
  @SuppressWarnings("FieldMayBeFinal")
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
  @SuppressWarnings("FieldMayBeFinal")
  private boolean emailDebug = true;

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
  @SuppressWarnings("FieldMayBeFinal")
  private String registrationPath = DEFAULT_REGISTRATION_PATH;

  @CommandLine.Option(
    names = "--application-path",
    defaultValue = "${env:APP_PATH}",
    arity = "1",
    description = "Path to the client app component used to manage dataset access relative to $SITE_URL."
  )
  @SuppressWarnings("FieldMayBeFinal")
  private String applicationPath = DEFAULT_APPLICATION_PATH;

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
}

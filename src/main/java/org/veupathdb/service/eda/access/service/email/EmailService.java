package org.veupathdb.service.eda.access.service.email;


import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.gusdb.fgputil.FormatUtil;
import org.slf4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.eda.Main;
import org.veupathdb.service.eda.access.model.Dataset;
import org.veupathdb.service.eda.access.model.Email;
import org.veupathdb.service.eda.access.model.EndUserRow;
import org.veupathdb.service.eda.access.model.MessageTemplate;
import org.veupathdb.service.eda.access.util.Format;

public class EmailService
{
  private static EmailService instance;

  private static final Logger log = LogProvider.logger(EmailService.class);

  public void sendEndUserRegistrationEmail(final String address, final Dataset dataset)
  throws Exception {
    log.info("EmailService#sendEndUserRegistrationEmail(String, Dataset)");

    final var template = Const.EndUserTemplate;
    final var util     = EmailUtil.getInstance();

    log.info("Sending email");
    sendEmail(new Email()
      .setSubject(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.subject())
        .build()))
      .setBody(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.body())
        .build()))
      .setFrom(dataset.getProperties().get(Dataset.Property.REQUEST_EMAIL))
      .setTo(new String[]{address}));
  }

  public void sendProviderRegistrationEmail(final String address, final Dataset dataset)
  throws Exception {
    log.trace("EmailService#sendProviderRegistrationEmail(String, Dataset)");

    final var template = Const.ProviderTemplate;
    final var util     = EmailUtil.getInstance();

    sendEmail(new Email()
      .setSubject(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.subject())
        .build()))
      .setBody(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.body())
        .build()))
      .setFrom(dataset.getProperties().get(Dataset.Property.REQUEST_EMAIL))
      .setTo(new String[]{address}));
  }

  public void sendEndUserUpdateNotificationEmail(
    final String[] cc,
    final Dataset dataset,
    final EndUserRow user,
    final String userSpecificContent
  ) throws Exception {
    log.trace("EmailService#sendEndUserUpdateNotificationEmail(String[], Dataset, EndUserRow)");

    final var template = Const.EditNotification;
    final var util     = EmailUtil.getInstance();

    sendEmail(new Email()
      .setSubject(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.subject())
        .build()))
      .setBody(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.body())
        .withEndUserRow(user)
        .withUserSpecificContent(userSpecificContent)
        .build()))
      .setTo(
        Stream.concat(Arrays.stream(cc), Stream.of(Main.config.getSupportEmail()))
          .distinct()
          .toArray(String[]::new)
      )
      .setFrom(dataset.getProperties().get(Dataset.Property.REQUEST_EMAIL)));
  }

  public void sendDatasetApprovedNotificationEmail(final String[] cc,
                                                   final Dataset dataset,
                                                   final EndUserRow user,
                                                   final String[] managerEmails) throws Exception {
    sendNotificationEmail(cc, dataset, user, managerEmails, Const.ApproveNotification);
  }

  public void sendDatasetDeniedNotificationEmail(final String[] cc,
                                                 final Dataset dataset,
                                                 final EndUserRow user,
                                                 final String[] managerEmails) throws Exception {
    sendNotificationEmail(cc, dataset, user, managerEmails, Const.DenyNotification);
  }

  public void sendEmail(final Email mail) throws Exception {
    log.trace("EmailService#sendEmail(Email)");
    if (!Main.config.isEmailEnabled()) {
      log.warn("Per configuration, email is disabled. Would have sent {}.", Format.Json.writeValueAsString(mail));
      return;
    }

    log.debug("Sending e-mail with subject: {}", mail.getSubject());
    final var props = new Properties();
    try {
      final var util = EmailUtil.getInstance();

      props.put("mail.smtp.host", Main.config.getSmtpHost());
      props.put("mail.debug", String.valueOf(Main.config.isEmailDebug()));

      final var session = Session.getInstance(props);

      final var message = util.prepMessage(session, mail);

      message.setFrom(mail.getFrom());
      message.setRecipients(MimeMessage.RecipientType.TO, util.toAddresses(mail.getTo()));
      message.setRecipients(MimeMessage.RecipientType.CC, util.toAddresses(mail.getCc()));
      message.setRecipients(MimeMessage.RecipientType.BCC, util.toAddresses(mail.getBcc()));
      message.setReplyTo(util.toAddresses(new String[]{ Main.config.getSupportEmail() }));
      message.setSubject(mail.getSubject());

      Transport.send(message);
    }
    catch (Exception e) {
      log.error("Failed to create and send email message using config: {}", FormatUtil.prettyPrint(props, FormatUtil.Style.MULTI_LINE), e);
      throw e;
    }
  }

  public static EmailService getInstance() {
    if (instance == null)
      instance = new EmailService();

    return instance;
  }

  private void sendNotificationEmail(
    final String[] cc,
    final Dataset dataset,
    final EndUserRow user,
    final String[] managerEmails,
    final MessageTemplate template
  ) throws Exception {
    final var util = EmailUtil.getInstance();

    sendEmail(new Email()
      .setSubject(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.subject())
        .build()))
      .setBody(util.populateTemplate(EmailUtil.TemplateInput.newBuilder()
        .withDataset(dataset)
        .withTemplate(template.body())
        .withEndUserRow(user)
        .withManagerEmails(managerEmails)
        .build()))
      .setTo(cc)
      .setCc(managerEmails)
      .setFrom(dataset.getProperties().get(Dataset.Property.REQUEST_EMAIL)));
  }
}

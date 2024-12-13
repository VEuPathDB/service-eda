package org.veupathdb.service.eda.access.service.email;

import org.slf4j.Logger;
import org.stringtemplate.v4.ST;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.eda.Main;
import org.veupathdb.service.eda.access.model.Dataset;
import org.veupathdb.service.eda.access.model.Email;
import org.veupathdb.service.eda.access.model.EndUserRow;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;

public class EmailUtil
{
  private static final char TEMPLATE_DELIM = '$';

  private static EmailUtil instance;

  private final Logger log;

  public EmailUtil() {
    log = LogProvider.logger(EmailUtil.class);
  }

  public String populateTemplate(TemplateInput templateInput) {
    final var inject = new ST(templateInput.template, TEMPLATE_DELIM, TEMPLATE_DELIM);
    inject.add("dataset", templateInput.dataset);
    inject.add("site-url", Main.config.getSiteUrl());
    inject.add("user-specific-content", templateInput.userSpecificContent);
    inject.add("sign-up-link", URI.create(makeUrl(Main.config.getRegistrationPath())));
    inject.add("app-link", URI.create(makeUrl(Main.config.getApplicationPath())));

    if (templateInput.managerEmails != null) {
      inject.add("manager-emails", String.join(", ", templateInput.managerEmails));
    }
    if (templateInput.endUserRow != null) {
      inject.add("end-user", templateInput.endUserRow);
    }

    return inject.render();
  }

  public static EmailUtil getInstance() {
    if (instance == null)
      instance = new EmailUtil();

    return instance;
  }


  public Address[] toAddresses(final String[] emails) throws Exception {
    log.trace("EmailUtil#toAddresses(String[])");

    var addresses = new Address[emails.length];

    for (var i = 0; i < emails.length; i++)
         addresses[i] = new InternetAddress(emails[i]);

    return addresses;
  }

  public MimeMessage prepMessage(final Session ses, final Email mail) throws Exception {
    log.trace("EmailUtil#prepMessage(Session, Email)");

    var message = new MimeMessage(ses);

    message.setFrom(mail.getFrom());

    message.setReplyTo(toAddresses(new String[]{Main.config.getSupportEmail()}));
    message.setRecipients(Message.RecipientType.TO, toAddresses(mail.getTo()));
    message.setSubject(mail.getSubject());
    message.setSentDate(new Date());

    if (mail.getCc() != null && mail.getCc().length > 0)
      message.setRecipients(Message.RecipientType.CC, toAddresses(mail.getCc()));
    if (mail.getBcc() != null && mail.getBcc().length > 0)
      message.setRecipients(Message.RecipientType.BCC, toAddresses(mail.getBcc()));

    var bodyPart = new MimeBodyPart();
    bodyPart.setDataHandler(new DataHandler(new HTMLDataSource(mail.getBody())));
    message.setContent(new MimeMultipart(bodyPart));

    return message;
  }

  String makeUrl(final String path) {
    return Main.config.getSiteUrl() + (path.charAt(0) == '/' ? path : "/" + path);
  }

  private static class HTMLDataSource implements javax.activation.DataSource {

    private final String html;

    public HTMLDataSource(String htmlString) {
      html = htmlString;
    }

    // Return html string in an InputStream.
    // A new stream must be returned each time.
    @Override
    public InputStream getInputStream() throws IOException {
      if (html == null)
        throw new IOException("Null HTML");
      return new ByteArrayInputStream(html.getBytes());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
      throw new IOException("This DataHandler cannot write HTML");
    }

    @Override
    public String getContentType() {
      return "text/html";
    }

    @Override
    public String getName() {
      return "JAF text/html dataSource to send e-mail only";
    }
  }


  /**
   * Variables whose values may be substituted into e-mail templates.
   */
  public static class TemplateInput {
    private final String template;
    private final String[] managerEmails;
    private final Dataset dataset;
    private final EndUserRow endUserRow;
    private final String userSpecificContent;

    private TemplateInput(SecondStepTemplateBuilder builder) {
      template = builder.template;
      managerEmails = builder.managerEmails;
      dataset = builder.dataset;
      endUserRow = builder.endUserRow;
      userSpecificContent = builder.userSpecificContent;
    }

    public static FirstStepTemplateBuilder newBuilder() {
      return new FirstStepTemplateBuilder();
    }

    /**
     * The first step of the builder is to provide the dataset, as some subsequent builder methods depend on the
     * dataset.
     */
    public static final class FirstStepTemplateBuilder {
      // Unlock second step builder methods after dataset is specified.
      public SecondStepTemplateBuilder withDataset(Dataset val) {
        return new SecondStepTemplateBuilder(val);
      }
    }

    public static final class SecondStepTemplateBuilder {
      private final Dataset dataset;
      private String template;
      private String[] managerEmails;
      private EndUserRow endUserRow;
      private String userSpecificContent;

      private SecondStepTemplateBuilder(Dataset dataset) {
        this.dataset = dataset;
      }

      public SecondStepTemplateBuilder withTemplate(String val) {
        template = val;
        return this;
      }

      public SecondStepTemplateBuilder withManagerEmails(String[] val) {
        managerEmails = val;
        return this;
      }

      public SecondStepTemplateBuilder withEndUserRow(EndUserRow val) {
        endUserRow = val;
        return this;
      }

      public SecondStepTemplateBuilder withUserSpecificContent(String val) {
        if (val != null) {
          // This is a bit of hack. Since this content is coming from the dataset presenters which uses a different
          // style of templating from this service. We do the variable substitution here for the variables we expect
          // to be present in this field.
          userSpecificContent = val.replaceAll("\\$\\$DATASET_ID\\$\\$", dataset.getDatasetId());
          if (dataset.getDaysForApproval() != null) {
            userSpecificContent = userSpecificContent.replaceAll("\\$\\$DAYS_FOR_APPROVAL\\$\\$", dataset.getDaysForApproval());
          }
        }
        return this;
      }


      public TemplateInput build() {
        return new TemplateInput(this);
      }
    }
  }
}

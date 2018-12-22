package com.reigninbinary.cloud.aws.ses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.Template;
import com.amazonaws.services.simpleemail.model.UpdateTemplateRequest;
import com.reigninbinary.cloud.aws.AwsCloudException;
import com.reigninbinary.cloud.aws.ses.SesEmailTemplate;
import com.reigninbinary.cloud.aws.ses.SesRenderedTemplate;
import com.reigninbinary.core.CoreLogging;

public class SesClient {

	private static final AmazonSimpleEmailService client;
	static {
		String region = SesEnv.getSesRegion();
		if (StringUtils.isNotBlank(region)) {
			client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region).build();
		} else {
			client = AmazonSimpleEmailServiceClientBuilder.standard().build();
		}
	}

	public SesClient() {
	}

	public void deleteEmailTemplate(String templateName) {

		client.deleteTemplate(new DeleteTemplateRequest().withTemplateName(templateName));
	}

	public void updateEmailTemplate(SesEmailTemplate emailTemplate) {

		Template template = new Template().withTemplateName(emailTemplate.getTemplateName())
				.withSubjectPart(emailTemplate.getSubject()).withTextPart(emailTemplate.getText())
				.withHtmlPart(emailTemplate.getHtml());

		client.updateTemplate(new UpdateTemplateRequest().withTemplate(template));
	}

	public void createEmailTemplate(SesEmailTemplate emailTemplate) {

		Template template = new Template().withTemplateName(emailTemplate.getTemplateName())
				.withSubjectPart(emailTemplate.getSubject()).withTextPart(emailTemplate.getText())
				.withHtmlPart(emailTemplate.getHtml());

		client.createTemplate(new CreateTemplateRequest().withTemplate(template));
	}

	public SesRenderedTemplate testRenderTemplate(SesEmailTemplate template, Map<String, Object> mapTemplateData)
			throws AwsCloudException {

		SesRenderedTemplate renderedTemplate = new SesRenderedTemplate(template, mapTemplateData);
		return renderedTemplate;
	}

	// Not necessary since we're using Mustache to render templates now.
	@SuppressWarnings("unused")
	private void sendTemplatedEmail(SesEmailInfo emailInfo, SesEmailTemplate emailTemplate) {

		SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
				.withConfigurationSetName(SesEnv.getConfigSet()).withSource(SesEnv.getFromEmailAddress())
				.withDestination(new Destination().withToAddresses(emailInfo.getEmailAddress()))
				.withTemplate(emailTemplate.getTemplateName()).withTemplateData(emailInfo.toJsonString());
		client.sendTemplatedEmail(request);
	}

	public void sendEmailUsingTemplate(SesEmailInfo emailInfo, SesEmailTemplate emailTemplate) throws AwsCloudException {

		SesRenderedTemplate renderedTemplate = new SesRenderedTemplate(emailTemplate, emailInfo);
		sendRawEmail(emailInfo, renderedTemplate);
	}
	
	private Address[] getBccEmailAddresses(String[] bccEmailAddresses) {

		Address[] addresses = null;

		if (bccEmailAddresses != null && bccEmailAddresses.length > 0) {
			
			ArrayList<Address> addressList = new ArrayList<>();
			
			for (String email : bccEmailAddresses) {
				try {
					addressList.add(new InternetAddress(email.trim()));
				} 
				catch (AddressException e) {
					String FMT = "inavlid bcc email address: %s; %s";
					CoreLogging.logSevere(String.format(FMT, email, e.getMessage()));
				}
			}
			
			addresses = (Address[]) addressList.toArray(new Address[addressList.size()]);
		}
		
		return addresses;
	}

	private Address[] getCcEmailAddresses(String[] ccEmailAddresses) {

		Address[] addresses = null;

		if (ccEmailAddresses != null && ccEmailAddresses.length > 0) {
			
			ArrayList<Address> addressList = new ArrayList<>();
			
			for (String email : ccEmailAddresses) {
				try {
					addressList.add(new InternetAddress(email.trim()));
				}
				catch (AddressException e) {
					String FMT = "inavlid bcc email address: %s; %s";
					CoreLogging.logSevere(String.format(FMT, email, e.getMessage()));
				}
			}
			
			addresses = (Address[]) addressList.toArray(new Address[addressList.size()]);
		}
		
		return addresses;
	}

	private void sendRawEmail(SesEmailInfo emailInfo, 
			SesRenderedTemplate renderedTemplate) throws AwsCloudException {

		SesAttachment attachment = null;
		if (SesEnv.isEmailAttachmentsEnabled()) {
			try {
				attachment = SesAttachments.getAttachment(emailInfo.getAttachmentDirectory(),
						emailInfo.getAttachmentFilename());
			} 
			catch (AwsCloudException e) {
				
				// TODO: make this configurable
				// continue to send email without attachment
				
				CoreLogging.logSevere(
					String.format("failed to download attachment from S3; email: %s, attdir: %s, attfname: %s",
					emailInfo.getEmailAddress(), emailInfo.getAttachmentDirectory(),
					emailInfo.getAttachmentFilename()));
			}
		}

		try {
			sendRawEmail(emailInfo.getEmailAddress(), 
					getCcEmailAddresses(emailInfo.getCcAddresses()),
					getBccEmailAddresses(emailInfo.getBccAddresses()),
					renderedTemplate.getSubject(), renderedTemplate.getText(),
					renderedTemplate.getHtml(), attachment);
		} 
		catch (MessagingException e) {
			
			throw new AwsCloudException(e);
		} 
		catch (IOException e) {
			
			throw new AwsCloudException(e);
		}
	}

	private String getToAddress(String toAddress) {

		String toAddressDebug = SesEnv.getToEmailAddressDebug();
		
		if (StringUtils.isNotBlank(toAddressDebug)) {
			CoreLogging.logInfo(String.format("Using debug TO address: %s", toAddressDebug));
			return toAddressDebug;
		}
		return toAddress;
	}
	
	private void sendRawEmail(String toAddress, 
			Address[] ccAddresses, Address[] bccAddresses,
			String subject, String textEmailPart, String htmlEmailPart,
			SesAttachment attachment) throws MessagingException, IOException {

		final String UTF8 = "UTF-8";
		final String MIXED = "mixed";
		final String ALTERNATIVE = "alternative";
		final String HTML_CONTENT = "text/html; charset=UTF-8";
		final String TEXT_CONTENT = "text/plain; charset=UTF-8";

		Session session = Session.getDefaultInstance(new Properties());

		// Create a new MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		message.setSubject(subject, UTF8);
		message.setFrom(new InternetAddress(SesEnv.getFromEmailAddress(), SesEnv.getFromName()));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getToAddress(toAddress)));
		
		if (ccAddresses != null && ccAddresses.length > 0) {
			message.addRecipients(RecipientType.CC, ccAddresses);
		}

		if (bccAddresses != null && bccAddresses.length > 0) {
			message.addRecipients(RecipientType.BCC, bccAddresses);
		}

		// Create a multipart/alternative child container.
		MimeMultipart msg_body = new MimeMultipart(ALTERNATIVE);

		// Create a wrapper for the HTML and text parts.
		MimeBodyPart wrap = new MimeBodyPart();

		// Define the text part.
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(textEmailPart, TEXT_CONTENT);
		msg_body.addBodyPart(textPart);

		// Define the html part.
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(htmlEmailPart, HTML_CONTENT);
		msg_body.addBodyPart(htmlPart);

		// Add the child container to the wrapper object.
		wrap.setContent(msg_body);

		// Create a multipart/mixed parent container.
		MimeMultipart msg = new MimeMultipart(MIXED);

		// Add the parent container to the message.
		message.setContent(msg);

		// Add the multipart/alternative part to the message.
		msg.addBodyPart(wrap);

		if (attachment != null) {
			MimeBodyPart att = new MimeBodyPart();
			att.setDataHandler(new DataHandler(attachment));
			att.setFileName(attachment.getName());
			msg.addBodyPart(att);
		}

		// Print the raw email content on the console
		// PrintStream out = System.out;
		// message.writeTo(out);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		message.writeTo(outputStream);

		RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

		SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage)
				.withConfigurationSetName(SesEnv.getConfigSet());

		client.sendRawEmail(rawEmailRequest);
	}
}
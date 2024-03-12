package org.tm30.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.tm30.security.data.EmailRequestData;
import org.tm30.security.data.WelcomeRequestData;
import org.tm30.security.service.EmailService;
import org.tm30.security.util.FileUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;

	@Value("${favourite.company.email}")
	private String companyEmail;

	@Override
	public String sendEmail(EmailRequestData data) {
		MimeMessage message = mailSender.createMimeMessage();

		if (data == null) {
			return "Email Request is null";
		}
		String recipientEmailAddress = data.getTo();
		String subject = data.getSubject();
		String body = data.getBody();

		try {
			String validationResult = data.validate();
			if (!validationResult.equalsIgnoreCase("valid")) {
				return validationResult;
			}

			message.setFrom(new InternetAddress(companyEmail));
			message.setRecipients(MimeMessage.RecipientType.TO, recipientEmailAddress);
			message.setSubject(subject);
			message.setContent(body, "text/html; charset=utf-8");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		mailSender.send(message);
		return "message sent";
	}

	private String sendEmailInHtml(EmailRequestData data, String pathToHtmlFile) {
		MimeMessage message = mailSender.createMimeMessage();

		if (data == null) {
			return "Email Request is null";
		}
		String recipientEmailAddress = data.getTo();
		String subject = data.getSubject();
		String body = data.getBody();

		FileUtils fileUtils = new FileUtils();
		try {

			String htmlString = fileUtils.readFileToString(pathToHtmlFile);
			String validationResult = data.validate();
			if (!validationResult.equalsIgnoreCase("valid")) {
				return validationResult;
			}

			message.setFrom(new InternetAddress(companyEmail));
			message.setRecipients(MimeMessage.RecipientType.TO, recipientEmailAddress);
			message.setSubject(subject);
			message.setContent(htmlString, "text/html; charset=utf-8");

			mailSender.send(message);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
		return "message sent";
	}

	@Override
	public String sendEmailWithAttachment(EmailRequestData data, String attachmentPath) {
		String to = data.getTo();
		String subject = data.getSubject();
		String body = data.getBody();

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);

			FileSystemResource file = new FileSystemResource(new File(attachmentPath));
			helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		mailSender.send(message);
		return "Email Sent";
	}

	@Override
	public String sendWelcomeEmail(WelcomeRequestData requestData) {
		FileUtils fileUtils = new FileUtils();

		String to = requestData.getTo();
		String subject = requestData.getSubject();
		MimeMessage message = mailSender.createMimeMessage();

		try {

			String validationResult = requestData.validate();
			if (!validationResult.equalsIgnoreCase("valid")) {
				return validationResult;
			}
			log.info("companyEmail: {}", companyEmail);

			message.setFrom(new InternetAddress(companyEmail));
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setSubject(subject);

			String pathToHtmlTemplate = "src/main/resources/templates/email/welcome.html";
			String htmlString = fileUtils.readFileToString(pathToHtmlTemplate);

			// log.info("htmlString: {} ", htmlString);
			htmlString = htmlString.replace("@displayName", requestData.getDisplayName());
			htmlString = htmlString.replace("@activationUrl", requestData.getActivateUrl());

			log.info("htmlString: {} ", htmlString);
			message.setContent(htmlString, "text/html; charset=utf-8");

			// new Thread(() -> mailSender.send(message)).start();

			mailSender.send(message);
		} catch (Exception ex) {
			log.info("ERROR: {}", ex);
			// throw new RuntimeException();
		}
		return "Mail Sent";
	}

	// send to user pdf file
	private void jSoupHtmlTemplate(String pathhtmlTemplateFile) {
		FileUtils fileUtils = new FileUtils();
		StringBuilder htmlStringBuilder = new StringBuilder();
		try {

			pathhtmlTemplateFile = "src/main/resources/statement/statement.html";
			String htmlString = fileUtils.readFileToString(pathhtmlTemplateFile);

			String pdfName = "statement";
			String path = "src/main/resources/statement/pdf/" + pdfName + ".html";
			String pathPDF = "src/main/resources/statement/pdf/" + pdfName + ".pdf";

			File newHtmlFile = new File(path);
			fileUtils.writeStringToFile(newHtmlFile, htmlString);
			String html = new String(Files.readAllBytes(Paths.get(path)));
			Document document = Jsoup.parse(html);

			document.getElementById("tableloan").append(htmlStringBuilder.toString());
			document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

			fileUtils.toPDF(document, pathPDF);
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
}

package org.tm30.security.service;


import org.tm30.security.data.EmailRequestData;
import org.tm30.security.data.WelcomeRequestData;

public interface EmailService {
	String sendEmail(EmailRequestData data);

	String sendEmailWithAttachment(EmailRequestData data, String attachmentPath);

	String sendWelcomeEmail(WelcomeRequestData requestData);
}

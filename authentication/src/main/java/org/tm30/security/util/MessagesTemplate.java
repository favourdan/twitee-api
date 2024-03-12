package org.tm30.security.util;

import lombok.extern.slf4j.Slf4j;
import org.tm30.security.domain.AppUser;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class MessagesTemplate {
	public String welcomeMessageTemplate(AppUser appUser, String token, HttpServletRequest request) {
		String message = "<html> " + "<body>" + "<h5>Hi " + appUser.getName()
				+ ",<h5> <br>" + "<p>Thank you for your interest in joining Twitee."
				+ "To complete your registration, we need you to verify your email address \n"
				+ "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY YOUR ACCOUNT. </a><p>" + "</body> " + "</html>";

		String url = getApplicationUrl(request) + "verify-registration?token=" + token;
		log.info("VERIFY_URL: {}", url);
		message = message.replace("[[TOKEN_URL]]", url);
		return message;
	}

	private String getApplicationUrl(HttpServletRequest request) {
		// return "http://" + request.getServerName() + ":3000";
		return "http://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/auth/";
	}
}

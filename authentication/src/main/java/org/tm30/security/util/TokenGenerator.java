package org.tm30.security.util;

import org.springframework.stereotype.Component;
import org.tm30.security.domain.Token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class TokenGenerator {
	public Token generateToken(Long time, ChronoUnit timeType, Long appUserId) {
		String genToken = UUID.randomUUID().toString();

		Token token = Token.builder().token(genToken).startTime((System.currentTimeMillis() * 1000))
				.expirationTime(Instant.now().plus(time, timeType).getEpochSecond()).appUserId(appUserId).build();

		return token;
	}
}

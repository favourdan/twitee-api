//package org.tm30.util;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Optional;
//
//@Slf4j
//public class AppContextUser {
//	@Bean
//	public Optional<String> extractEmailFromPrincipal() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		log.info("AUTHENTICATION: {}", authentication);
//		if (!(authentication instanceof AnonymousAuthenticationToken))
//			return Optional.of(authentication.getName());
//		return Optional.empty();
//	}
//}

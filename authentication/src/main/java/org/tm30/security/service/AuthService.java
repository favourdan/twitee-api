package org.tm30.security.service;

import org.springframework.http.ResponseEntity;
import org.tm30.security.data.LoginData;
import org.tm30.security.data.RegistrationData;
import org.tm30.security.domain.AppUser;
import org.tm30.security.util.data.CommandResult;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

	ResponseEntity<CommandResult> loginUserIn(LoginData loginData);

	ResponseEntity<CommandResult> register(RegistrationData registerData, HttpServletRequest request);

	ResponseEntity<CommandResult> verifyUserVerificationToken(String token);

    ResponseEntity<AppUser> isUserAuthenticated();

    ResponseEntity<String> validateToken(String jwtToken);
}

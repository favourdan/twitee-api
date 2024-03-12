package org.tm30.security.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tm30.security.data.LoginData;
import org.tm30.security.data.RegistrationData;
import org.tm30.security.domain.AppUser;
import org.tm30.security.service.AuthService;
import org.tm30.security.util.data.CommandResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/")
@CrossOrigin
public class AuthController {
	private final AuthService authService;

	@PostMapping("login")
	public ResponseEntity<CommandResult> login(@RequestBody LoginData loginData) {
		return authService.loginUserIn(loginData);
	}

	@PostMapping("register")
	public ResponseEntity<CommandResult> register(@RequestBody @Valid RegistrationData registerData,
			HttpServletRequest request) {
		return authService.register(registerData, request);
	}

	@GetMapping("verify-registration")
	public ResponseEntity<CommandResult> verifyAccount(@RequestParam String token) {
		return authService.verifyUserVerificationToken(token);
	}

	@PostMapping("isAuthenticated")
	public ResponseEntity<AppUser> contextAuthenticated() {
		return authService.isUserAuthenticated();
	}

	@PostMapping("validate-user")
	public ResponseEntity<String> validateJwt(@RequestParam String jwtToken) {
		return authService.validateToken(jwtToken);
	}

}

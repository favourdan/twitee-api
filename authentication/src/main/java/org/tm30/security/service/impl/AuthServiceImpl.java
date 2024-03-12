package org.tm30.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tm30.security.data.EmailRequestData;
import org.tm30.security.data.LoginData;
import org.tm30.security.data.RegistrationData;
import org.tm30.security.domain.AppUser;
import org.tm30.security.domain.Token;
import org.tm30.security.enums.Role;
import org.tm30.security.exception.AbstractPlatformException;
import org.tm30.security.exception.ConstraintValidationException;
import org.tm30.security.repository.AppUserRepository;
import org.tm30.security.repository.TokenRepository;
import org.tm30.security.service.AuthService;
import org.tm30.security.service.EmailService;
import org.tm30.security.tokens.JwtTokenService;
import org.tm30.security.util.MessagesTemplate;
import org.tm30.security.util.TokenGenerator;
import org.tm30.security.util.data.CommandResult;
import org.tm30.security.util.data.CommandResultBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;
	private final AppUserRepository appUserRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessagesTemplate messagesTemplate = new MessagesTemplate();
	private final TokenGenerator tokenGenerator;
	private final EmailService emailService;

	@Override
	public ResponseEntity<CommandResult> loginUserIn(LoginData loginData) {

		try {

			UserDetails user = userDetailsService.loadUserByUsername(loginData.getEmail());
			if (!user.isEnabled())
				throw new UsernameNotFoundException(
						"error.user.not.verified.or.active: Check your email to be verified!");
			if (!user.isAccountNonLocked()) {
				throw new AbstractPlatformException("error.message.invalid.account",
						"Please contact the administrator");
			}

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));
			log.info("authentication: {}", authentication);
			if (authentication == null) {
				throw new AbstractPlatformException("error.msg.invalid.email.or.password",
						"Email or Password Invalid!");
			} else {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String token = this.jwtTokenService.generateToken(authentication);
				return ResponseEntity.ok(new CommandResultBuilder().response("Login Successful").resourceId("200L")
						.token(token).build());

			}

		} catch (BadCredentialsException e) {
			throw new AbstractPlatformException("error.msg.auth.login", e.getMessage(), 401);
		} catch (UsernameNotFoundException e) {
			throw new AbstractPlatformException("error.msg.login", e.getMessage(), 404);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<CommandResult> register(RegistrationData registerData, HttpServletRequest request) {

		String email = registerData.getEmail();
		String name = registerData.getName();
		String password = registerData.getPassword();

		boolean existsByEmail = this.appUserRepository.existsByEmail(email);
		if (BooleanUtils.isTrue(existsByEmail)) {
			throw new AbstractPlatformException("error.msg.user.email.already.exists",
					"User with email " + email + " already exists", 409);
		}

		String role = "Client";
		String username = email.split("@")[0];

		// todo: Create and add cart
		AppUser newAppuser = AppUser.builder().name(name).email(email)
				.username(username)
				.password(passwordEncoder
				.encode(password)).role(Role.CLIENT).build();
		newAppuser = appUserRepository.save(newAppuser);

		Token token = tokenGenerator.generateToken(300L, ChronoUnit.SECONDS, newAppuser.getId());
		tokenRepository.save(token);

		// todo: sendEmail To user
		String messageBody = messagesTemplate.welcomeMessageTemplate(newAppuser, token.getToken(), request);

		EmailRequestData emailRequestData = EmailRequestData.builder().to(newAppuser.getEmail())
				.from("noreply@favourite-collections.ng").subject("WELCOME TO TWITEE APP").body(messageBody)
				.build();

		//emailService.sendEmail(emailRequestData);
		log.info("email: {}", emailRequestData);
		return ResponseEntity.ok(new CommandResultBuilder().entityId(newAppuser.getId())
				.response("Registration Successful").message("Check your email to get verified").build());
	}

	@Override
	public ResponseEntity<CommandResult> verifyUserVerificationToken(String token) {
		Token verificationToken = getToken(token);
		Long id = verificationToken.getAppUserId();
		AppUser appUser = appUserRepository.findById(id == null ? 0L : id)
				.orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

		appUser.setIsVerified(true);
		appUser.setIsActive(true);
		appUserRepository.save(appUser);

		tokenRepository.delete(verificationToken);

		return ResponseEntity
				.ok(new CommandResultBuilder().entityId(appUser.getId()).response("Account verification successful")
						.message("Login to your account to start tweeting!").build());
	}

	@Override
	public ResponseEntity<AppUser> isUserAuthenticated() {
		log.info("Calling to check if user is authenticated!!");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("AUTHENTICATION: {}", authentication);
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			AppUser loggedInUser = this.appUserRepository.findByEmail(authentication.getName())
					.orElseThrow(() -> new AbstractPlatformException("error.user.not.authenticated",
							"No user with this email", 404));
			return ResponseEntity.ok(loggedInUser);
		}
		return ResponseEntity.status(401).body(null);
	}

	@Override
	public ResponseEntity<String> validateToken(String jwtToken) {
		String jwtUsername = jwtTokenService.extractUsername(jwtToken);
		log.info("AUTHENTICATION: {}", jwtUsername);
		if(jwtUsername == null) {
			return ResponseEntity.status(401).body(null);
		}else {
			return ResponseEntity.ok(jwtUsername);
		}
	}

	private Token getToken(String token) {
		Token verificationToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new AbstractPlatformException("error.msg.auth.token.not.found", "Token Not Found"));

		long expirationTime = verificationToken.getExpirationTime();
		long now = Instant.now().getEpochSecond();

		if (now > expirationTime) {
			throw new ConstraintValidationException("error.msg.auth.token.expired",
					"Token has expired. Try resending verification token to email");
		}
		return verificationToken;
	}

}

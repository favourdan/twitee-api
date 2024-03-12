package org.tm30.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tm30.util.data.ApiError;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
		ApiError apiError = new ApiError();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			apiError.getSubErrors().put(fieldName, errorMessage);
		});

		apiError.setMessage("Errors Found");
		apiError.setMessage("Bad Request");
		return ResponseEntity.status(400).body(apiError);
	}

	@ExceptionHandler(AbstractPlatformException.class)
	public ResponseEntity<ApiError> abstractPlatformException(AbstractPlatformException ex) {

		ApiError errorResponse = ApiError.builder().message(ex.getDefaultUserMessage())
				.globalMessageCode(ex.getGlobalisationMessageCode()).debugMessage(ex.getLocalizedMessage())
				.build();

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(ConstraintValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> constraintValidationException(ConstraintValidationException ex) {

		ApiError errorResponse = ApiError.builder().message(ex.getDefaultUserMessage())
				.globalMessageCode(ex.getGlobalisationMessageCode()).debugMessage(ex.getLocalizedMessage())
				.build();

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(MailSendException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<ApiError> mailSendException(MailSendException ex) {

		ApiError errorResponse = ApiError.builder().message(ex.getMessage())
				.globalMessageCode(HttpStatus.SERVICE_UNAVAILABLE.name()).debugMessage(ex.getLocalizedMessage())
				.build();

		return ResponseEntity.badRequest().body(errorResponse);
	}
}

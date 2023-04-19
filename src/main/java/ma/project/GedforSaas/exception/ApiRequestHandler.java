package ma.project.GedforSaas.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ma.project.GedforSaas.request.ApiRequest;

@ControllerAdvice
public class ApiRequestHandler {

	@ExceptionHandler(value = { ApiRequestException.class })
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {

		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), badRequest, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, badRequest);
	}

	@ExceptionHandler(value = { ResourceNotFoundExceptionConstimized.class })
	public ResponseEntity<Object> handleResourceNotFoundExceptionConstimized(ResourceNotFoundExceptionConstimized e) {

		HttpStatus notFound = HttpStatus.NOT_FOUND;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), notFound, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, notFound);
	}

	@ExceptionHandler(value = { ResourceAlreadyExistCustomized.class })
	public ResponseEntity<Object> handleResourceAlreadyExistCustomized(ResourceAlreadyExistCustomized e) {

		HttpStatus notFound = HttpStatus.CONFLICT;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), notFound, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, notFound);
	}

	@ExceptionHandler(value = { IllegalStateExceptionCustomized.class })
	public ResponseEntity<Object> handleIllegalStateExceptioncCustomized(IllegalStateExceptionCustomized e) {

		HttpStatus conflict = HttpStatus.CONFLICT;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), conflict, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, conflict);
	}

	@ExceptionHandler(value = { AuthenticationFailedException.class })
	public ResponseEntity<Object> handleAuthenticationFailedException(AuthenticationFailedException e) {

		HttpStatus conflict = HttpStatus.FORBIDDEN;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), conflict, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, conflict);
	}

	@ExceptionHandler(value = { RequiredParameterException.class })
	public ResponseEntity<Object> handleRequiredParameterException(RequiredParameterException e) {

		HttpStatus conflict = HttpStatus.UNPROCESSABLE_ENTITY;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), conflict, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, conflict);
	}

	@ExceptionHandler(value = { EmailNotConfirmed.class })
	public ResponseEntity<Object> handleEmailNotConfirmed(EmailNotConfirmed e) {

		HttpStatus conflict = HttpStatus.UNAUTHORIZED;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), conflict, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, conflict);
	}

	@ExceptionHandler(value = { NotAuthorizedCustomized.class })
	public ResponseEntity<Object> handleNotAuthorizedCustomized(NotAuthorizedCustomized e) {

		HttpStatus unAuthorized = HttpStatus.UNAUTHORIZED;

		ApiRequest apiRequest = new ApiRequest(e.getMessage(), unAuthorized, ZonedDateTime.now());

		return new ResponseEntity<>(apiRequest, unAuthorized);
	}

}

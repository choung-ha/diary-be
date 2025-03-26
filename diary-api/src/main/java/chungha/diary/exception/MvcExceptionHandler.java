package chungha.diary.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;

import ch.qos.logback.classic.Logger;
import chungha.diarycommon.exception.ErrorResponse;
import chungha.diarycommon.exception.ServiceException;

@RestControllerAdvice
public class MvcExceptionHandler {
	private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass().getSimpleName());
	final ZoneId zoneId = ZoneId.of("Asia/Seoul");

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
		if (ex.getDebugMessage() != null) {
			logger.error(ex.getMessage());
		}

		var errorResponse = new ErrorResponse(
			ex.getErrorMessage(),
			ZonedDateTime.now(zoneId)
		);
		var httpStatus = MvcErrorCode.valueOf(ex.getErrorCode()).getHttpStatus();

		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException exception) {
		StringBuilder errorMessageBuilder = new StringBuilder();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			String errorMessage = error.getDefaultMessage();
			errorMessageBuilder.append(fieldName).append(" : ").append(errorMessage).append("\n");
		});
		String errorMessages = "Request is not Valid. Please Check Again";

		if (!errorMessageBuilder.isEmpty()) {
			errorMessages = errorMessageBuilder.toString();
			logger.error(errorMessages);
		}

		var errorResponse = new ErrorResponse(
			errorMessages,
			ZonedDateTime.now(zoneId)
		);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleDeserializationException(HttpMessageNotReadableException exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof ValueInstantiationException) {
			Throwable nested = cause.getCause();
			if (nested instanceof ServiceException) {
				ErrorResponse errorResponse = new ErrorResponse(nested.getMessage(), ZonedDateTime.now(zoneId));
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			}
		}
		// 그 외 예외는 기본 500 처리
		logger.error(exception.getMessage());
		var errorResponse = new ErrorResponse("Sorry, something went wrong", ZonedDateTime.now(zoneId));
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler()
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		logger.error(exception.getMessage());
		var errorResponse = new ErrorResponse(
			"Sorry, something went wrong",
			ZonedDateTime.now(zoneId)
		);

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

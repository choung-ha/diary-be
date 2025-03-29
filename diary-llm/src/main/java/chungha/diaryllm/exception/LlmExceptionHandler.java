package chungha.diaryllm.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;

import chungha.diarycommon.exception.ErrorResponse;
import chungha.diarycommon.exception.ServiceException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class LlmExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(LlmExceptionHandler.class);
	private final ZoneId zoneId = ZoneId.of("Asia/Seoul");

	@ExceptionHandler(ServiceException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleValidException(ServiceException exception) {
		if (exception.getDebugMessage() != null) {
			logger.error(exception.getMessage());
		}

		var errorResponse = new ErrorResponse(exception.getErrorMessage(), ZonedDateTime.now(zoneId));
		var httpStatus = LlmErrorCode.valueOf(exception.getErrorCode()).getHttpStatus();

		return Mono.just(ResponseEntity.status(httpStatus).body(errorResponse));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleDeserializationException(HttpMessageNotReadableException exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof ValueInstantiationException valueException) {
			Throwable nested = valueException.getCause();
			if (nested instanceof ServiceException serviceException) {
				var errorResponse = new ErrorResponse(serviceException.getErrorMessage(), ZonedDateTime.now(zoneId));
				return Mono.just(ResponseEntity.badRequest().body(errorResponse));
			}
		}

		logger.error(exception.getMessage());
		var errorResponse = new ErrorResponse("Sorry, something went wrong", ZonedDateTime.now(zoneId));
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
	}

	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<ErrorResponse>> handleException(Exception exception) {
		logger.error(exception.getMessage());
		var errorResponse = new ErrorResponse(
			"Sorry, something went wrong",
			ZonedDateTime.now(zoneId)
		);
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
	}
}

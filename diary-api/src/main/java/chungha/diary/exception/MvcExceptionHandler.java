package chungha.diary.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ch.qos.logback.classic.Logger;
import chungha.diarycommon.exception.ErrorResponse;
import chungha.diarycommon.exception.ServiceException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class MvcExceptionHandler {
	private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass().getSimpleName());
	final ZoneId zoneId = ZoneId.of("Asia/Seoul");

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception) {
		if (exception.getDebugMessage() != null) {
			logger.error(exception.getMessage());
		}

		var errorResponse = new ErrorResponse(
			exception.getErrorMessage(),
			ZonedDateTime.now(zoneId)
		);
		var httpStatus = MvcErrorCode.valueOf(exception.getErrorCode()).getHttpStatus();

		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

	@ExceptionHandler({
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class,
		ConstraintViolationException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception exception) {
		logger.error("Bad request [{}]: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		String errorMessage = "요청이 잘못되었습니다.";

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(errorMessage, ZonedDateTime.now(zoneId)));
	}

	@ExceptionHandler()
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		logger.error(exception.getMessage());
		var errorResponse = new ErrorResponse(
			"관리자의 도움이 필요합니다.",
			ZonedDateTime.now(zoneId)
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

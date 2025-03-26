package chungha.diarycommon.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(
	String message,
	ZonedDateTime timestamp) {
}

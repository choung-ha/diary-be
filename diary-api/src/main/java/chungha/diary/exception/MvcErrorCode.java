package chungha.diary.exception;

import org.springframework.http.HttpStatus;

import chungha.diarycommon.exception.BaseErrorCode;
import chungha.diarycommon.exception.ServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MvcErrorCode implements BaseErrorCode {
	// 다이어리 검색 예외
	WRONG_DATE_RANGE(HttpStatus.BAD_REQUEST, "검색 범위가 유효하지 않습니다."),

	;

	private final HttpStatus httpStatus;
	private final String message;

	public ServiceException serviceException() {
		return new ServiceException(this.name(), message);
	}

	public ServiceException serviceException(String debugMessage, Object... debugMessageArgs) {
		return new ServiceException(this.name(), message, String.format(debugMessage, debugMessageArgs));
	}

	public ServiceException serviceException(Throwable cause, String debugMessage, Object... debugMessageArgs) {
		return new ServiceException(cause, this.name(), message, String.format(debugMessage, debugMessageArgs));
	}
}

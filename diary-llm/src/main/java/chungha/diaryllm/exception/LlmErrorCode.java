package chungha.diaryllm.exception;

import org.springframework.http.HttpStatus;

import chungha.diarycommon.exception.BaseErrorCode;
import chungha.diarycommon.exception.ServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LlmErrorCode implements BaseErrorCode {
	SAMPLE(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생");
	private final HttpStatus httpStatus;
	private final String message;

	public ServiceException serviceException() {
		return new ServiceException(this.name(), this.message);
	}

	public ServiceException serviceException(String debugMessage, Object... debugMessageArgs) {
		return new ServiceException(this.name(), message, String.format(debugMessage, debugMessageArgs));
	}

	public ServiceException serviceException(Throwable cause, String debugMessage, Object... debugMessageArgs) {
		return new ServiceException(cause, this.name(), message, String.format(debugMessage, debugMessageArgs));
	}
}

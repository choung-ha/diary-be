package chungha.diaryllm.exception;

import org.springframework.http.HttpStatus;

import chungha.diarycommon.exception.BaseErrorCode;
import chungha.diarycommon.exception.ServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LlmErrorCode implements BaseErrorCode {
	// llm module
	DIARY_FEEDBACK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 피드백이 반영된 일기입니다."),
	LLM_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "LLM 호출에 실패했습니다."),
	DIARY_CONDITION_NOT_MATCH(HttpStatus.BAD_REQUEST, "조건에 맞는 일기가 없습니다."),
	FEEDBACK_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "피드백 저장에 실패했습니다.");

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

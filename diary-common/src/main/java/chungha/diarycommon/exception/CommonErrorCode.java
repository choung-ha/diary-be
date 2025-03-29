package chungha.diarycommon.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {
	// 다이어리
	DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 일기입니다."),

	// 유효성
	WRONG_EMOTION(HttpStatus.BAD_REQUEST, "유효한 감정을 선택해주세요."),

	// llm module
	DIARY_FEEDBACK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 피드백이 반영된 일기입니다."),
	LLM_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "LLM 호출에 실패했습니다."),
	DIARY_CONDITION_NOT_MATCH(HttpStatus.BAD_REQUEST, "조건에 맞는 일기가 없습니다."),
	FEEDBACK_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "피드백 저장에 실패했습니다.");
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

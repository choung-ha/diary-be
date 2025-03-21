package chungha.diarycommon.exception;

/**
 * api, llm에서 error enum을 각자 구현하되 통일시키고자 만듬
 */
public interface BaseErrorCode {
	ServiceException serviceException();

	ServiceException serviceException(String debugMessage, Object... debugMessageArgs);

	ServiceException serviceException(Throwable cause, String debugMessage, Object... debugMessageArgs);
}

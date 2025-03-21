package chungha.diary.model.request;

import chungha.diary.util.validation.annotation.ValidMongoId;

public record DiaryDeleteReq(
	@ValidMongoId String diaryId
) {
}

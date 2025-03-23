package chungha.diary.model.request;

import chungha.diary.util.validation.annotation.ValidMongoId;
import chungha.diarycommon.model.Emotion;

public record DiaryUpdateReq(
	@ValidMongoId String diaryId,
	String title,
	String content,
	Emotion emotion
) {
}

package chungha.diary.model.request;

import chungha.diary.util.validation.annotation.ValidContent;
import chungha.diary.util.validation.annotation.ValidMongoId;
import chungha.diary.util.validation.annotation.ValidTitle;
import chungha.diarycommon.model.Emotion;
import jakarta.validation.constraints.NotNull;

public record DiaryUpdateReq(
	@ValidMongoId String diaryId,
	@ValidTitle String title,
	@ValidContent String content,
	@NotNull Emotion emotion
) {
}

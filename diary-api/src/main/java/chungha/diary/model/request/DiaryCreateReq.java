package chungha.diary.model.request;

import chungha.diary.util.validation.annotation.ValidContent;
import chungha.diary.util.validation.annotation.ValidMongoId;
import chungha.diary.util.validation.annotation.ValidTitle;
import chungha.diarycommon.model.Emotion;
import jakarta.validation.constraints.NotNull;

public record DiaryCreateReq(
	@ValidMongoId String userId, // 회원 기능이 아직 없어서 임시로 추가
	@ValidTitle String title,
	@ValidContent String content,
	@NotNull Emotion emotion
) {
}

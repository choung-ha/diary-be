package chungha.diary.model.request;

import chungha.diarycommon.model.Emotion;

public record DiaryUpdateReq(
	String diaryId,
	String title,
	String content,
	Emotion emotion
) {
}

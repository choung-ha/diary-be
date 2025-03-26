package chungha.diary.model.response;

import java.time.LocalDateTime;

import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.model.Emotion;

public record DiaryRes(
	String id,
	String title,
	String content,
	Emotion emotion,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static DiaryRes from(Diary diary) {
		return new DiaryRes(
			diary.getId(),
			diary.getTitle(),
			diary.getContent(),
			diary.getEmotion(),
			diary.getCreatedAt(),
			diary.getUpdatedAt()
		);
	}
}

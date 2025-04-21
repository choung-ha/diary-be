package chungha.diary.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.model.Emotion;

public interface DiaryCustomRepository {
	Page<Diary> findAllByUserId(String userId, Pageable pageable);

	Diary updateDiary(DiaryUpdateReq req);

	Page<Diary> findByKeyword(String userId, Pageable pageable, String keyword);

	Page<Diary> findByCreatedAtBetween(String userId, Pageable pageable, LocalDateTime start, LocalDateTime end);

	Page<Diary> findByEmotion(String userId, Pageable pageable, Emotion emotion);
}

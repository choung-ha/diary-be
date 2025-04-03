package chungha.diary.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diarycommon.entity.Diary;

public interface DiaryCustomRepository {
	Page<Diary> findAllByUserId(String userId, Pageable pageable);

	Diary updateDiary(DiaryUpdateReq req);

	List<Diary> searchInContent(String userId, String keyword);
}

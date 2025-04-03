package chungha.diary.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diarycommon.entity.Diary;

public interface DiaryCustomRepository {
	List<Diary> searchInContent(String keyword);

	Page<Diary> findAllByUserId(String userId, Pageable pageable);

	Diary updateDiary(DiaryUpdateReq req);
}

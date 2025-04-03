package chungha.diary.service;

import java.util.List;

import chungha.diarycommon.entity.Diary;

public interface DiarySearchService {
	List<Diary> search(String userId, String keyword);
}

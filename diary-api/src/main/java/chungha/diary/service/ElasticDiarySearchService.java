package chungha.diary.service;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import chungha.diarycommon.entity.Diary;

@Service
@ConditionalOnProperty(name = "diary.search.engine", havingValue = "elastic")
public class ElasticDiarySearchService implements DiarySearchService {
	@Override
	public List<Diary> search(String userId, String keyword) {
		return List.of();
	}
}

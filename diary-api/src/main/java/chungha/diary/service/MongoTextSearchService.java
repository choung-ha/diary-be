package chungha.diary.service;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "diary.search.engine", havingValue = "mongo", matchIfMissing = true)
public class MongoTextSearchService implements DiarySearchService {

	private final DiaryRepository diaryRepository;

	@Override
	public List<Diary> search(String keyword) {
		return diaryRepository.searchInContent(keyword);
	}
}

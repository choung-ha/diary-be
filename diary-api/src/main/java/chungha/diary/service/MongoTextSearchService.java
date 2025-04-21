package chungha.diary.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import chungha.diary.model.response.DiaryRes;
import chungha.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "diary.search.engine", havingValue = "mongo", matchIfMissing = true)
public class MongoTextSearchService implements DiarySearchService {

	private final DiaryRepository diaryRepository;

	@Override
	public Page<DiaryRes> search(String userId, Pageable pageable, String keyword) {
		return diaryRepository.findByKeyword(userId, pageable, keyword).map(DiaryRes::from);
	}
}

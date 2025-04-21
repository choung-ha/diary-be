package chungha.diary.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import chungha.diary.model.response.DiaryRes;

@Service
@ConditionalOnProperty(name = "diary.search.engine", havingValue = "elastic")
public class ElasticDiarySearchService implements DiarySearchService {
	@Override
	public Page<DiaryRes> search(String userId, Pageable pageable, String keyword) {
		return null;
	}
}

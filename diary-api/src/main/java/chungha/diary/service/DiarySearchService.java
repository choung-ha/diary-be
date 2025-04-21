package chungha.diary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import chungha.diary.model.response.DiaryRes;

public interface DiarySearchService {
	Page<DiaryRes> search(String userId, Pageable pageable, String keyword);
}

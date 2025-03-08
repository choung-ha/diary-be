package chungha.diary.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private final DiaryRepository diaryRepository;

	public void createDiary(DiaryCreateReq req) {
		Diary diary = Diary.builder()
			.title(req.title())
			.content(req.content())
			.emotion(req.emotion())
			.userId(req.userId())
			.feedback(new ArrayList<>())
			.build();
		diaryRepository.saveDiary(diary);
	}

	public Page<Diary> getAllDiary(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "created_at"));

		return diaryRepository.getAllDiary(pageable);
	}

	public Diary getDiaryById(String diaryId) {
		return diaryRepository.getDiaryById(diaryId).orElseThrow();
	}

	public Diary updateDiary(DiaryUpdateReq req) {
		return diaryRepository.updateDiary(req);
	}

	public void deleteDiary(DiaryDeleteReq req) {
		diaryRepository.deleteDiaryById(req.diaryId());
	}

	// 유효성 검사는 서비스 로직에서
}

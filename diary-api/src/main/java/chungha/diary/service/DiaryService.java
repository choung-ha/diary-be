package chungha.diary.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.exception.CommonErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private final DiaryRepository diaryRepository;

	/**
	 * 일기를 생성한다.
	 * @param req 제목, 내용, 감정, 사용자
	 */
	public void createDiary(DiaryCreateReq req) {
		// 사용자 존재하는지 확인

		Diary diary = Diary.builder()
			.title(req.title())
			.content(req.content())
			.emotion(req.emotion())
			.userId(req.userId())
			.feedback(new ArrayList<>())
			.build();
		diaryRepository.saveDiary(diary);
	}

	/**
	 * 사용자가 작성한 일기를 최근 생성 기준으로 정렬해서 페이지네이션으로 반환한다.
	 * @param userId 사용자
	 * @param page 페이지 번호
	 * @param pageSize 페이지 크기
	 * @return 일기 목록
	 */
	public PagedModel<Diary> getAllDiary(String userId, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "created_at"));
		return diaryRepository.getAllDiary(userId, pageable);
	}

	public Diary getDiaryById(String diaryId) {
		return diaryRepository.getDiaryById(diaryId).orElseThrow(CommonErrorCode.DIARY_NOT_FOUND::serviceException);
	}

	public Diary updateDiary(DiaryUpdateReq req) {
		getDiaryById(req.diaryId());
		return diaryRepository.updateDiary(req);
	}

	public void deleteDiary(DiaryDeleteReq req) {
		getDiaryById(req.diaryId());
		diaryRepository.deleteDiaryById(req.diaryId());
	}
}

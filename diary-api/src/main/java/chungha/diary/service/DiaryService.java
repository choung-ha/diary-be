package chungha.diary.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.model.response.DiaryRes;
import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.exception.CommonErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private final DiaryRepository diaryRepository;
	private final DiarySearchService diarySearchService;

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
			.pending(false)
			.build();
		diaryRepository.save(diary);
	}

	/**
	 * 사용자가 작성한 일기를 최근 생성 기준으로 정렬해서 페이지네이션으로 반환한다.
	 * @param userId 사용자
	 * @param pageable 페이지 요청 정보
	 * @return 일기 목록
	 */
	public Page<DiaryRes> getAllDiary(String userId, Pageable pageable) {
		return diaryRepository.findAllByUserId(userId, pageable).map(DiaryRes::from);
	}

	public Diary getDiaryById(String diaryId) {
		return diaryRepository.findById(diaryId).orElseThrow(CommonErrorCode.DIARY_NOT_FOUND::serviceException);
	}

	public Diary updateDiary(DiaryUpdateReq req) {
		getDiaryById(req.diaryId());
		return diaryRepository.updateDiary(req);
	}

	public void deleteDiary(DiaryDeleteReq req) {
		getDiaryById(req.diaryId());
		diaryRepository.deleteById(req.diaryId());
	}

	public List<DiaryRes> searchByKeyword(String userId, String keyword) {
		return diarySearchService.search(userId, keyword).stream().map(DiaryRes::from).toList();
	}

}

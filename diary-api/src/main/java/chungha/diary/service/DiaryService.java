package chungha.diary.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import chungha.diary.exception.MvcErrorCode;
import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.model.response.DiaryRes;
import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.exception.CommonErrorCode;
import chungha.diarycommon.model.Emotion;
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

	/**
	 * 사용자가 작성한 일기를 최근 생성 기준으로 정렬해서 페이지네이션으로 반환한다.
	 * @param userId 사용자
	 * @param pageable 페이지 요청 정보
	 * @return 일기 목록
	 */
	public Page<DiaryRes> searchDiaries(String userId, Pageable pageable) {
		return diaryRepository.findAllByUserId(userId, pageable).map(DiaryRes::from);
	}

	public Page<DiaryRes> findByKeyword(String userId, Pageable pageable, String keyword) {
		return diarySearchService.search(userId, pageable, keyword);
	}

	public Page<DiaryRes> findByCreatedAtBetween(String userId, Pageable pageable, LocalDate start, LocalDate end) {
		// end가 비어있을 경우 start 값으로 대체
		LocalDate actualEnd = (end != null) ? end : start;

		// 유효성 검사
		if (start.isAfter(actualEnd)) {
			throw MvcErrorCode.WRONG_DATE_RANGE.serviceException("시작 범위가 종료 범위보다 미래일 수 없습니다.");
		}
		if (ChronoUnit.DAYS.between(start, actualEnd) > 365) {
			throw MvcErrorCode.WRONG_DATE_RANGE.serviceException("start와 end의 간격은 1년 이내여야 합니다.");
		}
		if (actualEnd.isAfter(LocalDate.now())) {
			throw MvcErrorCode.WRONG_DATE_RANGE.serviceException("end(%s)은 현재 날짜 이후입니다.", actualEnd);
		}

		// 날짜 변환
		// 시작 날짜 00:00 ~ 끝 날짜 23:59:59.999999999
		LocalDateTime startDateTime = start.atStartOfDay();
		LocalDateTime endDateTime = actualEnd.atTime(LocalTime.MAX);
		return diaryRepository.findByCreatedAtBetween(userId, pageable, startDateTime, endDateTime).map(DiaryRes::from);
	}

	public Page<DiaryRes> findByEmotion(String userId, Pageable pageable, Emotion emotion) {
		return diaryRepository.findByEmotion(userId, pageable, emotion).map(DiaryRes::from);
	}

}

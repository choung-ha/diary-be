package chungha.diary.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diarycommon.entity.Diary;
import chungha.diarycommon.model.Emotion;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {
	private final MongoTemplate mongoTemplate;
	private static final String USER_ID = "userId";

	private Page<Diary> getPage(Query query, Pageable pageable) {
		// pageable을 적용하여 조회
		List<Diary> list = mongoTemplate.find(query.with(pageable), Diary.class);
		// count 쿼리는 pageable 조건 없이 원본 조건으로 생성
		Query countQuery = Query.of(query);
		long count = mongoTemplate.count(countQuery, Diary.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> count);
	}

	public Page<Diary> findAllByUserId(String userId, Pageable pageable) {
		Criteria userCriteria = Criteria.where(USER_ID).is(userId);
		Query query = new Query(userCriteria);
		return getPage(query, pageable);
	}

	public Diary updateDiary(DiaryUpdateReq req) {
		Query query = new Query(Criteria.where("_id").is(req.diaryId()));
		Update update = new Update().set("updatedAt", LocalDateTime.now());

		if (req.title() != null && !req.title().isEmpty()) {
			update.set("title", req.title());
		}
		if (req.content() != null && !req.content().isEmpty()) {
			update.set("content", req.content());
		}
		if (req.emotion() != null) {
			update.set("emotion", req.emotion());
		}

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true); // 업데이트 후 객체 반환
		return mongoTemplate.findAndModify(query, update, options, Diary.class);
	}

	public Page<Diary> findByKeyword(String userId, Pageable pageable, String keyword) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(keyword);
		Criteria userCriteria = Criteria.where(USER_ID).is(userId);
		Query query = new TextQuery(textCriteria).addCriteria(userCriteria);
		return getPage(query, pageable);
	}

	public Page<Diary> findByCreatedAtBetween(String userId, Pageable pageable,
		LocalDateTime start, LocalDateTime end) {
		Criteria criteria = Criteria.where(USER_ID).is(userId).and("createdAt").gte(start).lte(end);
		return getPage(new Query(criteria), pageable);
	}

	public Page<Diary> findByEmotion(String userId, Pageable pageable, Emotion emotion) {
		Criteria criteria = Criteria.where(USER_ID).is(userId).and("emotion").is(emotion);
		return getPage(new Query(criteria), pageable);
	}
}

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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {
	private final MongoTemplate mongoTemplate;

	public Page<Diary> findAllByUserId(String userId, Pageable pageable) {
		Criteria userCriteria = Criteria.where("userId").is(userId);
		Query query = new Query(userCriteria).with(pageable);
		// pageable과 같이쓰면 제대로 안나온다. totalCount가 page * pageSize 값으로 나옴
		Query countQuery = new Query(userCriteria);

		List<Diary> diaryList = mongoTemplate.find(query, Diary.class);
		return PageableExecutionUtils.getPage(diaryList, pageable,
			() -> mongoTemplate.count(countQuery, Diary.class));
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

	public List<Diary> searchInContent(String userId, String keyword) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(keyword);
		Criteria userCriteria = Criteria.where("userId").is(userId);
		Query query = new TextQuery(textCriteria).addCriteria(userCriteria);
		return mongoTemplate.find(query, Diary.class);
	}

}

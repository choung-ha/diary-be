package chungha.diaryllm.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import chungha.diarycommon.entity.Change;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LlmApiRepository {
	private final ReactiveMongoTemplate reactiveMongoTemplate;

	Mono<Diary> reserveDiaryUpdate(String diaryId, String userId) {
		Query query = new Query(
			Criteria.where("_id")
				.is(diaryId)
				.and("userId").is(userId)
				.and("feedback").isNull()
				.and("changes").isNull()
				.and("pending").isNull());
		Update update = new Update().set("pending", true);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
		return reactiveMongoTemplate.findAndModify(query, update, options, Diary.class);
	}

	Mono<Diary> updateFeedbackAndChanges(String diaryId, String feedback, List<Change> changes) {
		Query query = new Query(Criteria.where("_id").is(diaryId)
			.and("pending").is(true));

		Update updateQuery = new Update()
			.set("feedback", feedback)
			.set("changes", changes)
			.set("updated_at", LocalDateTime.now(ZoneOffset.UTC))
			.set("pending", Boolean.FALSE);

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

		return reactiveMongoTemplate.findAndModify(query, updateQuery, options, Diary.class);
	}
}

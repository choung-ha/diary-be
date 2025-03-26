package chungha.diaryllm.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

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
				.and("userId")
				.is(userId)
				.and("pending").ne(true));

		Update update = new Update().set("pending", true);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
		return reactiveMongoTemplate.findAndModify(query, update, options, Diary.class);
	}

	Mono<Diary> updateFeedbackAndChanges(
		String diaryId,
		String improvedContent,
		Map<String, String> feedbackMap) {
		Query query = new Query(Criteria.where("_id").is(diaryId)
			.and("pending").is(true));

		Update updateQuery = new Update()
			.set("improved_content", improvedContent)
			.set("feedback", feedbackMap)
			.set("updated_at", LocalDateTime.now(ZoneOffset.UTC));

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

		return reactiveMongoTemplate.findAndModify(query, updateQuery, options, Diary.class);
	}

	Mono<Diary> setPending(String diaryId, boolean pending) {
		Query query = new Query(Criteria.where("_id").is(diaryId));
		Update update = new Update().set("pending", pending);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

		return reactiveMongoTemplate.findAndModify(query, update, options, Diary.class);
	}
}

package com.chungha.diaryllm.service;

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

	Mono<Diary> findBy(String diaryId, String userId) {
		Query query = new Query(Criteria.where("_id").is(diaryId).and("userId").is(userId));
		return reactiveMongoTemplate.findOne(query, Diary.class);
	}

	public Mono<Diary> updateFeedbackAndChanges(String diaryId, String feedback, List<Change> changes) {
		Query query = new Query(Criteria.where("_id").is(diaryId));
		Update updateQuery = new Update().set("feedback", feedback)
			.set("changes", changes)
			.set("updated_at", LocalDateTime.now(ZoneOffset.UTC));

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

		return reactiveMongoTemplate.findAndModify(query, updateQuery, options, Diary.class);
	}
}

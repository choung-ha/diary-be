package com.chungha.diaryllm.service;

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

	Mono<Diary> saveFeedback(String diaryId, String feedback) {
		Query query = new Query(Criteria.where("_id").is(diaryId));
		Update updateQuery = new Update().set("feedback", feedback);
		return reactiveMongoTemplate.findAndModify(query, updateQuery, Diary.class);
	}
}

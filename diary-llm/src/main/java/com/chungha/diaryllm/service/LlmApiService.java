package com.chungha.diaryllm.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import com.chungha.diaryllm.model.request.FeedbackReq;
import com.chungha.diaryllm.model.response.FeedbackRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class LlmApiService {
	private final OpenAiChatModel openAiChatModel;
	private final LlmApiRepository llmApiRepository;

	private static final String PREFIX_MESSAGE = "당신은 본문 속 영어 문장들을 보다 더 나은 영어 문장으로 바꾸는 일을 담당합니다. 대답은 무슨 일이 있어도 다음 형식으로 하세요. {\"message\": \"당신이 바꾼 글\"} \n입력 문장 : ";

	public Mono<Diary> createFeedback(FeedbackReq req) {
		String prompt = PREFIX_MESSAGE + req.content();

		return Mono.fromCallable(() -> openAiChatModel.call(prompt))
			.subscribeOn(Schedulers.boundedElastic())
			.flatMap(feedbackJson ->
				Mono.fromCallable(() -> {
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(feedbackJson, FeedbackRes.class);
				}).subscribeOn(Schedulers.boundedElastic())
			).map(FeedbackRes::message)
			.flatMap(feedback -> llmApiRepository.saveFeedback(req.diaryId(), req.content()));
	}
}
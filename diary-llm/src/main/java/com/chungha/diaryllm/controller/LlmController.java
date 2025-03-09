package com.chungha.diaryllm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chungha.diaryllm.model.request.FeedbackReq;
import com.chungha.diaryllm.service.LlmApiService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class LlmController {
	private final LlmApiService llmApiService;

	@PostMapping("/llm-feedback")
	public Mono<ResponseEntity<?>> createFeedback(@RequestBody FeedbackReq req) {
		return llmApiService.createFeedBackAndSave(req)
			.map(ResponseEntity::ok);
	}
}

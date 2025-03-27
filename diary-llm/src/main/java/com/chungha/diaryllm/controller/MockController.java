package com.chungha.diaryllm.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mock-flux")
@RequiredArgsConstructor
public class MockController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Value("${spring.ai.openai.base-url}")
	private String mockUrl;

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	@GetMapping("")
	public Mono<ResponseEntity<?>> generateMockFeedback() {
		return webClient.get()
			.uri(mockUrl)
			.retrieve()
			.bodyToMono(String.class)
			.flatMap(body -> {
				try {
					Map<String, Object> feedbackMap = objectMapper.readValue(body, new TypeReference<>() {
					});
					return Mono.just(Map.of("feedbacks", feedbackMap));
				} catch (Exception e) {
					logger.error("JSON 파싱 실패", e);
					return Mono.just(Map.of("error", "Failed to parse response"));
				}
			})
			.onErrorResume(e -> {
				logger.warn("외부 API 요청 실패 또는 timeout 발생: {}", e.getMessage());
				return Mono.just(Map.of("error", "timeout or connection error"));
			})
			.map(ResponseEntity::ok); // 👈 타입 명시!
	}
}

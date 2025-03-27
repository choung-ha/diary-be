package chungha.diary.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Profile("stress")
@RestController
@RequestMapping("/mock-mvc")
@RequiredArgsConstructor
public class MockController {
	@Value("${spring.ai.openai.base-url}")
	private String mockUrl;

	@Qualifier("simpleRestTemplate")
	private final RestTemplate simpleRestTemplate;

	@Qualifier("pooledRestTemplate")
	private final RestTemplate pooledRestTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/simple")
	public ResponseEntity<?> generateMockFeedbackSimpleRT() {
		return getResponseEntity(simpleRestTemplate);
	}

	@GetMapping("/pool")
	public ResponseEntity<?> generateMockFeedbackPooledRT() {
		return getResponseEntity(pooledRestTemplate);
	}

	@GetMapping("/async-simple")
	@Async("asyncExecutor")
	public CompletableFuture<ResponseEntity<?>> generateAsyncMockFeedbackSimpleRT() {
		ResponseEntity<?> result = getResponseEntity(simpleRestTemplate);
		return CompletableFuture.completedFuture(result);
	}

	@GetMapping("/async-pool")
	@Async("asyncExecutor")
	public CompletableFuture<ResponseEntity<?>> generateAsyncMockFeedbackPooledRT() {
		ResponseEntity<?> result = getResponseEntity(pooledRestTemplate);
		return CompletableFuture.completedFuture(result);
	}

	private ResponseEntity<?> getResponseEntity(RestTemplate restTemplate) {
		Map<String, Object> feedbackMap = null;
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(mockUrl, String.class);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				feedbackMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(Map.of("feedbacks", feedbackMap));
	}
}

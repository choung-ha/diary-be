package diary.controller;

import diary.model.FeedBackRequest;
import diary.service.MockLLMApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Profile("stress")
@RestController
@RequiredArgsConstructor
public class MockLLMController {
    private final MockLLMApiService mockLLMApiService;

    @PostMapping("ai/mock-feedback")
    public ResponseEntity<?> generateMockFeedback(@RequestBody FeedBackRequest feedBackRequest) {
        Map<String, Object> response = this.mockLLMApiService.generateMockFeedback(feedBackRequest);
        return ResponseEntity.ok(response);
    }
}

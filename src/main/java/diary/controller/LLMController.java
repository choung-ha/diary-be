package diary.controller;

import diary.model.FeedBackRequest;
import diary.service.LLMApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LLMController {
    private final LLMApiService llmApiService;


    @PostMapping("ai/feedback")
    public ResponseEntity<?> generateFeedback(@RequestBody FeedBackRequest feedBackRequest) {
        Map<String, Object> response = this.llmApiService.generateFeedback(feedBackRequest);
        return ResponseEntity.ok(response);
    }

}

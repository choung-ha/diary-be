package com.chungha.diaryllm.controller;

import com.chungha.diaryllm.model.request.FeedbackReq;
import com.chungha.diaryllm.service.LLMApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class LLMController {
    private final LLMApiService llmApiService;

    @PostMapping("/llm-feedback")
    public Mono<ResponseEntity<?>> createFeedback(@RequestBody FeedbackReq req) {
        return llmApiService.createFeedback(req)
                .map(ResponseEntity::ok);
    }
}

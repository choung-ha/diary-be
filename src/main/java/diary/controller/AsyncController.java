package diary.controller;

import diary.model.FeedBackRequest;
import diary.service.AsyncService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("stress")
@RestController
@RequiredArgsConstructor
public class AsyncController {
    private final AsyncService asyncService;

    @PostMapping("async/mock-feedback")
    public CompletableFuture<ResponseEntity<?>> generateMockFeedback(@RequestBody FeedBackRequest feedBackRequest) {
        return asyncService.generateMockFeedback(feedBackRequest)
                .thenApply(response -> ResponseEntity.ok(response));
    }
}

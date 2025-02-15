package diary.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import diary.model.FeedBackRequest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("stress")
@Service
public class AsyncService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.ai.openai.base-url}")
    private String mockUrl;

    private static final String prefixMessage = """
            당신은 본문 속 영어 문장들을 보도 더 나은 영어 문장으로 바꾸는 일을 담당합니다. 당신이 해야할 일은 아래 본문의 글을 보고 단어들을 더 나은 방향이 되도록 안내해주는 일입니다.
            대답은 무슨 일이 있어도 다음 형식으로 해주세요.
            {
                "이전 단어" : "당신이 바꾼단어",
                "이전 단어" : "당신이 바꾼단어",
                "이전 단어" : "당신이 바꾼단어" // 이것들의 반복
            }
            
            만약 수정할 문장이 없디면 다음과 같이 보내 주세요.
            {
                "great" : "great"
            }
            -- 본문 --
            """;

    public AsyncService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Async("asyncExecutor")
    public CompletableFuture<Map<String, ?>> generateMockFeedback(FeedBackRequest feedBackRequest) {
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append(prefixMessage).append(feedBackRequest.content());

        return webClient.get()
                .uri(mockUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> feedbackMap = objectMapper.readValue(
                                response, new TypeReference<>() {});
                        return Map.of("feedbacks", feedbackMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Map.of("feedbacks", "Failed to parse response");
                    }
                })
                .toFuture();
    }
}

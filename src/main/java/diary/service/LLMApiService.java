package diary.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import diary.model.FeedBackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LLMApiService {
    @Value("${spring.ai.openai.base-url}")
    private String mockUrl;
    private final OpenAiChatModel openAiChatModel;

    private static final StringBuilder prefixMessage = new StringBuilder("""
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
            """);

    public Map<String, Object> generateFeedback(FeedBackRequest feedBackRequest) {
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append(prefixMessage).append(feedBackRequest.content());

        String feedback = openAiChatModel.call(requestMessage.toString());
        System.out.println(feedback);
        Map<String, Object> feedbackMap = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            feedbackMap = objectMapper.readValue(feedback, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Map.of("feedbacks", feedbackMap);
    }

    public Map<String, Object> generateMockFeedback(FeedBackRequest feedBackRequest) {
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append(prefixMessage).append(feedBackRequest.content());

        Map<String, Object> feedbackMap = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(mockUrl, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                feedbackMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Map.of("feedbacks", feedbackMap);
    }


}

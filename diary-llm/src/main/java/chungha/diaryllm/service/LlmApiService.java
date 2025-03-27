package chungha.diaryllm.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import chungha.diaryllm.model.request.FeedbackReq;
import chungha.diaryllm.model.response.FeedbackRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class LlmApiService {
	private final ObjectMapper objectMapper;
	private final OpenAiChatModel openAiChatModel;
	private final LlmApiRepository llmApiRepository;

	private static final String PREFIX_MESSAGE =
		"""
		당신은 본문 속 영어 문장들을 보다 더 나은 영어 문장으로 바꾸고,
		각 문장을 어떻게 바꾸었는지(그리고 그 이유)까지 설명해 줄 책임이 있습니다.
	
		반드시 무슨 일이 있어도 다음 JSON 형식으로만 답변을 작성하세요. 다른 문구는 일절 넣지 않습니다.
		improvedContent에는 value에는 당신이 개선해서 바꾼 글
		feedback의 key에는 당신이 교정한 문장을 value에는 무엇을 어떻게 바꿨는지, 그리고 그 이유를 적어줘
		
		{
		  "improvedContent": "value",
		  "feedback": {
			"key": "value",
			"key": "value",
			...
		  }
		}
	
		입력 문장 :
		""";


	public Mono<Diary> createFeedBackAndSave(FeedbackReq req) {
		return llmApiRepository.reserveDiaryUpdate(req.diaryId(), req.userId())
			.switchIfEmpty(Mono.error(new RuntimeException("일기를 찾을 수 없거나 권한이 없습니다.")))
			.flatMap(diary -> {
				if (diary.getImprovedContent() != null) {
					return Mono.error(new RuntimeException("이미 피드백이 반영된 일기입니다"));
				}
				return callAndParseFeedback(diary.getContent())
					.flatMap(parseFeedback -> saveFeedBackAfterReservation(diary, parseFeedback))
					.onErrorResume(ex ->
						llmApiRepository.setPending(diary.getId(), false)
							.then(Mono.error(ex)));
			});
	}

	private Mono<FeedbackRes> callAndParseFeedback(String content) {
		String prompt = PREFIX_MESSAGE + content;
		return Mono.fromCallable(() -> {
			String rawFeedback = openAiChatModel.call(prompt);
			return objectMapper.readValue(rawFeedback, FeedbackRes.class);
		})
			.subscribeOn(Schedulers.boundedElastic())
			.retryWhen(
				Retry.backoff(3, Duration.ofSeconds(1))
					.maxBackoff(Duration.ofSeconds(10))
					.filter(throwable -> (throwable instanceof RuntimeException) || (throwable instanceof IOException))
				.onRetryExhaustedThrow(((retrySpec, retrySignal) ->
					new RuntimeException("LLM 호출에 실패했습니다.", retrySignal.failure())))
			);
	}

	private Mono<Diary> saveFeedBackAfterReservation(Diary diary, FeedbackRes feedback) {
		String improvedContent = feedback.improvedContent();
		Map<String, String> sanitizeMap = sanitizeFeedback(feedback.feedback());
		return llmApiRepository.updateFeedbackAndChanges(diary.getId(), improvedContent, sanitizeMap)
			.switchIfEmpty(Mono.error(new RuntimeException("조건에 맞는 일기가 없습니다")));
	}


	private Map<String, String> sanitizeFeedback(Map<String, String> feedbackMap) {
		if (feedbackMap == null) return null;
		return feedbackMap.entrySet().stream()
			.collect(Collectors.toMap(entry -> makeMongoKeySafe(entry.getKey()), Map.Entry::getValue));
	}

	private String makeMongoKeySafe(String key) {
		return key.replaceAll("\\.", "").replaceAll("\\$", "");
	}
}
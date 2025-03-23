package chungha.diaryllm.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import chungha.diaryllm.model.request.FeedbackReq;
import chungha.diaryllm.model.response.FeedbackRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
		{
		  "content": "당신이 개선한 전체 글",
		  "feedback": {
			"교정 후 문장": "무엇을 어떻게 바꿨는지, 그리고 그 이유"
		  }
		}
	
		입력 문장 :
		""";


	public Mono<Diary> createFeedBackAndSave(FeedbackReq req) {
		return llmApiRepository.reserveDiaryUpdate(req.diaryId(), req.userId())
			.switchIfEmpty(Mono.error(new RuntimeException("일기를 찾을 수 없거나 권한이 없습니다.")))
			.flatMap(diary -> {
				if (!Objects.isNull(diary.getImprovedContent())) {
					return Mono.error(new RuntimeException("이미 피드백이 반영된 일기입니다"));
				}
				if (Boolean.TRUE.equals(diary.getPending())) {
					return Mono.error(new RuntimeException("피드백 업데이트 중인 일기입니다."));
				}
				return Mono.just(diary);
			})
			.flatMap(diary -> callLlmApi(diary.getContent())
				.flatMap(this::parseFeedback)
				.flatMap(parsed -> saveFeedBackAfterReservation(diary, parsed)));
	}

	private Mono<String> callLlmApi(String content) {
		String prompt = PREFIX_MESSAGE + content;
		return Mono.fromCallable(() -> openAiChatModel.call(prompt)).subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<FeedbackRes> parseFeedback(String feedbackJson) {
		return Mono.fromCallable(() -> objectMapper.readValue(feedbackJson, FeedbackRes.class))
			.subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<Diary> saveFeedBackAfterReservation(Diary diary, FeedbackRes feedback) {
		String improvedContent = feedback.improvedContent();
		Map<String, String> feedbackMap = feedback.feedback();
		return llmApiRepository.updateFeedbackAndChanges(diary.getId(), improvedContent, feedbackMap)
			.switchIfEmpty(Mono.error(new RuntimeException("조건에 맞는 일기가 없습니다")));
	}
}
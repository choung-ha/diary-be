package chungha.diaryllm.service;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.openai.OpenAiChatModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chungha.diarycommon.entity.Diary;
import chungha.diaryllm.model.request.FeedbackReq;
import chungha.diaryllm.model.response.FeedbackRes;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class LlmApiServiceTest {

	@Mock
	private LlmApiRepository llmApiRepository;

	@Mock
	private OpenAiChatModel openAiChatModel;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private LlmApiService llmApiService;

	@Test
	@DisplayName("일기를 찾을 수 없다면 피드백을 생성할 수 없습니다.")
	void createFeedback_diaryNotFound() {
		String diaryId = UUID.randomUUID().toString();
		String userid = "testuser";
		String content = "Today is poem poem";
		FeedbackReq req = new FeedbackReq(userid, diaryId, content);

		when(llmApiRepository.reserveDiaryUpdate(diaryId, userid)).thenReturn(Mono.empty());

		Mono<Diary> result = llmApiService.createFeedBackAndSave(req);

		StepVerifier.create(result)
			.expectErrorMessage("일기를 찾을 수 없거나 권한이 없습니다.")
			.verify();
	}

	@Test
	@DisplayName("이미 피드백을 받은 일기는 피드백 요청할 수 없습니다.")
	void createFeedback_feedbackAlreadyExist() {
		String diaryId = UUID.randomUUID().toString();
		String userid = "testuser";
		String content = "Today is poem poem";
		FeedbackReq req = new FeedbackReq(userid, diaryId, content);

		Diary existingDiary = Diary.builder()
			.id(diaryId)
			.userId(userid)
			.improvedContent("Today is greatest poem poem")
			.pending(false)
			.build();

		when(llmApiRepository.reserveDiaryUpdate(diaryId, userid)).thenReturn(Mono.just(existingDiary));

		Mono<Diary> result = llmApiService.createFeedBackAndSave(req);

		StepVerifier.create(result)
			.expectErrorMessage("이미 피드백이 반영된 일기입니다")
			.verify();
	}

	@Test
	@DisplayName("일기 당 한번만 피드백 받을 수 있다.")
	void createFeedback_diaryPending() {
		String diaryId = UUID.randomUUID().toString();
		String userid = "testuser";
		String content = "Today is poem poem";
		FeedbackReq req = new FeedbackReq(userid, diaryId, content);

		// 이미 요청 한 일기이기에 빈 객체만 나옴
		when(llmApiRepository.reserveDiaryUpdate(diaryId, userid)).thenReturn(Mono.empty());

		Mono<Diary> result = llmApiService.createFeedBackAndSave(req);

		StepVerifier.create(result)
			.expectErrorMessage("일기를 찾을 수 없거나 권한이 없습니다.")
			.verify();
	}

	@Test
	@DisplayName("성공적으로 피드백 생성에 성공한다")
	void createFeedbackAndSave_success() throws JsonProcessingException {
		String diaryId = UUID.randomUUID().toString();
		String userid = "testuser";
		String content = "Today is poem poem";
		String improvedContent = "Today is greatest poem poem";
		FeedbackReq req = new FeedbackReq(userid, diaryId, content);

		// reserveDiaryUpdate mock
		Diary diary = Diary.builder()
			.id(diaryId)
			.userId(userid)
			.content(content)
			.improvedContent(null)
			.pending(null)
			.build();
		when(llmApiRepository.reserveDiaryUpdate(diaryId, userid))
			.thenReturn(Mono.just(diary));

		// LLM 응답 mock
		String gptResponseJson = "{\"content\":\"Improved content\",\"feedback\":{\"Improved sentence\":\"Reason\"}}";
		//import static org.mockito.ArgumentMatchers.anyString;

		when(openAiChatModel.call(anyString())).thenReturn(gptResponseJson);


		// Jackson 파싱 mock
		FeedbackRes feedbackRes = new FeedbackRes(improvedContent, Map.of("Improved sentence", "Reason"));
		when(objectMapper.readValue(gptResponseJson, FeedbackRes.class))
			.thenReturn(feedbackRes);

		// updateFeedbackAndChanges mock
		Diary updateDiary = Diary.builder()
			.id(diaryId)
			.userId(userid)
			.content(content)
			.improvedContent(feedbackRes.improvedContent())
			.feedback(feedbackRes.feedback())
			.pending(false)
			.build();
		when(llmApiRepository.updateFeedbackAndChanges(diaryId, feedbackRes.improvedContent(), feedbackRes.feedback()))
			.thenReturn(Mono.just(updateDiary));

		// 테스트 실행
		Mono<Diary> result = llmApiService.createFeedBackAndSave(req);

		// 검증
		StepVerifier.create(result)
			.expectNext(updateDiary)
			.verifyComplete();
	}

	@Test
	@DisplayName("Json parse 실패해서 피드백 생성에 실패한다")
	void llmRetryAndPendingFalseOnFailure() throws Exception {
		String diaryId = "diary123";
		String userId = "user1";
		String content = "Today is poem poem";
		Diary diary = Diary.builder()
			.id(diaryId)
			.userId(userId)
			.content(content)
			.pending(true)
			.build();

		when(llmApiRepository.reserveDiaryUpdate(diaryId, userId)).thenReturn(Mono.just(diary));

		// openAiChatModel.call 3번 모두 실패
		when(openAiChatModel.call(anyString())).thenThrow(new RuntimeException("LLM error"));

		Diary pendingOffDiary = Diary.builder().id(diaryId).userId(userId).pending(false).build();
		when(llmApiRepository.setPending(diaryId, false)).thenReturn(Mono.just(pendingOffDiary));

		Mono<Diary> result = llmApiService.createFeedBackAndSave(new FeedbackReq(userId, diaryId, content));

		StepVerifier.create(result)
			.expectErrorMessage("LLM 호출에 실패했습니다.")
			.verify();

		verify(openAiChatModel, times(4)).call(anyString());
		verify(llmApiRepository, times(1)).setPending(diaryId, false);
	}

}

package com.chungha.diaryllm.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import com.chungha.diaryllm.model.request.FeedbackReq;
import com.chungha.diaryllm.model.response.FeedbackRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import chungha.diarycommon.entity.Change;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class LlmApiService {
	private final OpenAiChatModel openAiChatModel;
	private final LlmApiRepository llmApiRepository;

	private static final String PREFIX_MESSAGE = "당신은 본문 속 영어 문장들을 보다 더 나은 영어 문장으로 바꾸는 일을 담당합니다. 대답은 무슨 일이 있어도 다음 형식으로 하세요. {\"message\": \"당신이 바꾼 글\"} \n입력 문장 : ";

	public Mono<Diary> createFeedBackAndSave(FeedbackReq req) {
		return llmApiRepository.reserveDiaryUpdate(req.diaryId(), req.userId())
			.switchIfEmpty(Mono.error(new RuntimeException("피드백 업데이트 중인 일기입니다")))
			.flatMap(diary -> {
				if (!Objects.isNull(diary.getFeedback())) {
					return Mono.error(new RuntimeException("이미 피드백이 반영된 일기입니다"));
				}
				return Mono.just(diary);
			})
			.flatMap(diary -> callLlmApi(diary.getContent()).flatMap(this::parseFeedback)
				.flatMap(feedback -> saveFeedBackAfterReservation(diary, feedback)));
	}

	private Mono<String> callLlmApi(String content) {
		String prompt = PREFIX_MESSAGE + content;
		return Mono.fromCallable(() -> openAiChatModel.call(prompt)).subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<String> parseFeedback(String feedbackJson) {
		return Mono.fromCallable(() -> {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(feedbackJson, FeedbackRes.class).message();
		}).subscribeOn(Schedulers.boundedElastic());
	}

	private Mono<Diary> saveFeedBackAfterReservation(Diary diary, String feedback) {
		List<Change> changes = calculateDiff(diary.getContent(), feedback);
		return llmApiRepository.updateFeedbackAndChanges(diary.getId(), feedback, changes)
			.switchIfEmpty(Mono.error(new RuntimeException("조건에 맞는 일기가 없습니다")));
	}

	// 변경 지점을 계산하고 변경된 내용을 반환한다
	private List<Change> calculateDiff(String original, String feedback) {
		DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
		LinkedList<DiffMatchPatch.Diff> diffs = diffMatchPatch.diffMain(original, feedback);
		diffMatchPatch.diffCleanupSemantic(diffs);

		List<Change> changes = new ArrayList<>();
		int originIndex = 0;
		int pos = 0;
		while (pos < diffs.size()) {
			DiffMatchPatch.Diff diff = diffs.get(pos);
			if (diff.operation == DiffMatchPatch.Operation.EQUAL) {
				originIndex += diff.text.length();
				pos++;
			} else {
				int startIndex = originIndex;
				StringBuilder newContent = new StringBuilder();
				while (pos < diffs.size() && diffs.get(pos).operation != DiffMatchPatch.Operation.EQUAL) {
					DiffMatchPatch.Diff current = diffs.get(pos);
					if (current.operation == DiffMatchPatch.Operation.DELETE) {
						originIndex += current.text.length();
					} else if (current.operation == DiffMatchPatch.Operation.INSERT) {
						newContent.append(current.text);
					}
					pos++;
				}
				int endIndex = originIndex;
				changes.add(new Change(startIndex, endIndex, newContent.toString()));
			}
		}
		return changes;
	}
}
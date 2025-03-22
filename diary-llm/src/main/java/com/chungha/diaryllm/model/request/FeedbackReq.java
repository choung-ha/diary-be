package com.chungha.diaryllm.model.request;

public record FeedbackReq(
	String userId,
	String diaryId,
	String content
) {
}

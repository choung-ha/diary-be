package com.chungha.diaryllm.model.request;

public record FeedbackReq(
	String diaryId,
	String content
) {
}

package chungha.diaryllm.model.response;

import java.util.Map;

public record FeedbackRes(String improvedContent, Map<String, String> feedback) {
}


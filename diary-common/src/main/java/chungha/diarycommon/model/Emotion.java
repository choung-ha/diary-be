package chungha.diarycommon.model;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import chungha.diarycommon.exception.CommonErrorCode;

public enum Emotion {
	HAPPY, SAD, ANGRY, EXCITED, NEUTRAL;

	@JsonCreator
	public static Emotion fromString(String value) {
		return Stream.of(Emotion.values())
			.filter(e -> e.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(CommonErrorCode.WRONG_EMOTION::serviceException);
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
}


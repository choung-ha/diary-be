package chungha.diarycommon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Emotion {
    HAPPY, SAD, ANGRY, EXCITED, NEUTRAL;

    @JsonCreator
    public static Emotion fromString(String value) {
        return Stream.of(Emotion.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid emotion: " + value));
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}


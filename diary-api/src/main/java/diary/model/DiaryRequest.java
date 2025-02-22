package diary.model;

import java.util.Map;

public record DiaryRequest(
    String title,
    String content,
    Emotion emotion,
    Map<String, Object> feedback
) {}

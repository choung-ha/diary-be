package chungha.diary.model;

import chungha.diarycommon.model.Emotion;
import java.util.List;
import java.util.Map;

public record DiaryRequest(
    String userId, // 회원 기능이 아직 없어서 임시로 추가
    String title,
    String content,
    Emotion emotion
) {}

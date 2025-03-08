package chungha.diary.model.request;

import chungha.diarycommon.model.Emotion;

public record DiaryCreateReq(
    String userId, // 회원 기능이 아직 없어서 임시로 추가
    String title,
    String content,
    Emotion emotion
) {}

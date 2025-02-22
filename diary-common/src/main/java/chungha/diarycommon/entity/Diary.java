package chungha.diarycommon.entity;

import chungha.diarycommon.model.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "diary")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
    @Id
    private String id;

    // 제목
    private String title;

    // 일기 내용 (500자)
    private String content;

    // 감정 (Enum)
    private Emotion emotion;

    // 피드백 json
    private Map<String, Object> feedback;

}

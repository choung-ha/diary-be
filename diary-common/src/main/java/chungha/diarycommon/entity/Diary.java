package chungha.diarycommon.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import chungha.diarycommon.model.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "diary")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary extends BaseEntity {
	@Id
	private String id;

	// 제목 (50자)
	private String title;

	// 일기 내용 (500자)
	private String content;

	// 감정 (Enum)
	private Emotion emotion;

	// 피드백
	@Field("improved_content")
	private String improvedContent;

	// LLM이 제공한 문장별 수정 이유 (key: 문장/구문, value: 수정 이유)
	private Map<String, String> feedback;


	// 사용자 id
	private String userId;

	// 예약 플래그
	private Boolean pending;
}

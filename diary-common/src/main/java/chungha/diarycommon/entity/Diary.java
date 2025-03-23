package chungha.diarycommon.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
	private String feedback;

	// 변경된 부분
	private List<Change> changes;

	// 사용자 id
	private String userId;

	// 예약 플래그
	private Boolean pending;

}

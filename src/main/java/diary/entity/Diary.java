package diary.entity;

import diary.model.Emotion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "diary")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 제목
    @Column(nullable = false)
    private String title;
    // 일기 내용 (500자)
    @Column(nullable = false, length = 500)
    private String content;

    // 감정 (Enum)
    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    // 피드백 json
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> feedback;

}

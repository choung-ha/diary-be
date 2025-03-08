package chungha.diarycommon.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document
public abstract class BaseEntity {
	@CreatedDate
	@Field(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field(name = "updated_at")
	private LocalDateTime updatedAt;
}

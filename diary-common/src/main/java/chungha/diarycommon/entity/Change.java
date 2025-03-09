package chungha.diarycommon.entity;

public record Change(
	int startIdx,
	int endIdx,
	String changeContent
) {
}

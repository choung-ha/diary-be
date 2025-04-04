package chungha.diarycommon.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import chungha.diarycommon.model.Emotion;

@Component
public class EmotionConverter implements Converter<String, Emotion> {
	// request body의 필드는 @JsonCreator로 처리 가능하지만,
	// request parameter는 이 컨버터가 필요합니다.
	@Override
	public Emotion convert(String source) {
		return Emotion.fromString(source);
	}
}
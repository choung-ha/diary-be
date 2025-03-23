package chungha.diary.util.validation.validator;

import java.util.regex.Pattern;

import chungha.diary.util.validation.annotation.ValidMongoId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MongoIdValidator implements ConstraintValidator<ValidMongoId, String> {
	// 24자리 16진수 문자열 패턴 (소문자/대문자 모두 허용)
	private static final Pattern OBJECT_ID_PATTERN = Pattern.compile("^[0-9a-fA-F]{24}$");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		return OBJECT_ID_PATTERN.matcher(value).matches();
	}
}

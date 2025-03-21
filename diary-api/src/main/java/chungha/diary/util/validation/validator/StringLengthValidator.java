package chungha.diary.util.validation.validator;

import chungha.diary.util.validation.annotation.ValidStringLength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringLengthValidator implements ConstraintValidator<ValidStringLength, String> {
	private int min;
	private int max;

	@Override
	public void initialize(ValidStringLength stringValue) {
		this.min = stringValue.min();
		this.max = stringValue.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		// 코드 포인트 수를 기준으로 글자 수를 측정 (공백 포함)
		int actualLength = value.codePointCount(0, value.length());
		return actualLength >= min && actualLength <= max;
	}
}

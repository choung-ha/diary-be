package chungha.diary.util.validation.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = {}) // composed constraint 임을 명시
@ValidStringLength(max = 50)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidTitle {
	String message() default "유효한 제목이 아닙니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

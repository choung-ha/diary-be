package chungha.diary.util.validation.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import chungha.diary.util.validation.validator.MongoIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = {MongoIdValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidMongoId {
	String message() default "유효한 아이디 형식이 아닙니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

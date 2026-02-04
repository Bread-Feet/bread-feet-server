package kr.co.breadfeetserver.presentation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Min(value = 1, message = "사이즈는 최소 1 이상이어야 합니다.")
@Max(value = 50, message = "한 번에 최대 50개까지만 조회 가능합니다.")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface CursorSize {

    String message() default "사이즈 범위를 맞지 않게 요청했습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

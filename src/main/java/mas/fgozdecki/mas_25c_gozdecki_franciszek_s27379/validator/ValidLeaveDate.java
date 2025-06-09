package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MembershipLeaveDateValidator.class)
public @interface ValidLeaveDate {
    String message() default "Leaved ate can't be in the future, nor before " +
            "the leave date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

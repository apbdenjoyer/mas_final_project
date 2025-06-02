package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;

import java.time.LocalDateTime;

public class MembershipLeaveDateValidator implements ConstraintValidator<ValidLeaveDate, Membership> {
    @Override
    public void initialize(ValidLeaveDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Membership membership, ConstraintValidatorContext constraintValidatorContext) {
        if (membership.getLeaveDate() == null) {
            return true;
        } else return !membership.getLeaveDate().isBefore(membership.getJoinDate()) && !membership.getLeaveDate().isAfter(LocalDateTime.now());
    }
}

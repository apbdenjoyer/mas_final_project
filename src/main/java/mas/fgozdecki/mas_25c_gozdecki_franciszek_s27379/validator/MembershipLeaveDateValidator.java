package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;

import java.time.LocalDateTime;

public class MembershipLeaveDateValidator implements ConstraintValidator<ValidLeaveDate, Membership> {
    @Override
    public void initialize(ValidLeaveDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the leave date of the membership meets the required criteria.
     *
     * A membership passes validation if:
     * 1. The leave date is null (indicating an active membership), or
     * 2, The leave date is null (indicating an active membership), or
     * 3. The leave date is not before the join date AND not in the future
     *
     *
     * @param membership the membership object being validated
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the leave date is valid or null, false otherwise
     */
    @Override
    public boolean isValid(Membership membership, ConstraintValidatorContext constraintValidatorContext) {
        if (membership.getLeaveDate() == null) {
            return true;
        } else return !membership.getLeaveDate().isBefore(membership.getJoinDate()) && !membership.getLeaveDate().isAfter(LocalDateTime.now());
    }
}

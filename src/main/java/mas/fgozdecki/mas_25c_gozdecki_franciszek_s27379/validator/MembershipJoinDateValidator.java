package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;

import java.time.LocalDateTime;

public class MembershipJoinDateValidator implements ConstraintValidator<ValidJoinDate, Membership> {
    @Override
    public void initialize(ValidJoinDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the join date of the membership is not in the future.
     *
     * @param membership the membership object being validated
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the join date is valid (not in the future), false
     * otherwise
     */
    @Override
    public boolean isValid(Membership membership, ConstraintValidatorContext constraintValidatorContext) {
        return !(membership.getJoinDate().isAfter(LocalDateTime.now()));
    }
}


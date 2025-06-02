package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;

import java.time.LocalDateTime;

public class MembershipJoinDateValidator implements ConstraintValidator<ValidJoinDate, Membership> {
    @Override
    public void initialize(ValidJoinDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Membership membership, ConstraintValidatorContext constraintValidatorContext) {
        return !(membership.getJoinDate().isAfter(LocalDateTime.now()));
    }
}


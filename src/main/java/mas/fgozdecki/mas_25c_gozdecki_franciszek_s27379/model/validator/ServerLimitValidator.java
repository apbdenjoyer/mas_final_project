package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.User;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service.MembershipService;

public class ServerLimitValidator implements ConstraintValidator<ValidServerLimit, Membership> {
    @Override
    public void initialize(ValidServerLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Membership membership, ConstraintValidatorContext constraintValidatorContext) {

        if (membership == null) {
            return true;
        }

        if (!(membership.getMember() instanceof User user)) {
            return true;
        }

        if (membership.getLeaveDate() != null) {
            return true;
        }

        return MembershipService.canJoinMoreServers(user);
    }


}

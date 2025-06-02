package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service.MembershipService;


public class ServerCreationLimitValidator implements ConstraintValidator<ValidServerCreationLimit, Server> {
    @Override
    public void initialize(ValidServerCreationLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Server server, ConstraintValidatorContext constraintValidatorContext) {

        if (server.getOwner() == null) {
            return true;
        }

        User owner = server.getOwner();

        /* Because server creation == membership creation, we can use the
        same logic*/
        return MembershipService.canJoinMoreServers(owner);
    }

}



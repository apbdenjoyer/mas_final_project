package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
@Component
public class ServerCreationLimitValidator implements ConstraintValidator<ValidServerCreationLimit, Server> {


    @Override
    public void initialize(ValidServerCreationLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the owner of the server has not exceeded their server limit.
     *
     * A server passes validation if:
     *   The server has no owner set (e.g., during initial validation), or
     *   The owner has not exceeded their server limit based on subscription level
     *
     * Since server creation implies membership creation, this method uses the same logic
     * as the membership service to determine if a user can join more servers.
     *
     * @param server the server object being validated
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the server creation is valid, false otherwise
     */
    @Override
    public boolean isValid(Server server, ConstraintValidatorContext constraintValidatorContext) {
        if (server.getOwner() == null) {
            return false;
        }

        User owner = server.getOwner();

        return  canJoinMoreServers(owner);
    }

    private boolean canJoinMoreServers(User user) {
        // Existing implementation
        if (user.getSubscriptions() == null) {
            return user.getMemberships() == null ||
                    user.getMemberships().stream()
                            .filter(m -> m.getLeaveDate() == null)
                            .count() < SubscriptionLevel.NONE.getServerCountLimit();
        }

        Subscription currentSub = user.getSubscriptions().stream()
                .filter(s -> s.getEndDate() != null)
                .findFirst().orElse(null);

        Integer serverLimit;

        if (currentSub == null) {
            serverLimit = SubscriptionLevel.NONE.getServerCountLimit();
        } else {
            serverLimit = currentSub.getLevel().getServerCountLimit();
        }

        Set<Membership> memberships = user.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .collect(Collectors.toSet());

        return memberships.size() < serverLimit;
    }
}
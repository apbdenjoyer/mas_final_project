package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Subscription;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.SubscriptionLevel;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ServerLimitValidator implements ConstraintValidator<ValidServerLimit, Membership> {
    @Override
    public void initialize(ValidServerLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the member has not exceeded their server membership limit.
     *
     * A membership passes validation if:
     *   The membership is null (e.g., during initial validation), or
     *   The member is not a User instance (e.g., a Bot), or
     *   The membership has a leave date (indicating it's no longer active), or
     *   The user has not exceeded their server limit based on subscription level
     *
     * @param membership the membership object being validated
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the membership is valid, false otherwise
     */

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

        return canJoinMoreServers(user);
    }

    private boolean canJoinMoreServers(User user) {
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

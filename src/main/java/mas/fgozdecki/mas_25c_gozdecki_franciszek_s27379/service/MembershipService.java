package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    public boolean canJoinMoreServers(User user) {
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

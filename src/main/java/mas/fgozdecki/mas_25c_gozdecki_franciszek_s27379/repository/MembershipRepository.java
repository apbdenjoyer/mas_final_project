package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import jakarta.validation.constraints.NotNull;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipRepository extends CrudRepository<Membership,Long> {
    public Membership findByMemberAndServer(Account member, Server server);


    List<Membership> findAllByMemberAndLeaveDateIsNull(Account account);

    Membership findByMemberAndServerAndLeaveDateIsNull(Account account, Server server);
}

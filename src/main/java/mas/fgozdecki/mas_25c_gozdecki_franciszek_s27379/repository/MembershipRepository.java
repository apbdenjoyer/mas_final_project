package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership,Long> {
    public Membership findByMemberAndServer(Account member, Server server);
}

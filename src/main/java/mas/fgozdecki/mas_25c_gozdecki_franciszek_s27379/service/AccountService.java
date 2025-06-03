package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public List<Membership> getActiveMemberships(Account account) {
        return account.getMemberships().stream().filter(m -> m.getLeaveDate() == null).toList();
    }

    public List<Server> getServersJoined(Account account) {
        List<Membership> memberships =
                getActiveMemberships(account);
        return memberships.stream().map(Membership::getServer).toList();
    }

}

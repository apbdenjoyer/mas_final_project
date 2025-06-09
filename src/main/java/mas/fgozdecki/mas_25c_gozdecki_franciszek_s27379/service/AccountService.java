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

    /**
     * Retrieves all active memberships for a given account.
     * Active memberships are defined as those with a null leave date.
     *
     * @param account the account whose active memberships to retrieve
     * @return a list of active memberships for the account
     */
    public List<Membership> getActiveMemberships(Account account) {
        return account.getMemberships().stream().filter(m -> m.getLeaveDate() == null).toList();
    }

    /**
     * Retrieves all servers that the account has active memberships in.
     * Uses the getActiveMemberships method to first get active memberships.
     *
     * @param account the account whose joined servers to retrieve
     * @return a list of servers the account has active memberships in
     */
    public List<Server> getServersJoined(Account account) {
        List<Membership> memberships =
                getActiveMemberships(account);
        return memberships.stream().map(Membership::getServer).toList();
    }

}
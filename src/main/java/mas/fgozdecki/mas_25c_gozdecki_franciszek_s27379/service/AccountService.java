package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.AccountRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MembershipRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MessageRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ServerRepository serverRepository;
    private final MembershipRepository membershipRepository;
    private final MessageRepository messageRepository;

    public void addAccountToServer(Account account, Server server) {
        membershipRepository.save(Membership.builder()
                .server(server)
                .member(account)
                .build());
    }

    public void removeAccountFromServer(Account account, Server server) {
        Membership membership = membershipRepository.findByMemberAndServerAndLeaveDateIsNull(account, server);

        if (membership != null) {
            membership.setLeaveDate(LocalDateTime.now());
            membershipRepository.save(membership);
        } else {
            throw new IllegalArgumentException("Account is not a member of the server");
        }
    }

    public void addMessage(Account author, TextChannel textChannel, String contents) {
        messageRepository.save(Message
                .builder()
                .author(author)
                .channel(textChannel)
                .contents(contents)
                .build());
    }
}

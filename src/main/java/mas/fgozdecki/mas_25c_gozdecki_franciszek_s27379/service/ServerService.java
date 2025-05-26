package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.User;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ServerService {

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final MembershipRepository membershipRepository;
    private final ChannelRepository channelRepository;
    private final ServerEventRepository serverEventRepository;

    public void addServer(String name, User owner) {
        serverRepository.save(Server.builder()
                .name(name)
                .owner(owner)
                .build());
    }

    public void deleteServer(Long serverId, User owner) {
        Optional<Server> server = serverRepository.findByIdAndOwner(serverId, owner);

        if (server.isPresent()) {

        }

    }

    public void addAccountToServer(Account account, Server server) {
        membershipRepository.save(new Membership(account, server));
    }

    public void removeAccountFromServer(Account account, Server server) {
        Membership membership = membershipRepository.findByMemberAndServerAndLeaveDateIsNull(account, server);

        if (membership != null) {
            membership.setLeaveDate(LocalDateTime.now());
            membershipRepository.save(membership);
        } else {
            throw new IllegalArgumentException("Account is not a member of the server");
        }
    }}



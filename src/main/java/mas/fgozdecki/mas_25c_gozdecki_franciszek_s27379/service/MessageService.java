package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.transaction.Transactional;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    private final TextChannelRepository textChannelRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(UserRepository userRepository, MembershipRepository membershipRepository, ServerRepository serverRepository, TextChannelRepository textChannelRepository, MessageRepository messageRepository) {
        this.textChannelRepository = textChannelRepository;
        this.messageRepository = messageRepository;
    }

    public List<Membership> getActiveMembershipsOfAccount(Account account) {
        /* Account's active memberships */
        return account.getMemberships().stream().filter(m -> m.getLeaveDate() == null).toList();
    }
    public List<Server> getServersJoinedByAccount(Account account) {
        /* Account's servers */
        List<Membership> memberships =
                getActiveMembershipsOfAccount(account);

        /* Return only servers */
        return memberships.stream().map(Membership::getServer).toList();
    }

    public Set<Channel> getChannelsOfServer(Server server) {
        return server.getChannels();
    }

    public Set<TextChannel> getTextChannelsOfServer(Server server) {
        return (Set<TextChannel>) server.getChannels().stream().filter(
                c -> c instanceof TextChannel).map(c -> (TextChannel) c).sorted(Comparator.comparing(Channel::getName)).collect(Collectors.toSet());
    }

    public List<TextChannel> getTextChannelsFilterByAccessLevel(Account account, Server server) {

        Membership membership = getActiveMembershipsOfAccount(account).stream().filter(m -> m.getServer().equals(server)).findFirst().orElseThrow();

        int access;
        if (membership.getRole() == null) {
            access = 0;
        }else if (server.getOwner().equals(account)) {
            access=Integer.MAX_VALUE;
        } else {
            access = membership.getRole().getAccessLevel();
        }

        /* Return only those channels that should be visible by account */
        return getTextChannelsOfServer(server).stream().filter(c -> c.getRequiredAccessLevel() <= access).toList();


    }

    public List<Message> getMessagesOfTextChannel(Account account,
                                                  TextChannel channel) {
        /* Return all messages*/
        if (channel.getServer().getOwner().equals(account)) {
            return channel.getMessages().stream().sorted(Comparator.comparing(Message::getCreatedAt)).toList();
        }

        /* Return only messages visible to normal users*/
        return channel.getMessages().stream().filter(m -> m.getStatus()!=MessageStatus.SHADOWED).sorted(Comparator.comparing(Message::getCreatedAt)).toList();
    }

    public void updateMessageStatus(MessageStatus status, Message message) {
        message.setStatus(status);
        messageRepository.save(message);
    }


    /*Initial message creation, not supposed to save to db because it has to
    be checked first*/
    public Message createMessage(Account account, TextChannel channel,
                                 String contents) {
        return Message.builder()
                .channel(channel)
                .author(account)
                .contents(contents)
                .status(MessageStatus.DRAFT)
                .build();
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void editMessage(Message message, String contents) {
        message.setContents(contents);
        if (isContentBlacklistedInChannel(message.getChannel(), contents)) {
            message.setStatus(MessageStatus.SHADOWED);
        }
        messageRepository.save(message);
    }

    public boolean isContentBlacklistedInChannel(TextChannel channel,
                                                 String contents) {

        Set<String> blacklist = channel.getBlacklist();

        if (blacklist.isEmpty()) {
            return false;
        }

        String contentsMerged = contents.toLowerCase().replaceAll("\\s+", " ");
        for (String word : blacklist) {
            if (contentsMerged.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public TextChannel getTextChannelById(Long channelId) {
        return textChannelRepository.findById(channelId).orElseThrow();
    }
}

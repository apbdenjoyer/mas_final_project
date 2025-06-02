package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MessageRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.TextChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {
    private final TextChannelRepository textChannelRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(TextChannelRepository textChannelRepository, MessageRepository messageRepository) {
        this.textChannelRepository = textChannelRepository;
        this.messageRepository = messageRepository;
    }

    public List<Membership> getActiveMembershipsOfAccount(Account account) {
        return account.getMemberships().stream().filter(m -> m.getLeaveDate() == null).toList();
    }

    public List<Server> getServersJoinedByAccount(Account account) {
        List<Membership> memberships =
                getActiveMembershipsOfAccount(account);

        return memberships.stream().map(Membership::getServer).toList();
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

    public TextChannel findTextChannelById(Long channelId) {
        return textChannelRepository.findById(channelId).orElse(null);
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<TextChannel> getTextChannelsFilteredByAccess(User user, Server selectedServer) {
        Membership membership =
                user.getMemberships().stream()
                        .filter(m-> m.getLeaveDate() == null)
                        .filter(m -> m.getServer().equals(selectedServer))
                        .findFirst().orElseThrow();

        int access;
        if (membership.getRole() == null) {
            access = 0;
        }else if (selectedServer.getOwner().equals(user)) {
            access=Integer.MAX_VALUE;
        } else {
            access = membership.getRole().getAccessLevel();
        }

        return selectedServer.getChannels().stream()
                .filter(c -> c instanceof TextChannel)
                .map(c -> (TextChannel) c)
                .filter(c -> c.getRequiredAccessLevel() <= access)
                .toList();
    }

    public List<Message> getMessages(TextChannel selectedChannel, User user) {
        List<Message> messages;
        if(selectedChannel.getServer().getOwner().equals(user)) {
            messages = selectedChannel.getMessages().stream()
                    .sorted(Comparator.comparing(Message::getCreatedAt)).toList();
        } else {
            messages= selectedChannel.getMessages().stream()
                    .filter(m -> m.getStatus()!=MessageStatus.SHADOWED)
                    .sorted(Comparator.comparing(Message::getCreatedAt))
                    .toList();
        }
        return messages;
    }

    public Message createMessage(String contents, TextChannel channel,
                               User user) {
        return Message.builder()
                .channel(channel)
                .author(user)
                .contents(contents)
                .status(MessageStatus.DRAFT)
                .build();
    }

}

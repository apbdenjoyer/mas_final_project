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

    public List<Message> getMessagesFiltered(TextChannel channel,
                                             Account account) {
        List<Message> messages;
        if(channel.getServer().getOwner().equals(account)) {
            messages = channel.getMessages().stream()
                    .sorted(Comparator.comparing(Message::getCreatedAt)).toList();
        } else {
            messages= channel.getMessages().stream()
                    .filter(m -> m.getStatus()!=MessageStatus.SHADOWED)
                    .sorted(Comparator.comparing(Message::getCreatedAt))
                    .toList();
        }
        return messages;
    }

    public Message createMessage(String contents, TextChannel channel,
                               Account account) {
        return Message.builder()
                .channel(channel)
                .author(account)
                .contents(contents)
                .status(MessageStatus.DRAFT)
                .build();
    }

}

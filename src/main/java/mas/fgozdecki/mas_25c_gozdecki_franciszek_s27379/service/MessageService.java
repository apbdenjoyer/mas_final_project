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

    /**
     * Checks if the content of a message contains any blacklisted words for
     * a given channel.
     *
     * @param channel The TextChannel to check against its blacklist
     * @param contents The message contents to check
     * @return true if the content contains blacklisted words, false otherwise
     */
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

    /**
     * Retrieves a list of TextChannels that the user has access to in the specified server.
     * The channels are filtered based on the user's role and access level.
     *
     * @param user The user requesting access to channels
     * @param selectedServer The server containing the channels
     * @return A list of TextChannels that the user can access
     */
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
                                             User user) {
        List<Message> messages;
        if(channel.getServer().getOwner().equals(user)) {
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

    /**
     * Creates a new message with the provided content, channel, and author.
     * The message is initialized with DRAFT status and needs to be saved to persist.
     *
     * @param contents The text content of the message
     * @param channel The TextChannel where the message will be posted
     * @param user The user who is creating the message
     * @return A new Message instance with DRAFT status
     */
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

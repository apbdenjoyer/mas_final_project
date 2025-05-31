package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ServerRepository serverRepository;
    private final RoleRepository roleRepository;
    private final MembershipRepository membershipRepository;
    private final TextChannelRepository textChannelRepository;
    private final VoiceChannelRepository voiceChannelRepository;
    private final EmojiRepository emojiRepository;
    private final ReactionRepository reactionRepository;
    private final ServerEventRepository serverEventRepository;
    private final ParticipationRepository participationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BotRepository botRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (isDatabasePopulated()) {
            return;
        }
        
        User user1 = User.builder()
                .login("user1")
                .registrationDate(LocalDateTime.now())
                .email("user1@host")
                .password("password123")
                .build();

        User user2 = User.builder()
                .login("user2")
                .registrationDate(LocalDateTime.now())
                .email("user2@host")
                .password("password456")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        
        Bot bot1 = Bot.builder()
                .login("bot123")
                .registrationDate(LocalDateTime.now())
                .author(user2)
                .token("abc123token456")
                .build();

        Bot bot2 = Bot.builder()
                .login("bot456")
                .registrationDate(LocalDateTime.now())
                .author(user2)
                .token("xyz789token321")
                .build();

        Set<String> bot1Features = new HashSet<>();
        bot1Features.add("MODERATION");
        bot1Features.add("HELP_COMMANDS");
        bot1.setFeatures(bot1Features);

        Set<String> bot2Features = new HashSet<>();
        bot2Features.add("MUSIC_PLAYBACK");
        bot2Features.add("PLAYLIST_MANAGEMENT");
        bot2.setFeatures(bot2Features);

        botRepository.save(bot1);
        botRepository.save(bot2);

        
        Server server1 = Server.builder()
                .name(user2.getLogin()+"'s server")
                .owner(user2)
                .build();

        Server server2 = Server.builder()
                .name(user1.getLogin()+"'s server")
                .owner(user1)
                .build();

        serverRepository.save(server1);
        serverRepository.save(server2);

        
        Role adminRole = Role.builder()
                .server(server1)
                .name("Admin")
                .accessLevel(100)
                .build();
        Role moderatorRole = Role.builder()
                .server(server1)
                .name("Moderator")
                .accessLevel(50)
                .build();

        Role memberRole = Role.builder()
                .server(server1)
                .name("Member")
                .accessLevel(10)
                .build();

        roleRepository.save(adminRole);
        roleRepository.save(moderatorRole);
        roleRepository.save(memberRole);

        
        Membership membership1 = Membership.builder()
                .member(user1)
                .server(server1)
                .role(memberRole)
                .joinDate(LocalDateTime.now().minusDays(20))
                .leaveDate(null)
                .build();

        Membership membership2 = Membership.builder()
                .member(user2)
                .server(server1)
                .role(adminRole)
                .joinDate(LocalDateTime.now().minusDays(30))
                .leaveDate(null)
                .build();

        Membership membership3 = Membership.builder()
                .member(user1)
                .server(server2)
                .role(memberRole)
                .joinDate(LocalDateTime.now().minusDays(20))
                .leaveDate(null)
                .build();

        Membership membership4 = Membership.builder()
                .member(user2)
                .server(server2)
                .role(null)
                .joinDate(LocalDateTime.now().minusDays(30))
                .leaveDate(null)
                .build();

        membershipRepository.save(membership1);
        membershipRepository.save(membership2);
        membershipRepository.save(membership3);
        membershipRepository.save(membership4);

        TextChannel textChannel1 = TextChannel.builder()
                .server(server1)
                .name("general")
                .requiredAccessLevel(0)
                .build();

        TextChannel textChannel2 = TextChannel.builder()
                .server(server1)
                .name("off-topic")
                .requiredAccessLevel(0)
                .build();

        TextChannel textChannel3 = TextChannel.builder()
                .server(server1)
                .name("admin-chat")
                .requiredAccessLevel(100)
                .build();

        TextChannel textChannel4 = TextChannel.builder()
                .server(server2)
                .name("general2")
                .requiredAccessLevel(0)
                .build();

        TextChannel textChannel5 = TextChannel.builder()
                .server(server2)
                .name("off-topic2")
                .requiredAccessLevel(0)
                .build();

        TextChannel textChannel6 = TextChannel.builder()
                .server(server2)
                .name("admin-chat2")
                .requiredAccessLevel(100)
                .build();

        Set<String> blacklistedWords = new HashSet<>();
        blacklistedWords.add("pjatk");
        textChannel4.setBlacklist(blacklistedWords);

        textChannelRepository.save(textChannel1);
        textChannelRepository.save(textChannel2);
        textChannelRepository.save(textChannel3);
        textChannelRepository.save(textChannel4);
        textChannelRepository.save(textChannel5);
        textChannelRepository.save(textChannel6);

        VoiceChannel voiceChannel1 = VoiceChannel.builder()
                .server(server1)
                .name("vc #1")
                .requiredAccessLevel(0)
                .bitrate(64)
                .maxUsers(10)
                .build();

        VoiceChannel voiceChannel2 = VoiceChannel.builder()
                .server(server1)
                .name("vc #2")
                .requiredAccessLevel(0)
                .bitrate(96)
                .maxUsers(5)
                .build();

        VoiceChannel voiceChannel3 = VoiceChannel.builder()
                .server(server1)
                .name("vc #3")
                .requiredAccessLevel(0)
                .bitrate(64)
                .maxUsers(20)
                .build();

        voiceChannelRepository.save(voiceChannel1);
        voiceChannelRepository.save(voiceChannel2);
        voiceChannelRepository.save(voiceChannel3);

        
        Message message1 = Message.builder()
                .contents("Hello!")
                .author(user2)
                .channel(textChannel1)
                .createdAt(LocalDateTime.now().minusDays(10))
                .status(MessageStatus.APPROVED)
                .build();

        Message message2 = Message.builder()
                .contents("Hi!")
                .author(user1)
                .channel(textChannel1)
                .createdAt(LocalDateTime.now().minusDays(9))
                .status(MessageStatus.APPROVED)
                .build();

        Message message3 = Message.builder()
                .contents("I love cats!")
                .author(user1)
                .channel(textChannel2)
                .createdAt(LocalDateTime.now().minusDays(8))
                .status(MessageStatus.APPROVED)
                .build();

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);

        
        Emoji emoji1 = Emoji.builder()
                .server(server1)
                .name("smiley")
                .imageData(new byte[]{0x01, 0x02, 0x03, 0x04})
                .build();

        Emoji emoji2 = Emoji.builder()
                .server(server1)
                .name("heart")
                .imageData(new byte[]{0x05, 0x06, 0x07, 0x08})
                .build();

        Emoji emoji3 = Emoji.builder()
                .server(server1)
                .name("thumbsup")
                .imageData(new byte[]{0x09, 0x0A, 0x0B, 0x0C})
                .build();

        emojiRepository.save(emoji1);
        emojiRepository.save(emoji2);
        emojiRepository.save(emoji3);

        
        Reaction reaction1 = Reaction.builder()
                .emoji(emoji1)
                .message(message1)
                .user(user2)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();

        Reaction reaction2 = Reaction.builder()
                .emoji(emoji2)
                .message(message1)
                .user(user1)
                .createdAt(LocalDateTime.now().minusDays(4))
                .build();

        Reaction reaction3 = Reaction.builder()
                .emoji(emoji2)
                .message(message1)
                .user(user2)
                .createdAt(LocalDateTime.now().minusDays(3))
                .build();

        reactionRepository.save(reaction1);
        reactionRepository.save(reaction2);
        reactionRepository.save(reaction3);


        ServerEvent event1 = ServerEvent.builder()
                .name("event #1")
                .description("first event")
                .server(server1)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .status(ServerEvent.EventStatus.SCHEDULED)
                .build();

        ServerEvent event2 = ServerEvent.builder()
                .name("event #2")
                .description("second event")
                .server(server1)
                .startTime(LocalDateTime.now().plusHours(12))
                .endTime(LocalDateTime.now().plusHours(16))
                .status(ServerEvent.EventStatus.SCHEDULED)
                .build();

        ServerEvent event3 = ServerEvent.builder()
                .name("event #3")
                .description("third event")
                .server(server2)
                .startTime(LocalDateTime.now().plusDays(3))
                .endTime(LocalDateTime.now().plusDays(4))
                .status(ServerEvent.EventStatus.SCHEDULED)
                .build();

        serverEventRepository.save(event1);
        serverEventRepository.save(event2);
        serverEventRepository.save(event3);

        
        Participation participation1 = Participation.builder()
                .participant(membership1)
                .event(event1)
                .signDate(LocalDateTime.now().minusDays(2))
                .build();

        Participation participation2 = Participation.builder()
                .participant(membership1)
                .event(event2)
                .signDate(LocalDateTime.now().minusDays(1))
                .build();

        Participation participation3 = Participation.builder()
                .participant(membership2)
                .event(event3)
                .signDate(LocalDateTime.now())
                .build();

        participationRepository.save(participation1);
        participationRepository.save(participation2);
        participationRepository.save(participation3);

        
        Subscription subscription1 = Subscription.builder()
                .user(user2)
                .level(Subscription.SubscriptionLevel.PRO)
                .startDate(LocalDateTime.now().minusDays(60))
                .endDate(LocalDateTime.now().plusDays(30))
                .build();

        Subscription subscription2 = Subscription.builder()
                .user(user1)
                .level(Subscription.SubscriptionLevel.CLASSIC)
                .startDate(LocalDateTime.now().minusDays(30))
                .endDate(LocalDateTime.now().plusDays(60))
                .build();

        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
    }

    private boolean isDatabasePopulated() {
        return userRepository.count() > 0 ||
                serverRepository.count() > 0 ||
                messageRepository.count() > 0 ||
                roleRepository.count() > 0 ||
                membershipRepository.count() > 0 ||
                textChannelRepository.count() > 0 ||
                voiceChannelRepository.count() > 0 ||
                emojiRepository.count() > 0 ||
                reactionRepository.count() > 0 ||
                serverEventRepository.count() > 0 ||
                participationRepository.count() > 0 ||
                subscriptionRepository.count() > 0 ||
                botRepository.count() > 0;
    }

}
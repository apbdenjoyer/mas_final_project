package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.TextChannelRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.UserRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class MessageController {

    private final String CURRENT_USER = "currentUser";
    private final String SERVERS = "servers";
    private final String CURRENT_SERVER = "currentServer";
    private final String CHANNELS = "channels";
    private final String CURRENT_CHANNEL = "currentChannel";
    private final String MESSAGES = "messages";
    private final String ERROR = "error";

    /* used for accessing the logged-in user*/
    private final UserRepository userRepository;

    private final TextChannelRepository textChannelRepository;
    private final MessageService messageService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "user/login";
    }


    @PostMapping("/login")
    public String processLogin(Model model, @RequestParam String email,
                               @RequestParam String password, HttpSession session) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmailAndPassword(email,
                password));

        if (user.isEmpty()) {
            model.addAttribute(ERROR, "Invalid credentials");
            return "user/login";
        }
        
        session.setAttribute(CURRENT_USER + "_ID", user.get().getId());

        return "redirect:/user";
    }

    @GetMapping("")
    @Transactional
    public String getUserHomePage(Model model, HttpSession session,
                              @RequestParam(required = false) Long serverId,
                              @RequestParam(required = false) Long channelId) {

        Long userID = (Long) session.getAttribute(CURRENT_USER+"_ID");

        if (userID == null) {
            return "redirect:/user/login";
        }

        User user = userRepository.findById(userID).orElse(null);

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute(CURRENT_USER, user);
        List<Server> servers = messageService.getServersJoinedByAccount(user);
        model.addAttribute(SERVERS, servers);

        Server selectedServer = null;

        if (!servers.isEmpty()) {
        if (serverId != null) {
            selectedServer =
                    servers.stream().filter(s -> s.getId().equals(serverId)).findFirst().orElse(null);
            if (selectedServer != null) {
                model.addAttribute(CURRENT_SERVER, selectedServer);
            }
        }
    }

    TextChannel selectedChannel = null;

    if (selectedServer != null) {
        List<TextChannel> channels =
                messageService.getTextChannelsFilterByAccessLevel(user, selectedServer);

        model.addAttribute(CHANNELS, channels);

        if (!channels.isEmpty() && channelId != null) {
            selectedChannel =
                    channels.stream().filter(c -> c.getId().equals(channelId)).findFirst().orElse(null);
            if (selectedChannel != null) {
                model.addAttribute(CURRENT_CHANNEL, selectedChannel);

                List<Message> messages =
                        messageService.getMessagesOfTextChannel(user, selectedChannel);
                model.addAttribute(MESSAGES, messages);

            }
        }
    }

    return "user/home";
}

    @PostMapping("/servers/channels/{channelId}/send")
    public String sendMessage(Model model, HttpSession session,
                         @RequestParam String contents,
                         @PathVariable Long channelId) {

        Long userID = (Long) session.getAttribute(CURRENT_USER+"_ID");

        if (userID == null) {
            return "redirect:/user/login";
        }

        User user = userRepository.findById(userID).orElse(null);

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute(CURRENT_USER, user);

        if (contents == null || contents.isBlank()) {
            return "redirect:/user";
        }

        try {
        // Get the channel by ID from the repository
            TextChannel channel = textChannelRepository.findById(channelId).orElse(null);

            if (channel == null) {
                return "redirect:/user";
            }

            Message message = messageService.createMessage(user, channel, contents);

            if (messageService.isContentBlacklistedInChannel(channel, contents)) {
                message.setStatus(MessageStatus.SHADOWED);
            } else {
                message.setStatus(MessageStatus.APPROVED);
            }

            messageService.saveMessage(message);

            return "redirect:/user?serverId=" + channel.getServer().getId() + "&channelId=" + channelId;

        } catch (Exception e) {
            return "redirect:/user";
        }
    }
/*
    @PostMapping("/user/servers/channels/{channelId}/{msgId}/edit")
    public String editMessage(Model model, @ModelAttribute User user,
                              @PathVariable Long channelId,
                              @PathVariable Long msgId) {
        TextChannel channel = messageService.getTextChannelById(channelId);
        Optional<Message> message =
                channel.getMessages().stream().filter(c -> c.getId()==msgId).findFirst();

        if (message.isEmpty()) {
            *//* idk*//*
        }
    }*/
}
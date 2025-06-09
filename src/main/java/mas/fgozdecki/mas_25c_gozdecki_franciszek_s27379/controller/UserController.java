package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.UserRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {


    private final UserRepository userRepository;
    private final MessageService messageService;

    @Autowired
    public UserController(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String processLogin(Model model, @RequestParam String email,
                               @RequestParam String password, HttpSession session) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmailAndPassword(email,
                password));

        if (user.isEmpty()) {
            model.addAttribute("error", "Invalid credentials");
            return "user/login";
        }

        session.setAttribute("currentUser_ID", user.get().getId());

        return "redirect:/user";
    }

    @GetMapping("")
    @Transactional
    public String getUserHomePage(Model model, HttpSession session,
                                  @RequestParam(required = false) Long serverId,
                                  @RequestParam(required = false) Long channelId) {

        User user = getUserIfExists(session);
        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("currentUser", user);


        List<Server> servers = messageService.getServersJoinedByAccount(user);
        model.addAttribute("servers", servers);

        Server selectedServer;

        if (!servers.isEmpty()) {
            if (serverId != null) {
                selectedServer =
                        servers.stream().filter(s -> s.getId().equals(serverId)).findFirst().orElse(null);
                if (selectedServer != null) {
                    model.addAttribute("currentServer", selectedServer);
                }
            } else {
                selectedServer = null;
            }
        } else {
            selectedServer = null;
        }

        TextChannel selectedChannel;

        if (selectedServer != null) {

            List<TextChannel> channels = messageService.getTextChannelsFilteredByAccess(user,
                    selectedServer);

            model.addAttribute("channels", channels);

            if (!channels.isEmpty() && channelId != null) {
                selectedChannel =
                        channels.stream().filter(c -> c.getId().equals(channelId)).findFirst().orElse(null);
                if (selectedChannel != null) {
                    model.addAttribute("currentChannel", selectedChannel);


                    List<Message> messages =
                            messageService.getMessages(selectedChannel, user);

                    model.addAttribute("messages", messages);

                }
            }
        }

        return "/user/home";
    }

    private User getUserIfExists(HttpSession session) {
        Long userID = (Long) session.getAttribute("currentUser_ID");

        if (userID == null) {
            return null;
        }

        return userRepository.findById(userID).orElse(null);
    }


    @PostMapping("/servers/channels/{channelId}/send")
    public String sendMessage(Model model, HttpSession session,
                              @RequestParam String contents,
                              @PathVariable Long channelId) {

        User user = getUserIfExists(session);

        if (user == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("currentUser", user);


        if (contents == null || contents.isBlank()) {
            return "redirect:/user";
        }

        try {
            TextChannel channel =
                    messageService.findTextChannelById(channelId);

            if (channel == null) {
                return "redirect:/user";
            }

            Message message = messageService.createMessage(contents, channel,
                    user);

            if (messageService.isContentBlacklistedInChannel(channel,
                    contents)) {
                message.setStatus(MessageStatus.SHADOWED);
            } else {
                message.setStatus(MessageStatus.APPROVED);
            }
            messageService.save(message);
            
            return "redirect:/user?serverId=" + channel.getServer().getId() + "&channelId=" + channelId;

        } catch (Exception e) {
            return "redirect:/user";
        }
    }

}
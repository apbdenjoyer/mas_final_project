package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.UserRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service.AccountService;
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


    /* used for accessing the logged-in user*/
    private final UserRepository userRepository;

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public UserController(UserRepository userRepository, AccountService accountService, MessageService messageService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Displays the login page for users.
     *
     * @return The view name for the login page
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    /**
     * Processes a login attempt with the provided credentials.
     *
     * @param model The Spring MVC model for passing attributes to the view
     * @param email The email address provided by the user
     * @param password The password provided by the user
     * @param session The HTTP session to store user information upon successful login
     * @return A redirect to the user home page if login is successful, otherwise returns to the login page with an error message
     */
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

    /**
     * Displays the user's home page with servers, channels, and messages.
     * Handles the display of content based on a selected server and channel.
     *
     * @param model The Spring MVC model for passing attributes to the view
     * @param session The HTTP session containing the user's login information
     * @param serverId Optional parameter specifying which server to display
     * @param channelId Optional parameter specifying which channel to display
     * @return The view name for the home page or a redirect to login if
     * the user is not authenticated
     */
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


        List<Server> servers = accountService.getServersJoined(user);
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
                            messageService.getMessagesFiltered(selectedChannel, user);

                    model.addAttribute("messages", messages);

                }
            }
        }

        return "/user/home";
    }

    /**
     * Helper method to retrieve the currently logged-in user from the session.
     *
     * @param session The HTTP session containing the user's ID
     * @return The User object if found, or null if not logged in or user doesn't exist
     */
    private User getUserIfExists(HttpSession session) {
        Long userID = (Long) session.getAttribute("currentUser_ID");

        if (userID == null) {
            return null;
        }
        return userRepository.findById(userID).orElse(null);
    }

    /**
     * Processes the sending of a new message in a specific channel.
     *
     * @param model The Spring MVC model for passing attributes to the view
     * @param session The HTTP session containing the user's login information
     * @param contents The text content of the message to be sent
     * @param channelId The ID of the channel where the message should be sent
     * @return A redirect to the user home page, focusing on the channel where the message was sent, or a redirect to login if the user is not authenticated
     */
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
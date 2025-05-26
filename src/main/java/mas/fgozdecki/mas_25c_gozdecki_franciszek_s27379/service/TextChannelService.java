package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Message;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.TextChannel;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.AccountRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MessageRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.ServerRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.TextChannelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TextChannelService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;
    private final ServerRepository serverRepository;
    private final TextChannelRepository textChannelRepository;

    public void addMessageToTextChannel(Account author, TextChannel textChannel, String contents) {
        messageRepository.save(Message.builder()
                .author(author)
                .channel(textChannel)
                .contents(contents)
                .build());
    }

    public void deleteMessageFromTextChannel(Message message) {
        messageRepository.delete(message);
    }


}

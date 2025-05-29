package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import jakarta.validation.constraints.NotNull;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageRepository extends CrudRepository<Message, Long> {
    public Message findMessageByAuthorAndChannel(Account author,
                                                 TextChannel channel);

    List<Message> findAllByChannel(TextChannel channel);

    Set<Message> findAllByAuthor(Account account);

    Set<Message> findAllByAuthorAndChannel(Account author, TextChannel channel);

    List<Message> findAllByChannelAndStatus(TextChannel channel, @NotNull MessageStatus status);

    List<Message> findAllByChannelAndStatusNot(TextChannel channel, MessageStatus messageStatus);
}

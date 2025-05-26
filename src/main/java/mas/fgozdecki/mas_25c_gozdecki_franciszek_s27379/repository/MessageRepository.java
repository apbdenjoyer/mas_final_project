package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Channel;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Message;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.TextChannel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long> {
    public Message findMessageByAuthorAndChannel(Account author,
                                                 TextChannel channel);
}

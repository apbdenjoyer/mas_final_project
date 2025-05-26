package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.TextChannel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TextChannelRepository extends CrudRepository<TextChannel, Long> {
    public TextChannel findByServerAndName(Server server, String name);

    Optional<TextChannel> findByIdAndServer(Long textChannelId, Server server);
}

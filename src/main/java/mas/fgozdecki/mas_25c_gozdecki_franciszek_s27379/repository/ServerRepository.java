package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends CrudRepository<Server, Long> {

    Server findByName(String name);
    List<Server> findByOwner(User owner);

    Optional<Server> findByIdAndOwner(Long serverId, User owner);
}

package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Role;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findByServer(Server server);
    Optional<Role> findByServerAndName(Server server, String name);
}

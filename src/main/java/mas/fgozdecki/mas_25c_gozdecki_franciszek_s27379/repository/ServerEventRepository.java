package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.ServerEvent;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ServerEventRepository extends CrudRepository<ServerEvent, Long> {

  ServerEvent findByNameAndServerAndStartTimeAndEndTime(String name, Server server, LocalDateTime startTime, LocalDateTime endTime);
}

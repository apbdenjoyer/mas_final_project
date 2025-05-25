package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServerRepositoryTest {

    @Autowired
    private ServerRepository serverRepository;

    @Test
    public void testRequiredDependencies() {
        assertNotNull(serverRepository);
    }

    @Test
    public void testFetchServersWithName() {
        Iterable<Server> all = serverRepository.findAll();
        for (Server server : all) {
            System.out.println(server);
        }
    }

}
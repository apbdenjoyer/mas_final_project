package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Membership;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Server;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;



    User user;
    @Autowired
    private ServerRepository serverRepository;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .login("testUser")
                .email("testEmail@example.com")
                .password("testPassword123")
                .registrationDate(LocalDateTime.now())
                .memberships(new HashSet<>())
                .servers(new HashSet<>())
                .participations(new HashSet<>())
                .createdBots(new HashSet<>())
                .build();

        accountRepository.save(user);
        entityManager.flush();
    }

    @Test
    public void testRequiredDependencies() {
        assertNotNull(accountRepository);
    }

    @Test
    public void testFetchAccounts() {
        for (Account account : accountRepository.findAll()) {
            System.out.println(account);
        }
    }

    @Test
    public void testSaveAccount(){
        accountRepository.save(user);
        entityManager.flush();
        long count = accountRepository.count();
        assertEquals(3, count);
    }

    @Test
    public void testSaveInvalidAccountLogin(){
        assertThrows(ConstraintViolationException.class, () -> {
            user.setLogin("ab");
            accountRepository.save(user);
            entityManager.flush();
        });
    }

    @Test
    public void testFindByLogin(){
        List<Account> accounts = accountRepository.findByLogin("testUser");
        System.out.println(accounts);
        assertEquals(1, accounts.size());
    }

    @Test
    public void testAddAccountToServer() {
        Optional<Server> s = serverRepository.findById(2001L);

        System.out.println(s.get());

        if (s.isPresent()) {
            user.addToServer(s.get());
        }

        accountRepository.save(user);
        entityManager.flush();

        Set<Membership> memberships = user.getMemberships();
        Set<Membership> memberships2 = s.get().getMemberships();
        System.out.println(memberships2);
        assertEquals(1, memberships.size());

    }
}
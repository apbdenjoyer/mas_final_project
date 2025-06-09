package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MembershipRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.ServerRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.SubscriptionRepository;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MembershipServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User freeUser;
    private User classicUser;
    private User proUser;
    private List<Server> testServers;

    @BeforeEach
    public void setUp() {
        System.out.println("====== SETUP STARTING ======");

        // Create test users with different subscription levels
        freeUser = User.builder()
                .login("freeUser")
                .email("free@example.com")
                .password("password123")
                .registrationDate(LocalDateTime.now())
                .memberships(new HashSet<>())
                .subscriptions(new HashSet<>())
                .build();

        classicUser = User.builder()
                .login("classicUser")
                .email("classic@example.com")
                .password("password123")
                .registrationDate(LocalDateTime.now())
                .memberships(new HashSet<>())
                .subscriptions(new HashSet<>())
                .build();

        proUser = User.builder()
                .login("proUser")
                .email("pro@example.com")
                .password("password123")
                .registrationDate(LocalDateTime.now())
                .memberships(new HashSet<>())
                .subscriptions(new HashSet<>())
                .build();

        System.out.println("Created test users: " + freeUser.getLogin() + ", " +
                classicUser.getLogin() + ", " + proUser.getLogin());

        userRepository.save(freeUser);
        userRepository.save(classicUser);
        userRepository.save(proUser);

        System.out.println("Saved users with IDs: " + freeUser.getId() + ", " +
                classicUser.getId() + ", " + proUser.getId());

        // Create subscriptions
        Subscription classicSubscription = Subscription.builder()
                .user(classicUser)
                .level(SubscriptionLevel.CLASSIC)
                .startDate(LocalDateTime.now().minusDays(30))
                .endDate(LocalDateTime.now().plusDays(30))
                .build();

        Subscription proSubscription = Subscription.builder()
                .user(proUser)
                .level(SubscriptionLevel.PRO)
                .startDate(LocalDateTime.now().minusDays(30))
                .endDate(LocalDateTime.now().plusDays(30))
                .build();

        System.out.println("Created subscriptions: CLASSIC for " + classicUser.getLogin() +
                ", PRO for " + proUser.getLogin());

        subscriptionRepository.save(classicSubscription);
        subscriptionRepository.save(proSubscription);

        // Update users with subscriptions
        Set<Subscription> classicSubs = new HashSet<>();
        classicSubs.add(classicSubscription);
        classicUser.setSubscriptions(classicSubs);

        Set<Subscription> proSubs = new HashSet<>();
        proSubs.add(proSubscription);
        proUser.setSubscriptions(proSubs);

        userRepository.save(classicUser);
        userRepository.save(proUser);

        // Create test servers
        testServers = new ArrayList<>();
        for (int i = 1; i <= 60; i++) { // Create more servers than any user can join
            Server server = Server.builder()
                    .name("Test Server " + i)
                    .owner(proUser) // Use pro user as owner for all
                    .memberships(new HashSet<>())
                    .build();

            testServers.add(server);
            serverRepository.save(server);
        }

        System.out.println("Created and saved 60 test servers");
        System.out.println("====== SETUP COMPLETE ======");
    }

    private void createMembershipsUpToLimit(User user, int limit) {
        System.out.println("Creating " + limit + " memberships for " + user.getLogin());
        for (int i = 0; i < limit; i++) {
            Server server = testServers.get(i);
            System.out.println("Creating membership #" + (i+1) + " for server: " + server.getName());

            Membership membership = Membership.builder()
                    .member(user)
                    .server(server)
                    .joinDate(LocalDateTime.now().minusDays(10))
                    .build();

            membershipRepository.save(membership);

            // Update user's memberships
            Set<Membership> userMemberships = user.getMemberships();
            if (userMemberships == null) {
                userMemberships = new HashSet<>();
            }
            userMemberships.add(membership);
            user.setMemberships(userMemberships);
        }
        userRepository.save(user);
        System.out.println("Finished creating " + limit + " memberships for " + user.getLogin());
    }

    @Test
    public void testFreeUserServerLimit() {
        System.out.println("\n====== STARTING TEST: testFreeUserServerLimit ======");

        int freeLimit = SubscriptionLevel.NONE.getServerCountLimit();
        System.out.println("Free user server limit: " + freeLimit);

        // Create memberships up to the limit (should succeed)
        createMembershipsUpToLimit(freeUser, freeLimit);

        // Verify the user has joined exactly the limit number of servers
        freeUser = userRepository.findById(freeUser.getId()).orElseThrow();
        long activeCount = freeUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Free user active membership count: " + activeCount);
        assertEquals(freeLimit, activeCount, "Free user should have exactly " + freeLimit + " active memberships");

        // Now try to create one more membership (should fail with validation exception)
        Server extraServer = testServers.get(freeLimit);
        System.out.println("Attempting to create one more membership (exceeding limit): " + extraServer.getName());

        Membership invalidMembership = Membership.builder()
                .member(freeUser)
                .server(extraServer)
                .joinDate(LocalDateTime.now())
                .build();

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            membershipRepository.save(invalidMembership);
        });

        System.out.println("Exception thrown as expected: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("User has exceeded their server limit"),
                "Exception should mention server limit validation");

        // Verify the user still has only the limit number of servers
        freeUser = userRepository.findById(freeUser.getId()).orElseThrow();
        activeCount = freeUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Free user active membership count after failed attempt: " + activeCount);
        assertEquals(freeLimit, activeCount, "Free user should still have only " + freeLimit + " active memberships");

        System.out.println("====== TEST COMPLETE: testFreeUserServerLimit ======");
    }

    @Test
    public void testClassicUserServerLimit() {
        System.out.println("\n====== STARTING TEST: testClassicUserServerLimit ======");

        int classicLimit = SubscriptionLevel.CLASSIC.getServerCountLimit();
        System.out.println("Classic user server limit: " + classicLimit);

        // Create memberships up to the limit (should succeed)
        createMembershipsUpToLimit(classicUser, classicLimit);

        // Verify the user has joined exactly the limit number of servers
        classicUser = userRepository.findById(classicUser.getId()).orElseThrow();
        long activeCount = classicUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Classic user active membership count: " + activeCount);
        assertEquals(classicLimit, activeCount, "Classic user should have exactly " + classicLimit + " active memberships");

        // Now try to create one more membership (should fail with validation exception)
        Server extraServer = testServers.get(classicLimit);
        System.out.println("Attempting to create one more membership (exceeding limit): " + extraServer.getName());

        Membership invalidMembership = Membership.builder()
                .member(classicUser)
                .server(extraServer)
                .joinDate(LocalDateTime.now())
                .build();

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            membershipRepository.save(invalidMembership);
        });

        System.out.println("Exception thrown as expected: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("User has exceeded their server limit"),
                "Exception should mention server limit validation");

        // Verify the user still has only the limit number of servers
        classicUser = userRepository.findById(classicUser.getId()).orElseThrow();
        activeCount = classicUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Classic user active membership count after failed attempt: " + activeCount);
        assertEquals(classicLimit, activeCount, "Classic user should still have only " + classicLimit + " active memberships");

        System.out.println("====== TEST COMPLETE: testClassicUserServerLimit ======");
    }

    @Test
    public void testProUserServerLimit() {
        System.out.println("\n====== STARTING TEST: testProUserServerLimit ======");

        int proLimit = SubscriptionLevel.PRO.getServerCountLimit();
        System.out.println("Pro user server limit: " + proLimit);

        // Create memberships up to the limit (should succeed)
        createMembershipsUpToLimit(proUser, proLimit);

        // Verify the user has joined exactly the limit number of servers
        proUser = userRepository.findById(proUser.getId()).orElseThrow();
        long activeCount = proUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Pro user active membership count: " + activeCount);
        assertEquals(proLimit, activeCount, "Pro user should have exactly " + proLimit + " active memberships");

        // Now try to create one more membership (should fail with validation exception)
        Server extraServer = testServers.get(proLimit);
        System.out.println("Attempting to create one more membership (exceeding limit): " + extraServer.getName());

        Membership invalidMembership = Membership.builder()
                .member(proUser)
                .server(extraServer)
                .joinDate(LocalDateTime.now())
                .build();

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            membershipRepository.save(invalidMembership);
        });

        System.out.println("Exception thrown as expected: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("User has exceeded their server limit"),
                "Exception should mention server limit validation");

        // Verify the user still has only the limit number of servers
        proUser = userRepository.findById(proUser.getId()).orElseThrow();
        activeCount = proUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Pro user active membership count after failed attempt: " + activeCount);
        assertEquals(proLimit, activeCount, "Pro user should still have only " + proLimit + " active memberships");

        System.out.println("====== TEST COMPLETE: testProUserServerLimit ======");
    }

    @Test
    public void testServerCreationLimit() {
        System.out.println("\n====== STARTING TEST: testServerCreationLimit ======");

        int freeLimit = SubscriptionLevel.NONE.getServerCountLimit();
        System.out.println("Free user server limit: " + freeLimit);

        // First, create memberships up to the limit-1 (to leave room for the server owner membership)
        createMembershipsUpToLimit(freeUser, freeLimit - 1);

        // Now try to create a new server (should succeed and automatically create membership)
        System.out.println("Creating a new server as free user (should succeed)");

        Server newServer = Server.builder()
                .name("Free User Server")
                .owner(freeUser)
                .build();

        serverRepository.save(newServer);
        System.out.println("Successfully created server as free user");

        // Manually create the membership for the server owner
        Membership ownerMembership = Membership.builder()
                .member(freeUser)
                .server(newServer)
                .joinDate(LocalDateTime.now())
                .build();

        membershipRepository.save(ownerMembership);
        System.out.println("Created owner membership for the new server");

        // Update relationships in memory
        if (freeUser.getMemberships() == null) {
            freeUser.setMemberships(new HashSet<>());
        }
        freeUser.getMemberships().add(ownerMembership);

        if (newServer.getMemberships() == null) {
            newServer.setMemberships(new HashSet<>());
        }
        newServer.getMemberships().add(ownerMembership);

        // Verify user now has exactly the limit memberships
        freeUser = userRepository.findById(freeUser.getId()).orElseThrow();
        long activeCount = freeUser.getMemberships().stream()
                .filter(m -> m.getLeaveDate() == null)
                .count();

        System.out.println("Free user active membership count after server creation: " + activeCount);
        assertEquals(freeLimit, activeCount, "Free user should have exactly " + freeLimit + " active memberships");

        // Now try to create another server (should fail)
        System.out.println("Attempting to create another server as free user (should fail)");

        Server anotherServer = Server.builder()
                .name("Another Free User Server")
                .owner(freeUser)
                .build();

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            serverRepository.save(anotherServer);
        });

        System.out.println("Exception thrown as expected: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("User has exceeded their server limit"),
                "Exception should mention server limit validation");

        System.out.println("====== TEST COMPLETE: testServerCreationLimit ======");
    }
}
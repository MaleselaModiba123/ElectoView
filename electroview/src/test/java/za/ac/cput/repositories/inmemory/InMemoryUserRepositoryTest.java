package za.ac.cput.repositories.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.Enums.AccountStatus;
import za.ac.cput.Enums.Role;
import za.ac.cput.domain.User;
import za.ac.cput.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private IUserRepository repository;
    private User testUser;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();

        testUser = User.builder()
                .name("Malesela Modiba")
                .email("malesela@cput.ac.za")
                .passwordHash("hashed")
                .role(Role.ANALYST)
                .build();
    }

    @Test
    void save_NewUser_ReturnsSavedUser() {
        User saved = repository.save(testUser);

        assertNotNull(saved);
        assertEquals(testUser.getId(), saved.getId());
        assertEquals(1, repository.count());
    }

    @Test
    void save_NullUser_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.save(null));
    }

    @Test
    void findById_ExistingUser_ReturnsUser() {
        repository.save(testUser);

        Optional<User> found = repository.findById(testUser.getId());

        assertTrue(found.isPresent());
        assertEquals("malesela@cput.ac.za", found.get().getEmail());
    }

    @Test
    void findById_NonExistentUser_ReturnsEmpty() {
        Optional<User> found = repository.findById("non-existent-id");

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_EmptyRepository_ReturnsEmptyList() {
        List<User> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    void findAll_MultipleUsers_ReturnsAll() {
        User user2 = User.builder()
                .name("Phindiwe Bambata")
                .email("phindiwe@cput.ac.za")
                .passwordHash("hashed2")
                .role(Role.CONSUMER)
                .build();

        repository.save(testUser);
        repository.save(user2);

        List<User> all = repository.findAll();

        assertEquals(2, all.size());
    }

    // @Test
    // void deleteById_ExistingUser_RemovesUser() {
    //     repository.save(testUser);
    //     assertEquals(1, repository.count());

    //     repository.deleteById(testUser.getId());

    //     assertEquals(0, repository.count());
    //     assertFalse(repository.existsById(testUser.getId()));
    // }

    @Test
    void delete_ExistingUser_RemovesUser() {
        repository.save(testUser);

        repository.delete(testUser);

        assertEquals(0, repository.count());
    }

    @Test
    void existsById_AfterSave_ReturnsTrue() {
        repository.save(testUser);

        assertTrue(repository.existsById(testUser.getId()));
    }

    @Test
    void existsById_NonExistent_ReturnsFalse() {
        assertFalse(repository.existsById("nope"));
    }

    @Test
    void count_EmptyRepository_ReturnsZero() {
        assertEquals(0, repository.count());
    }

    @Test
    void findByEmail_ExistingUser_ReturnsUser() {
        repository.save(testUser);

        Optional<User> found = repository.findByEmail("malesela@cput.ac.za");

        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get().getId());
    }

    @Test
    void findByEmail_CaseInsensitive_ReturnsUser() {
        repository.save(testUser);

        Optional<User> found = repository.findByEmail("MALESELA@CPUT.AC.ZA");

        assertTrue(found.isPresent());
    }

    @Test
    void findByEmail_NonExistent_ReturnsEmpty() {
        Optional<User> found = repository.findByEmail("nobody@nowhere.com");

        assertTrue(found.isEmpty());
    }

    @Test
    void existsByEmail_AfterSave_ReturnsTrue() {
        repository.save(testUser);

        assertTrue(repository.existsByEmail("malesela@cput.ac.za"));
    }

    @Test
    void findByRole_ReturnsMatchingUsers() {
        User analyst1 = User.builder()
                .name("Analyst 1")
                .email("a1@test.com")
                .passwordHash("h")
                .role(Role.ANALYST)
                .build();
        User analyst2 = User.builder()
                .name("Analyst 2")
                .email("a2@test.com")
                .passwordHash("h")
                .role(Role.ANALYST)
                .build();
        User admin = User.builder()
                .name("Admin")
                .email("admin@test.com")
                .passwordHash("h")
                .role(Role.ADMINISTRATOR)
                .build();

        repository.save(analyst1);
        repository.save(analyst2);
        repository.save(admin);

        List<User> analysts = repository.findByRole(Role.ANALYST);

        assertEquals(2, analysts.size());
    }

    @Test
    void findByStatus_ReturnsMatchingUsers() {
        testUser.activate();
        repository.save(testUser);

        List<User> active = repository.findByStatus(AccountStatus.ACTIVE);

        assertEquals(1, active.size());
    }

    @Test
    void save_UpdatesExistingUser() {
        repository.save(testUser);
        testUser.setName("Updated Name");
        repository.save(testUser);

        assertEquals(1, repository.count());
        assertEquals("Updated Name",
                repository.findById(testUser.getId()).get().getName());
    }
}
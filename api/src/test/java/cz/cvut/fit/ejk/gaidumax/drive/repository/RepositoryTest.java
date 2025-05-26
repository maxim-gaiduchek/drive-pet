package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class RepositoryTest {

    @Inject
    EntityManager entityManager;

    Repository<User, Long> repository;
    private User savedUser;

    @BeforeEach
    @Transactional
    void setUp() {
        repository = new Repository<>(User.class);
        repository.entityManager = entityManager;

        var user = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        savedUser = entityManager.merge(user);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        var userToDelete = entityManager.merge(savedUser);
        entityManager.remove(userToDelete);
    }

    @Test
    void testFindById_withPresentUser() {
        var result = repository.findById(savedUser.getId());
        assertTrue(result.isPresent());
        assertEquals(savedUser.getFirstName(), result.get().getFirstName());
        assertEquals(savedUser.getLastName(), result.get().getLastName());
        assertEquals(savedUser.getEmail(), result.get().getEmail());
        assertEquals(savedUser.getPassword(), result.get().getPassword());
        assertEquals(savedUser.getRole(), result.get().getRole());
    }

    @Test
    void testFindById_withEmptyUser() {
        var result = repository.findById(0L);
        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void testSave_entityCreation() {
        var user = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        var resultSaved = repository.save(user);
        assertNotNull(resultSaved.getId());
        assertEquals(user.getFirstName(), resultSaved.getFirstName());
        assertEquals(user.getLastName(), resultSaved.getLastName());
        assertEquals(user.getEmail(), resultSaved.getEmail());
        assertEquals(user.getPassword(), resultSaved.getPassword());
        assertEquals(user.getRole(), resultSaved.getRole());

        var resultFetched = entityManager.find(User.class, resultSaved.getId());
        assertEquals(user.getFirstName(), resultFetched.getFirstName());
        assertEquals(user.getLastName(), resultFetched.getLastName());
        assertEquals(user.getEmail(), resultFetched.getEmail());
        assertEquals(user.getPassword(), resultFetched.getPassword());
        assertEquals(user.getRole(), resultFetched.getRole());
    }

    @Test
    @Transactional
    void testSave_entityUpdate() {
        savedUser.setEmail("new_email@test.com");

        var resultSaved = repository.save(savedUser);
        assertEquals(savedUser.getId(), resultSaved.getId());
        assertEquals(savedUser.getFirstName(), resultSaved.getFirstName());
        assertEquals(savedUser.getLastName(), resultSaved.getLastName());
        assertEquals(savedUser.getEmail(), resultSaved.getEmail());
        assertEquals(savedUser.getPassword(), resultSaved.getPassword());
        assertEquals(savedUser.getRole(), resultSaved.getRole());

        var resultFetched = entityManager.find(User.class, savedUser.getId());
        assertEquals(savedUser.getId(), resultFetched.getId());
        assertEquals(savedUser.getFirstName(), resultFetched.getFirstName());
        assertEquals(savedUser.getLastName(), resultFetched.getLastName());
        assertEquals(savedUser.getEmail(), resultFetched.getEmail());
        assertEquals(savedUser.getPassword(), resultFetched.getPassword());
        assertEquals(savedUser.getRole(), resultFetched.getRole());
    }

    @Test
    @Transactional
    void testDelete() {
        var user = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        var savedUser = entityManager.merge(user);
        assertNotNull(savedUser.getId());

        repository.delete(savedUser);

        var resultFetched = entityManager.find(User.class, savedUser.getId());
        assertNull(resultFetched);
    }
}

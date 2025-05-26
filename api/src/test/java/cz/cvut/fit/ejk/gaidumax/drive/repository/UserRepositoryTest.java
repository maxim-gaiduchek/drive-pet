package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    @Transactional
    void setUp() {
        var user = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        savedUser = userRepository.save(user);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        userRepository.delete(savedUser);
    }

    @Test
    void testFindByEmail_withPresentUser() {
        var result = userRepository.findByEmail(savedUser.getEmail());
        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getId());
        assertEquals(savedUser.getFirstName(), result.get().getFirstName());
        assertEquals(savedUser.getLastName(), result.get().getLastName());
        assertEquals(savedUser.getEmail(), result.get().getEmail());
        assertEquals(savedUser.getPassword(), result.get().getPassword());
        assertEquals(savedUser.getRole(), result.get().getRole());
    }

    @Test
    void testFindByEmail_withEmptyUser() {
        var result = userRepository.findByEmail("another_email@test.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByEmail() {
        var resultExists = userRepository.existsByEmail(savedUser.getEmail());
        var resultNotExists = userRepository.existsByEmail("another_email@test.com");
        assertTrue(resultExists);
        assertFalse(resultNotExists);
    }
}

package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setRole(UserRole.CUSTOMER);
        testUser.setStatus(UserStatus.ACTIVE);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testFindByEmail_Success() {
        entityManager.persist(testUser);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isNotPresent();
    }

    @Test
    void testExistsByEmail_True() {
        entityManager.persist(testUser);
        entityManager.flush();

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail_False() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void testFindByFirstNameOrLastName() {
        entityManager.persist(testUser);
        entityManager.flush();

        List<User> users = userRepository.findByFirstNameContainingOrLastNameContaining("John", "John");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindByStatus() {
        entityManager.persist(testUser);

        User suspended = new User();
        suspended.setEmail("suspended@example.com");
        suspended.setPassword("pass");
        suspended.setFirstName("Suspended");
        suspended.setLastName("User");
        suspended.setRole(UserRole.CUSTOMER);
        suspended.setStatus(UserStatus.SUSPENDED);
        suspended.setCreatedAt(LocalDateTime.now());
        suspended.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(suspended);
        entityManager.flush();

        List<User> activeUsers = userRepository.findByStatus(UserStatus.ACTIVE);

        assertThat(activeUsers).hasSize(1);
        assertThat(activeUsers.get(0).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void testSaveUser() {
        User saved = userRepository.save(testUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testUpdateUser() {
        entityManager.persist(testUser);
        entityManager.flush();

        testUser.setFirstName("UpdatedJohn");
        User updated = userRepository.save(testUser);

        assertThat(updated.getFirstName()).isEqualTo("UpdatedJohn");
    }

    @Test
    void testDeleteUser() {
        User saved = entityManager.persist(testUser);
        entityManager.flush();

        userRepository.deleteById(saved.getId());

        Optional<User> deleted = userRepository.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }
}

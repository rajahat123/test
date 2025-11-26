package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByStatus(UserStatus status);
    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    boolean existsByEmail(String email);
}

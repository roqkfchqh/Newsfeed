package com.example.newsfeed.repository;

import com.example.newsfeed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeletedAtIsNull(Long userId);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    // Method to fetch users where deletedAt is null (valid users)
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.deletedAt IS NULL")
    Optional<User> findActiveUserById(@Param("userId") Long userId);

    boolean existsByEmail(String email);
}

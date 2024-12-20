package com.example.newsfeed.repository;

import com.example.newsfeed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.name FROM User u WHERE u.id = :userId")
    String findNameById(Long userId);

    Optional<User> findByEmail(String email);
}

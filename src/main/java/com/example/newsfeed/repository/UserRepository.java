package com.example.newsfeed.repository;

import com.example.newsfeed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    //deletedAt이 null 인 유저 (유효한 유저) 불러오는 메서드
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.deletedAt IS NULL")
    Optional<User> findActiveUserById(@Param("userId") Long userId);
}

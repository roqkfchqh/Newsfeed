package com.example.newsfeed.repository;

import com.example.newsfeed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

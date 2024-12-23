package com.example.newsfeed.repository;

import com.example.newsfeed.model.Friend;
import com.example.newsfeed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByFollower(User follower);
    List<Friend> findByFollowee(User followee);

    boolean existsByFollowerAndFollowee(User follower, User followee);
}

package com.example.newsfeed.repository;

import com.example.newsfeed.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f JOIN f.followee u WHERE u.deletedAt IS NULL AND f.followee.id = :userId AND f.follow = false")
    List<Friend> findByFollower(@Param("userId") Long userId);

    @Query("SELECT f FROM Friend f JOIN f.follower u WHERE u.deletedAt IS NULL AND f.follower.id = :userId AND f.follow = false")
    List<Friend> findByFollowee(@Param("userId") Long userId);

    @Query("""
    SELECT f FROM Friend f
    JOIN f.follower u1
    JOIN f.followee u2
    WHERE (f.follower.id = :userId OR f.followee.id = :userId)
    AND f.follow = true
    AND u1.deletedAt IS NULL
    AND u2.deletedAt IS NULL
    """)
    List<Friend> findFriendsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(f) > 0
    FROM Friend f
    WHERE f.follower.id = :followerId
    AND f.followee.id = :followeeId
    """)
    Boolean existsByFollowerIdAndFolloweeId(@Param("followeeId")Long friendId, @Param("followerId")Long userId);    //is deletedAt 빠져있음

    @Query("""
    SELECT f.follow
    FROM Friend f
    JOIN f.followee u
    WHERE f.id = :relationId
    AND u.deletedAt IS NULL
    """)
    Boolean findFollowById(@Param("relationId") Long relationId);   //is deletedAt 빠져있음

    @Query("""
    SELECT f.follow
    FROM Friend f
    JOIN f.follower u1
    JOIN f.followee u2
    WHERE f.follower.id = :followerId
    AND f.followee.id = :followeeId
    AND u1.deletedAt IS NULL
    AND u2.deletedAt IS NULL
    """)
    Boolean findFollowByFollowerIdAndFolloweeId(@Param("followeeId") Long friendId, @Param("followerId")Long userId);   //is deletedAt 빠져있음

}

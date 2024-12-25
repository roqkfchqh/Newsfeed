package com.example.newsfeed.repository;

import com.example.newsfeed.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    //모두 기본적으로 deletedAt = null 인 사용자만 조회함

    @Query("SELECT f FROM Friend f JOIN f.followee u WHERE f.followee.id = :userId AND f.follow = false AND u.deletedAt IS NULL")
    List<Friend> findByFollower(@Param("userId") Long userId);
    //팔로워 엔티티 조회 (나에게 요청을 보낸 상대)

    @Query("SELECT f FROM Friend f JOIN f.follower u WHERE f.follower.id = :userId AND f.follow = false AND u.deletedAt IS NULL")
    List<Friend> findByFollowee(@Param("userId") Long userId);
    //팔로이 엔티티 조회 (내가 요청을 보낸 상대)

    @Query("""
    SELECT f FROM Friend f JOIN f.follower u1 JOIN f.followee u2 WHERE f.follower.id = :userId AND f.follow = true AND u1.deletedAt IS NULL AND u2.deletedAt IS NULL
    """)
    List<Friend> findFriendsByUserId(@Param("userId") Long userId);
    //친구관계가 존재하며 true 인 엔티티 조회 (양방향 친구 관계)

    Boolean existsByFollowerIdAndFolloweeId(Long friendId, Long userId);
    //친구관계가 존재하는지 true or false 로 반환(존재여부)

    @Query("""
    SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Friend f JOIN f.followee u WHERE f.id = :relationId AND u.deletedAt IS NULL
    """)
    Boolean findFollowById(@Param("relationId") Long relationId);
    //관계 id 를 기반으로 friend 관계가 존재하는지

    @Query("""
    SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Friend f JOIN f.follower u1 JOIN f.followee u2 WHERE f.follower.id = :followerId AND f.followee.id = :followeeId AND u1.deletedAt IS NULL AND u2.deletedAt IS NULL
    """)
    Boolean findFollowByFollowerIdAndFolloweeId(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);
    //follower 와 followee id 를 기반으로 친구관계가 존재하는지
}

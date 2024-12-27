package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follwee_id")
    private User followee;

    @Builder.Default
    private Boolean follow = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void acceptFollow() {
        this.follow = true;
    }
}
/**
 * 친구인 상태 --------------------------------------------
 * 1. 상대가 나를 팔로우 했는데 내가 수락한 경우
 * follower 상대 ID
 * followee 나의 ID
 * follow true
 * --------------------------------------------
 * 2. 내가 상대를 팔로우 했는데 상대가 수락한 경우
 * follower 나의 ID
 * followee 상대 ID
 * follow true
 * --------------------------------------------
 * 친구가 아닌 상태 --------------------------------------------
 * 친구 요청 상태
 * 1. 상대가 나에게 친구 요청한 상태
 * follower 상대 ID
 * followee 나의 ID
 * follow false
 * --------------------------------------------
 * 2. 내가 상대에게 친구 요청한 상태
 * follower 나의 ID
 * followee 상대 ID
 * follow false
 */
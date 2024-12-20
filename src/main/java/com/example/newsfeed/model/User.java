package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Setter
    @Column(length = 100, nullable = false)
    private String password;

    @Setter
    @Column(length = 5, nullable = false)
    private String name;

    @Column
    private LocalDateTime deletedAt = null;

    // 유저가 팔로우
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower")
    private List<Friend> followerList;

    // 유저를 팔로우
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followee")
    private List<Friend> followeeList;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
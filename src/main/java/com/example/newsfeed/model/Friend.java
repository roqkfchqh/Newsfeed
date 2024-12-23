package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
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

    private Boolean follow = false;

    public void acceptFollow() {
        this.follow = true;
    }

    public static Friend create(User user, User follower) {
        Friend friend = new Friend();
        friend.follower = user;
        friend.followee = follower;
        friend.follow = false;
        return friend;
    }
}

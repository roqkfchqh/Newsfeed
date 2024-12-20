package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
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
}

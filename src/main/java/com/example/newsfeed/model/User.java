package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
    private LocalDateTime deletedAt;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void softDelete(){
        deletedAt = LocalDateTime.now();
    }
}
package com.example.newsfeed.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer like_cnt;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Post create(String title, String content, User user) {
        return new Post(
                title, content, user
        );
    }

    public void update(String title, String content){
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
    }

    public void likeCnt(){
        this.like_cnt ++;
    }

    public void dislikeCnt(){
        this.like_cnt ++;
    }

}

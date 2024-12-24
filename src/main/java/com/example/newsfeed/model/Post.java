package com.example.newsfeed.model;

import com.example.newsfeed.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        this.like_cnt --;
    }
}

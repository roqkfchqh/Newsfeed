package com.example.newsfeed.model;

import com.example.newsfeed.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private Integer like_cnt;

    @Builder
    public Comment(User user, Post post, String content, Integer likeCnt) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.like_cnt = likeCnt;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void increaseLikeCount() {
        if (this.like_cnt == null) {
            this.like_cnt = 0;
        }
        this.like_cnt++;
    }

    public void decreaseLikeCount() {
        if (this.like_cnt != null && this.like_cnt > 0) {
            this.like_cnt--;
        }
    }

}

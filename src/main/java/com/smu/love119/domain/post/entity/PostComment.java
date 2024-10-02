package com.smu.love119.domain.post.entity;

import com.smu.love119.domain.user.entity.User;
import com.smu.love119.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_comment")
public class PostComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment_content", length = 500, nullable = false)
    private String content;

    @Column(name = "comment_like", nullable = false)
    private int likeCount = 0;

    // 댓글 계층을 위한 필드, 기본값은 0 (일반 댓글)
    @Column(name = "class", nullable = false)
    private int commentClass = 0;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Builder
    public PostComment(
            Post post, User user,
            String content,
            int likeCount,
            int commentClass,
            LocalDateTime deletedDate) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.likeCount = likeCount;
        this.commentClass = commentClass;
        this.deletedDate = deletedDate;
    }

}

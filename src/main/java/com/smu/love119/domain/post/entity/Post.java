package com.smu.love119.domain.post.entity;

import com.smu.love119.domain.user.entity.User;
import com.smu.love119.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "post_title", length = 50, nullable = false)
    private String postTitle;

    @Column(name = "post_content", length = 1000, nullable = false)
    private String postContent;

    @Column(name = "post_view", nullable = false)
    private int viewCount = 0;

    @Column(name = "post_like", nullable = false)
    private int likeCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti", columnDefinition = "ENUM('ISTJ','ISFJ','INFJ','INTJ','ISTP','ISFP','INFP','INTP','ESTP','ESFP','ENFP','ENTP','ESTJ','ENTJ','ESFJ','ENFJ')")
    private MBTI mbti;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PostComment> postComments = new ArrayList<>();

    public enum MBTI {
        ISTJ, ISFJ, INFJ, INTJ, ISTP, ISFP, INFP, INTP,
        ESTP, ESFP, ENFP, ENTP, ESTJ, ENTJ, ESFJ, ENFJ
    }

    @Builder
    public Post(
            User user,
            String postTitle,
            String postContent,
            int viewCount,
            int likeCount,
            MBTI mbti,
            LocalDateTime deletedDate
    ) {
        this.user = user;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.mbti = mbti;
        this.deletedDate = deletedDate;
    }

}

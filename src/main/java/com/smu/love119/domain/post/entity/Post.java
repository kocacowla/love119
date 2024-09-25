package com.smu.love119.domain.post.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_title", length = 50, nullable = false)
    private String title;

    @Column(name = "post_content", length = 1000, nullable = false)
    private String content;

    @Column(name = "post_view", nullable = false)
    private int viewCount = 0;

    @Column(name = "post_like", nullable = false)
    private int likeCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti", columnDefinition = "ENUM('ISTJ','ISFJ','INFJ','INTJ','ISTP','ISFP','INFP','INTP','ESTP','ESFP','ENFP','ENTP','ESTJ','ENTJ','ESFJ','ENFJ')")
    private MBTI mbti;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    // 생성자, Getter 및 Setter
    public Post() {}

    // 추가 생성자나 메소드 필요시 여기에 작성

    public enum MBTI {
        ISTJ, ISFJ, INFJ, INTJ, ISTP, ISFP, INFP, INTP,
        ESTP, ESFP, ENFP, ENTP, ESTJ, ENTJ, ESFJ, ENFJ
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public MBTI getMbti() {
        return mbti;
    }

    public void setMbti(MBTI mbti) {
        this.mbti = mbti;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }
}

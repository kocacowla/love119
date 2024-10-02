package com.smu.love119.domain.user.entity;

import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.entity.PostComment;
import com.smu.love119.global.audit.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "my_mbti", nullable = true)
    private MBTI myMbti;

    @Enumerated(EnumType.STRING)
    @Column(name = "fav_mbti", nullable = true)
    private MBTI favMbti;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public boolean isDeleted() {
        return this.deletedDate != null;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role = RoleType.ROLE_USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;


    public enum MBTI {
        ISTJ, ISFJ, INFJ, INTJ, ISTP, ISFP, INFP, INTP, ESTP, ESFP, ENFP, ENTP, ESTJ, ESFJ, ENFJ, ENTJ
    }

    public enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }

    @Builder
    public User(
            Long id,
            String username,
            String nickname,
            String password,
            MBTI myMbti,
            MBTI favMbti,
            LocalDateTime deletedDate,
            RoleType role
    ) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.myMbti = myMbti;
        this.favMbti = favMbti;
        this.deletedDate = deletedDate;
        this.role = role != null ? role : RoleType.ROLE_USER;
    }


}
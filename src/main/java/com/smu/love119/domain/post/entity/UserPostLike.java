package com.smu.love119.domain.post.entity;


import com.smu.love119.domain.user.entity.User;
import com.smu.love119.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserPostLikeId.class) // 복합 키 사용
public class UserPostLike extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}

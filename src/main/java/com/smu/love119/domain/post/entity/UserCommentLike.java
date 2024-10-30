package com.smu.love119.domain.post.entity;

import com.smu.love119.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserCommentLikeId.class)  // 복합 키 사용
public class UserCommentLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment comment;
}

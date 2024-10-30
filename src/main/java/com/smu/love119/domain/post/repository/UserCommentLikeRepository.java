package com.smu.love119.domain.post.repository;

import com.smu.love119.domain.post.entity.UserCommentLike;
import com.smu.love119.domain.post.entity.UserCommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, UserCommentLikeId> {
    Optional<UserCommentLike> findByUserIdAndCommentId(Long userId, Long commentId);
}

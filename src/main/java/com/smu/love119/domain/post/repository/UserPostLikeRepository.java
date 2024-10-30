package com.smu.love119.domain.post.repository;


import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.entity.UserPostLike;
import com.smu.love119.domain.post.entity.UserPostLikeId;
import com.smu.love119.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT COUNT(upl) > 0 " +
            "FROM UserPostLike upl WHERE upl.user.id = :userId AND upl.post.id = :postId")
    boolean hasUserLikedPost(@Param("userId") Long userId, @Param("postId") Long postId);

    Optional<UserPostLike> findByUserAndPost(User user, Post post);

}
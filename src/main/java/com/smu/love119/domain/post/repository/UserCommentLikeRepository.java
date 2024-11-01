package com.smu.love119.domain.post.repository;

import com.smu.love119.domain.post.entity.PostComment;
import com.smu.love119.domain.post.entity.UserCommentLike;
import com.smu.love119.domain.post.entity.UserCommentLikeId;
import com.smu.love119.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, UserCommentLikeId> {

    // 특정 유저가 특정 댓글에 좋아요를 눌렀는지 확인
    boolean existsByUserAndComment(User user, PostComment comment);

    // 특정 유저와 댓글에 대한 좋아요 레코드 조회
    Optional<UserCommentLike> findByUserAndComment(User user, PostComment comment);

    // 특정 댓글에 대한 좋아요 삭제
    void deleteByUserAndComment(User user, PostComment comment);
}

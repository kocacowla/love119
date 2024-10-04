package com.smu.love119.domain.post.repository;

import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findAllByPost(Post post);
    // 특정 사용자가 작성한 모든 삭제되지 않은 댓글 조회
    List<PostComment> findByUserAndDeletedDateIsNull(User user);
}
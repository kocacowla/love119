package com.smu.love119.domain.post.repository;

import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 삭제되지 않은 모든 게시글 조회
    List<Post> findAllByDeletedDateIsNull();

    // 삭제되지 않은 특정 게시글 조회
    Optional<Post> findByIdAndDeletedDateIsNull(Long id);

    // 특정 사용자가 작성한 삭제되지 않은 게시글 조회
    List<Post> findByUserAndDeletedDateIsNull(User user);

}
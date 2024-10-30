package com.smu.love119.domain.post.repository;

import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    // 삭제되지 않은 게시글 페이징 조회
    Page<Post> findAllByDeletedDateIsNull(Pageable pageable);

    // 키워드 검색 (제목 또는 내용에 포함된 키워드) 페이징 조회
    @Query("SELECT p FROM Post p WHERE (p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword%) AND p.deletedDate IS NULL")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(upl) > 0 " +
            "FROM UserPostLike upl WHERE upl.user.id = :userId AND upl.post.id = :postId")
    boolean hasUserLikedPost(@Param("userId") Long userId, @Param("postId") Long postId);

}
package com.smu.love119.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smu.love119.domain.post.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 모든 게시글을 작성일 기준으로 내림차순으로 조회하는 메소드
    List<Post> findAllByOrderByCreatedDateDesc();
}

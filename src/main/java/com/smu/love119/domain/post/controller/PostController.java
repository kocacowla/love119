package com.smu.love119.domain.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.service.PostService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 목록을 조회하는 API
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
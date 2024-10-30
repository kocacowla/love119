package com.smu.love119.domain.post.entity;


import java.io.Serializable;
import java.util.Objects;

public class UserPostLikeId implements Serializable {
    private Long user;
    private Long post;

    public UserPostLikeId() {}

    public UserPostLikeId(Long user, Long post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPostLikeId that = (UserPostLikeId) o;
        return Objects.equals(user, that.user) && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }
}
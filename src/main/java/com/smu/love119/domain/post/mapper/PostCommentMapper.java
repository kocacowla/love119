package com.smu.love119.domain.post.mapper;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostCommentMapper {

    // PostComment을 PostCommentDto로 변환
    PostCommentDto toDTO(PostComment postComment);

    // PostCommentDto를 PostComment으로 변환
    PostComment toEntity(PostCommentDto postCommentDto);
}
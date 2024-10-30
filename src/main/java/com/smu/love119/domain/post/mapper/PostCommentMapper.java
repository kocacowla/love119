package com.smu.love119.domain.post.mapper;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostCommentMapper {

    @Mapping(source = "user.nickname", target = "nickname")  // 유저 닉네임 매핑
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.myMbti", target = "myMbti")      // 유저의 MBTI 매핑
    @Mapping(source = "createdDate", target = "createdDate") // 생성일자 매핑
    PostCommentDto toDTO(PostComment postComment);

    PostComment toEntity(PostCommentDto postCommentDto);
}

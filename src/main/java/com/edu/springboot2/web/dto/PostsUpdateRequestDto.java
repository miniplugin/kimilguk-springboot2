package com.edu.springboot2.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private Long fileId;

    @Builder
    public PostsUpdateRequestDto(String title, String content, Long fileId){
        this.title = title;
        this.content = content;
        this.fileId = fileId;
    }

}

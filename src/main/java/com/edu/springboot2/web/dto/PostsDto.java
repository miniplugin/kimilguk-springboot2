package com.edu.springboot2.web.dto;

import com.edu.springboot2.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class PostsDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long fileId;
    private LocalDateTime modifiedDate;

    //조회
    public PostsDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.fileId = entity.getFileId();
        this.modifiedDate = entity.getModifiedDate();
    }
    //저장,수정
    @Builder
    public PostsDto(String title, String content, String author, Long fileId){
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .fileId(fileId)
                .build();
    }

}

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
    //수정 및 임시저장
    @Builder
    public PostsDto(Long id, String title, String content, String author, Long fileId){
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.fileId = fileId;
    }
    //DB 신규 저장
    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .fileId(fileId)
                .build();
    }

    @Override
    public String toString() {
        return "PostsDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", fileId=" + fileId +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}

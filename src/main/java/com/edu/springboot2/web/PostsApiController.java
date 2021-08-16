package com.edu.springboot2.web;

import com.edu.springboot2.service.posts.PostsService;
import com.edu.springboot2.web.dto.PostsResponseDto;
import com.edu.springboot2.web.dto.PostsSaveRequestDto;
import com.edu.springboot2.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsService postsService; // 생성자로 주입

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        //System.out.println("여기까지");
        logger.info("디버그 " + requestDto.getFileId());
        return postsService.save(requestDto);
    }

    @PostMapping("/api/v1/posts/update/{id}")
    public Long posts_update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }

}

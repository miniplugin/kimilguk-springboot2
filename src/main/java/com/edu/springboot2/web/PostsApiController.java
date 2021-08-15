package com.edu.springboot2.web;

import com.edu.springboot2.service.posts.FileService;
import com.edu.springboot2.service.posts.PostsService;
import com.edu.springboot2.web.dto.FileDto;
import com.edu.springboot2.web.dto.PostsResponseDto;
import com.edu.springboot2.web.dto.PostsSaveRequestDto;
import com.edu.springboot2.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsService postsService; // 생성자로 주입
    private final FileService fileService;

    @PostMapping("/api/upload")
    public Long uploadFile(@RequestParam("file") MultipartFile uploadFile) {
        Long fileId = null;
        try {
            String origFilename = uploadFile.getOriginalFilename();
            UUID uid = UUID.randomUUID();//유니크ID값 생성
            String filename = uid.toString() + "." + StringUtils.getFilenameExtension(origFilename);
            String directory = "/tmp";
            String filePath = Paths.get(directory, filename).toString();
            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadFile.getBytes());
            stream.close();
            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);
            fileId = fileService.saveFile(fileDto);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fileId = null;
        }
        logger.info("디버그 " + fileId);
        return fileId;
    }

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

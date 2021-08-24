package com.edu.springboot2.util;

import com.edu.springboot2.domain.posts.Posts;
import com.edu.springboot2.domain.posts.PostsRepository;
import com.edu.springboot2.service.posts.FileService;
import com.edu.springboot2.service.posts.ManyFileService;
import com.edu.springboot2.web.dto.FileDto;
import com.edu.springboot2.web.dto.ManyFileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ManyFileUtilsApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ManyFileService fileService;
    private final PostsRepository postsRepository;

    @DeleteMapping("/api/many_file_delete/{fileId}")
    //public String file_delete(@PathVariable("fileId") Long fileId) { //Ajax 반환값이 text 일때
    public ResponseEntity<Map<String,Object>> file_delete(@PathVariable("fileId") Long fileId) { //jsp 반환값이 json 일때
        //String result = "";//Ajax 반환값이 text 일때
        Map<String, Object> jsonResult = new HashMap<>();//Ajax 반환값이 json 일때
        ResponseEntity<Map<String,Object>> result = null;//Ajax 반환값이 json 일때
        try {
            ManyFileDto fileDto = fileService.getFile(fileId);
            Path filePath = Paths.get(fileDto.getFilePath());
            File target = new File(filePath.toString());
            logger.info("디버그 타켓" + target);
            if(target.exists()) {
                target.delete();
                fileService.deleteFile(fileId);
                //result = "successOK";//리턴값이 String 이고, jsp 반환값이 text 일때
                jsonResult.put("success","OK");
            }
            result = new ResponseEntity<Map<String,Object>>(jsonResult, HttpStatus.OK);
        } catch (Exception e) {
            //result = "fail: " + e.toString();
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;//Ajax에서 바로확인 가능
    }

    @GetMapping("/api/many_file_download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws Exception {
        ManyFileDto fileDto = fileService.getFile(fileId);
        Path filePath = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));//한글파일명 깨지는 것 방지
        String encOrigFilename = URLEncoder.encode(fileDto.getOrigFilename());//ie에서 URL한글일때 에러발생방시 코드 추가
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encOrigFilename + "\"")
                .body(resource);
    }

    @PostMapping("/api/many_file_upload")
    public Long uploadFile(@RequestParam("many_file") MultipartFile uploadFile, @RequestParam("post_id") Long post_id) {
        Long fileId = null;
        try {
            String origFilename = uploadFile.getOriginalFilename();
            UUID uid = UUID.randomUUID();//유니크ID값 생성
            String filename = uid.toString() + "." + StringUtils.getFilenameExtension(origFilename);
            String uploadPath = "/tmp";
            String filePath = Paths.get(uploadPath, filename).toString();
            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadFile.getBytes());
            stream.close();
            Posts posts = postsRepository.findById(post_id).orElseThrow(() -> new
                    IllegalArgumentException("헤당 게시글이 없습니다. id="+post_id));
            ManyFileDto fileDto = new ManyFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);
            fileDto.setPosts(posts);
            fileId = fileService.saveFile(fileDto);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fileId = null;
        }
        logger.info("디버그 " + fileId);
        return fileId;
    }
}

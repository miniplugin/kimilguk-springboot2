package com.edu.springboot2.service.posts;

import com.edu.springboot2.domain.posts.ManyFile;
import com.edu.springboot2.domain.posts.ManyFileRepository;
import com.edu.springboot2.web.dto.ManyFileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManyFileService {
    private ManyFileRepository fileRepository;

    public ManyFileService(ManyFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void deleteFile(Long id) {
        fileRepository.findById(id).orElseThrow(()->new
                IllegalArgumentException("해당 파일이 없습니다. id="+ id));
        fileRepository.deleteById(id);
    }
    @Transactional
    public Long saveFile(ManyFileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public ManyFileDto getFile(Long file_id) {
        ManyFile file = fileRepository.findById(file_id).get();
        ManyFileDto fileDto = ManyFileDto.builder()
                .id(file_id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .posts(file.getPosts())
                .build();
        return fileDto;
    }

    @Transactional
    public ManyFileDto getManyFile(Long post_id) {
        ManyFile file = fileRepository.fileAllDesc(post_id);
        if(file == null) { return null; }
        ManyFileDto fileDto = ManyFileDto.builder()
                .id(file.getId())
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .posts(file.getPosts())
                .build();
        return fileDto;
    }
}
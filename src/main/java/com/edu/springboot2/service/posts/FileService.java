package com.edu.springboot2.service.posts;

import com.edu.springboot2.domain.posts.File;
import com.edu.springboot2.domain.posts.FileRepository;
import com.edu.springboot2.web.dto.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void deleteFile(Long id) {
        fileRepository.findById(id).orElseThrow(()->new
                IllegalArgumentException("해당 파일이 없습니다. id="+ id));
        fileRepository.deleteById(id);
    }
    @Transactional
    public Long saveFile(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id) {
        File file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}
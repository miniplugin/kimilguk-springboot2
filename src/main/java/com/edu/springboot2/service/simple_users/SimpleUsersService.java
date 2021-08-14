package com.edu.springboot2.service.simple_users;

import com.edu.springboot2.domain.simple_users.SimpleUsers;
import com.edu.springboot2.domain.simple_users.SimpleUsersRepository;
import com.edu.springboot2.web.dto.SimpleUsersDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SimpleUsersService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final SimpleUsersRepository simpleUsersRepository;

    @Transactional
    public Long save(SimpleUsersDto requestDto){
        return simpleUsersRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, SimpleUsersDto requestDto){
        SimpleUsers simpleUsers = simpleUsersRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 회원이 없습니다. id="+ id));
        simpleUsers.update(requestDto.getUsername(),requestDto.getPassword(),requestDto.getRole(),requestDto.getEnabled());
        return id;
    }

    @Transactional
    public SimpleUsersDto findById(Long id){
        SimpleUsers entity = simpleUsersRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("헤당 회원이 없습니다. id="+id));
        return new SimpleUsersDto(entity);
    }

    @Transactional
    public SimpleUsersDto findByName(String username){
        SimpleUsers entity = simpleUsersRepository.findByName(username);
        return new SimpleUsersDto(entity);
    }

    @Transactional(readOnly = true)
    public List<SimpleUsersDto> findAllDesc(){
        return simpleUsersRepository.findAllDesc().stream()
                .map(SimpleUsersDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete (Long id){
        SimpleUsers posts = simpleUsersRepository.findById(id).orElseThrow(()->new
                IllegalArgumentException("해당 회원이 없습니다. id="+ id));
        simpleUsersRepository.delete(posts);
    }

}

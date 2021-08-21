package com.edu.springboot2.service.posts;

import com.edu.springboot2.domain.posts.Posts;
import com.edu.springboot2.domain.posts.PostsRepository;
import com.edu.springboot2.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getFileId());

        return id;
    }

    @Transactional
    public PostsDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("헤당 게시글이 없습니다. id="+id));
        return new PostsDto(entity);
    }

    private static final int BLOCK_PAGE_NUM_COUNT = 4; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 5; // 한 페이지에 존재하는 게시글 수

    private PostsDto convertEntityToDto(Posts postsEntity) {
        return PostsDto.builder()
                .id(postsEntity.getId())
                .title(postsEntity.getTitle())
                .content(postsEntity.getContent())
                .author(postsEntity.getAuthor())
                .fileId(postsEntity.getFileId())
                .build();
    }

    @Transactional
    public List<PostsDto> getPostsList(String keyword, Integer pageNum) {
        Page<Posts> page = postsRepository.findByTitleContaining(keyword, PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id")));
        List<Posts> postsList = page.getContent();
        List<PostsDto> postsDtoList = new ArrayList<>();
        for (Posts posts : postsList) {
            postsDtoList.add(this.convertEntityToDto(posts));
        }
        return postsDtoList;
    }

    @Transactional
    public Long getBoardCount() {
        return postsRepository.count();
    }

    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        // 총 게시글 갯수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());
        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int) (Math.ceil((postsTotalCount / PAGE_POST_COUNT))) - 1;
        logger.info("전체 페이지" + postsTotalCount);
        logger.info("전송값 페이지" + PAGE_POST_COUNT);
        logger.info("전체 마지막 페이지" + totalLastPageNum);
        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        logger.info(totalLastPageNum + " 비교 " + (curPageNum));
        Integer blockLastPageNum = 0;
        //블럭 끝 번호 구하기
        if (totalLastPageNum >= curPageNum && totalLastPageNum > BLOCK_PAGE_NUM_COUNT) { //PAGE_POST_COUNT = 5
            blockLastPageNum = BLOCK_PAGE_NUM_COUNT - 1;
            logger.info("블럭 마지막 페이지1 " + blockLastPageNum);
        } else {
            blockLastPageNum = totalLastPageNum - 1;
            logger.info("블럭 마지막 페이지2 " + blockLastPageNum);
        }
        // 페이지 시작 번호 조정
        if (curPageNum == totalLastPageNum) { //제일 마지막 페이지 반복횟수 처리
            //blockLastPageNum = (int) (postsTotalCount - ((blockLastPageNum + 1) * PAGE_POST_COUNT)) - 1;
            blockLastPageNum = (int) (postsTotalCount - (curPageNum * PAGE_POST_COUNT)) - 1;
            curPageNum = 0;
            logger.info("여기!1");
        } else {
            curPageNum = 0;
        }
        // 페이지 번호 할당
        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
            logger.info("pageList[{}] = {} ", idx, pageList[idx]);//페이지 번호는 jsp 에서...
        }
        return pageList;
    }

    @Transactional
    public Page<Posts> search(String keyword, Pageable pageable) {
        Page<Posts> postsList = postsRepository.findByTitleContaining(keyword, pageable);
        return postsList;
    }
    /*
    //아래는 페이징 테스트 전용 실제는 위에서 검색과 같이 사용합니다.
    @Transactional
    public Page<Posts> getBoardList(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }
    //아래 레포지토리 쿼리메서드는 필요 없음. 위 내장된 메서드 사용 : PostsListResponseDto 도 필요 없음.
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
    */

    @Transactional
    public void delete (Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(()->new
                IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        postsRepository.delete(posts);
    }

}

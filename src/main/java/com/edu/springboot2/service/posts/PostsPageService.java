package com.edu.springboot2.service.posts;

import com.edu.springboot2.domain.posts.Posts;
import com.edu.springboot2.domain.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsPageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsRepository postsRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 4; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 5; // 한 페이지에 존재하는 게시글 수

    @Transactional
    public Page<Posts> getPostsList(String keyword, Integer pageNum) {
        Page<Posts> page = postsRepository.findByTitleContaining(keyword, PageRequest.of(pageNum, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id")));
        return page;
    }

    public Integer[] getPageList(Long postsTotalCount, Integer totalLastPageNum, Integer curPageNum) {
        //페이지 번호 배열 반환값 선언
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        logger.info(totalLastPageNum + " 비교 " + (curPageNum));
        //블럭 끝 번호 구하기
        Integer blockLastPageNum = BLOCK_PAGE_NUM_COUNT;
        logger.info("블럭 마지막 페이지1 " + blockLastPageNum);
        // 페이지 시작 번호 조정
        int startBlockNum = (int)Math.ceil((curPageNum) / blockLastPageNum);
        int startPageNum = startBlockNum * BLOCK_PAGE_NUM_COUNT;
        //int startPageNum = (int)Math.ceil((curPageNum-1) / totalLastPageNum)+1;
        logger.info("여기 startPageNum " + startPageNum);
        //제일 마지막 페이지 일때
        if (totalLastPageNum == (curPageNum + 1) ) {
            int blockLastPageNumTmp = (int) (postsTotalCount - (curPageNum * PAGE_POST_COUNT)) - 1;//제일 마지막 페이지 갯수 구하기
            if(blockLastPageNumTmp < BLOCK_PAGE_NUM_COUNT) {//마지막 개수가 BLOCK_PAGE_NUM_COUNT 개수보다 작을때
                blockLastPageNum = blockLastPageNumTmp + 1;
                logger.info("여기!1 " + blockLastPageNumTmp);
            }
        }
        //제일 마지막 페이지가 블럭번호 보다 작을때
        if (totalLastPageNum < BLOCK_PAGE_NUM_COUNT) {
            blockLastPageNum = totalLastPageNum;
        }
        // 페이지 번호 할당
        for (int val = startPageNum, idx = 0; idx < blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
            logger.info("pageList[{}] = {} ", idx, pageList[idx]);//페이지 번호는 jsp 에서...
        }
        return pageList;
    }

}

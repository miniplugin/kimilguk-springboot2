package com.edu.springboot2.web;

import com.edu.springboot2.domain.posts.Posts;
import com.edu.springboot2.domain.posts.PostsRepository;
import com.edu.springboot2.web.dto.PostsSaveRequestDto;
import com.edu.springboot2.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 이 클래스는 게시물 CRUD JUnit 테스트클래스입니다.
 * 주) 스프링시큐리티 설정파일(SecurityConfig.java)에서 permitAll()해야 정상 작동 됩니다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception{
        port = 8080;
        // given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://127.0.0.1:"+ port + "/api/v1/posts";
        System.out.println("디버그: " + url);
        //실제DB저장 하려면 시작 시작 단, SecurityConfig 에서 .antMatchers("/api/v1/**").permitAll() 적용필요
        /*
        //when1
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        */
        //실제DB저장 하려면 끝

        //when2-실제DB저장은 않되지만, 아래 메서드 실행에 영향을 줌 postsRepository.findAll();
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Posts> all = postsRepository.findAll();
        System.out.println("디버그: "+all.size());
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }
    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception{
        port = 8080;
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://127.0.0.1:"+ port + "/api/v1/posts/" + updateId;
        //String url = "http://127.0.0.1:"+ port + "/api/v1/posts/update/" + updateId;
        System.out.println("디버그: " + url);
        //실제DB저장 하려면 시작 단, SecurityConfig 에서 .antMatchers("/api/v1/**").permitAll() 적용필요
        /*
        //when1
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        restTemplate.put(url, requestDto, Long.class);
        //then
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(responseEntity.getBody()).isGreaterThan(0L);
        */
        //실제DB저장 하려면 끝

        //when2-실제DB저장은 않되지만, 아래 메서드 실행에 영향을 줌 postsRepository.findAll();
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Posts> all = postsRepository.findAll();
        System.out.println(all.get(0).getTitle() + "디버그: "+all.size());
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

}

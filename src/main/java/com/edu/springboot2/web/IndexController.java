package com.edu.springboot2.web;

import com.edu.springboot2.config.auth.LoginUser;
import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.domain.posts.Posts;
import com.edu.springboot2.domain.simple_users.SimpleUsers;
import com.edu.springboot2.domain.simple_users.SimpleUsersRepository;
import com.edu.springboot2.service.posts.FileService;
import com.edu.springboot2.service.posts.PostsService;
import com.edu.springboot2.service.simple_users.SimpleUsersService;
import com.edu.springboot2.util.ScriptUtils;
import com.edu.springboot2.web.dto.FileDto;
import com.edu.springboot2.web.dto.PostsDto;
import com.edu.springboot2.web.dto.SimpleUsersDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PostsService postsService;
    private final SimpleUsersService simpleUsersService;
    private final SimpleUsersRepository simpleUsersRepository;
    private final FileService fileService;

    @PostMapping("/mypage/signout")
    public String simpleUsersDeletePost(HttpServletResponse response,SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersService.delete(simpleUsersDto.getId());
        ScriptUtils.alertAndMovePage(response, "회원 탈퇴 되었습니다.", "/logout");
        //return "redirect:/simple_users/list";
        return null;
    }
    @PostMapping("/mypage/mypage")
    public String simpleUsersUpdatePost(HttpServletResponse response, SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersDto.setRole("USER");//해킹 위험 때문에 강제로 추가
        simpleUsersService.update(simpleUsersDto.getId(), simpleUsersDto);
        ScriptUtils.alertAndMovePage(response, "수정 되었습니다.", "/mypage/mypage/" + simpleUsersDto.getId());
        //return "redirect:/simple_users/update/" + simpleUsersDto.getId();
        return null;
    }
    @GetMapping("/mypage/mypage/{id}")
    public String simpleUsersUpdate(HttpServletResponse response,@PathVariable Long id, Model model, @LoginUser SessionUser user){
        model.addAttribute("simple_user", simpleUsersService.findById(id));
        return "mypage/mypage";
    }
    @PostMapping("/signup")
    public String signUpPost(HttpServletResponse response, SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersDto.setRole("USER");//해킹 위험 때문에 강제로 추가
        //SimpleUsersDto simpleUsers = simpleUsersService.findByName(user.getName());//서비스 빌더에 ID값이 생성전이라서 Setter에서 에러남.
        SimpleUsers simpleUsers = simpleUsersRepository.findByName(simpleUsersDto.getUsername());
        if(simpleUsers == null) {
            simpleUsersService.save(simpleUsersDto);
            ScriptUtils.alertAndMovePage(response, "회원가입 되었습니다. 로그인해 주세요", "/");
        }else{
            ScriptUtils.alertAndBackPage(response, "중복 아이디가 존재 합니다 아이디를 다시 입력해 주세요.");
        }

        //return "redirect:/simple_users/list";
        return null;
    }
    @GetMapping("/signup")
    public String signupGet(){
        return "signup";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String message, Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("sessionUserName", user.getName());
        }
        //if(!message.isEmpty()) {
            model.addAttribute("message", message);
        //}
        return "login";
    }

    @GetMapping("/")
    public String index(@ModelAttribute(value="page")String page, HttpServletRequest request, String search_type, @RequestParam(value="keyword", defaultValue = "")String keyword, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model, @LoginUser SessionUser user){ //, Principal principal, HttpSession httpSession
        //model.addAttribute("posts", postsService.findAllDesc());//페이지 사용않할 때
        //if(keyword == null) { keyword = ""; }//@RequestParam defaultValue 처리
        String sessionKeyword = (String) request.getSession().getAttribute("sessionKeyword");
        if(keyword.isEmpty() && sessionKeyword == null) {
            sessionKeyword = "";
        }
        if(!keyword.isEmpty()) {
            request.getSession().setAttribute("sessionKeyword", keyword);
            sessionKeyword = (String) request.getSession().getAttribute("sessionKeyword");
        }
        if(keyword.isEmpty() && search_type != null && sessionKeyword != null) {
            request.getSession().removeAttribute("sessionKeyword");
            sessionKeyword = "";
        }
        model.addAttribute("sessionKeyword", sessionKeyword);
        Page<Posts> searchList = postsService.search(sessionKeyword, pageable);
        model.addAttribute("posts", searchList);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("prevCheck", searchList.hasPrevious());
        model.addAttribute("nextCheck", searchList.hasNext());
        /*
        ArrayList pageIndex = new ArrayList();
        for(int i=0; i<pageable.getPageSize()-1; i++) {
            pageIndex.add(i);
        }
        */
        model.addAttribute("pageIndex", pageable.getPageSize());

        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
            model.addAttribute("sessionUserName", user.getName());
            SimpleUsersDto simpleUsers = simpleUsersService.findByName(user.getName());
            //SimpleUsers simpleUsers = simpleUsersRepository.findByName(user.getName());
            model.addAttribute("sessionUserId", simpleUsers.getId());
            model.addAttribute("sessionRoleAdmin", ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
        }
        return "posts/posts-list";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user){
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
        return "posts/posts-save";
    }

    @GetMapping("/posts/read/{id}")
    public String postsRead(@PathVariable Long id, Model model){

        PostsDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        if(dto.getFileId() != null) {
            FileDto fileDto = fileService.getFile(dto.getFileId());
            model.addAttribute("OrigFilename", fileDto.getOrigFilename());
        }
        return "posts/posts-read";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user){

        PostsDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        if(dto.getFileId() != null) {
            FileDto fileDto = fileService.getFile(dto.getFileId());
            model.addAttribute("OrigFilename", fileDto.getOrigFilename());
        }
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
            model.addAttribute("sessionRoleAdmin", ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
        }
        return "posts/posts-update";
    }
}

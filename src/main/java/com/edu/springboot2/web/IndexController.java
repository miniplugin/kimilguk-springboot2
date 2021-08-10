package com.edu.springboot2.web;

import com.edu.springboot2.config.auth.LoginUser;
import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.domain.user.Role;
import com.edu.springboot2.domain.user.User;
import com.edu.springboot2.service.posts.PostsService;
import com.edu.springboot2.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PostsService postsService;

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
    public String index(Model model, @LoginUser SessionUser user){ //, Principal principal, HttpSession httpSession
        /* //LoginUserArgumentResolover.java 로 옮김
        if(user == null && principal != null) { //일반 로그인 일때 세션 저장
            String userName = principal.getName();
            Collection<GrantedAuthority> roles = ((UsernamePasswordAuthenticationToken) principal).getAuthorities();
            System.out.println("디버그 "+roles);
            //User user_local = new User(userName,"","",Role.USER);//Serializable 에러
            Role userAuthor = null;
            if(roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                userAuthor = Role.ADMIN;
            }else if(roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                userAuthor = Role.USER;
            }else{
                userAuthor = Role.GUEST;
            }
            User user_local = User.builder()
                    .name(userName)
                    .email("")
                    .picture("")
                    .role(userAuthor)
                    .build();
            httpSession.setAttribute("user", new SessionUser(user_local));
            SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
            System.out.println("사용자권한" + userAuthor + " 세션사용자명 " + sessionUser.getName() + " 로그인사용자명 "+ user_local.getName());
            model.addAttribute("sessionUserName", sessionUser.getName());
        }
        */
        model.addAttribute("posts", postsService.findAllDesc());

        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user){
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        return "posts-update";
    }
}

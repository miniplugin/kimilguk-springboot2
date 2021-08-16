package com.edu.springboot2.web;

import com.edu.springboot2.config.auth.LoginUser;
import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.domain.simple_users.SimpleUsers;
import com.edu.springboot2.domain.simple_users.SimpleUsersRepository;
import com.edu.springboot2.service.posts.FileService;
import com.edu.springboot2.service.posts.PostsService;
import com.edu.springboot2.service.simple_users.SimpleUsersService;
import com.edu.springboot2.util.ScriptUtils;
import com.edu.springboot2.web.dto.FileDto;
import com.edu.springboot2.web.dto.PostsResponseDto;
import com.edu.springboot2.web.dto.SimpleUsersDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;

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
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
            model.addAttribute("sessionUserName", user.getName());
            SimpleUsersDto simpleUsers = simpleUsersService.findByName(user.getName());
            //SimpleUsers simpleUsers = simpleUsersRepository.findByName(user.getName());
            model.addAttribute("sessionUserId", simpleUsers.getId());
            model.addAttribute("sessionRoleAdmin", ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
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

    @GetMapping("/posts/read/{id}")
    public String postsRead(@PathVariable Long id, Model model){

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        if(dto.getFileId() != null) {
            FileDto fileDto = fileService.getFile(dto.getFileId());
            model.addAttribute("OrigFilename", fileDto.getOrigFilename());
        }
        return "posts-read";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user){

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        if(dto.getFileId() != null) {
            FileDto fileDto = fileService.getFile(dto.getFileId());
            model.addAttribute("OrigFilename", fileDto.getOrigFilename());
        }
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
            model.addAttribute("sessionRoleAdmin", ("ROLE_ADMIN".equals(user.getRole())?"admin":null));
        }
        return "posts-update";
    }
}

package com.edu.springboot2.web;

import com.edu.springboot2.config.auth.LoginUser;
import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.service.simple_users.SimpleUsersService;
import com.edu.springboot2.util.ScriptUtils;
import com.edu.springboot2.web.dto.SimpleUsersDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class SimpleUsersController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //private final UsersRepository usersRepository;
    private final SimpleUsersService simpleUsersService;

    @GetMapping("/simple_users/list")
    public String simpleUsersList(Model model, @LoginUser SessionUser user){
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
/*

        List<SimpleUsersListDto> simpleUsers = null;
        simpleUsers = usersRepository.findAllDesc().stream()
                    .map(SimpleUsersListDto::new)
                    .collect(Collectors.toList());
        logger.info("디버그3");
*/

        //model.addAttribute("simpleUsers", simpleUsers);
        model.addAttribute("simpleUsers", simpleUsersService.findAllDesc());
        return "simple_users/list";
    }
    @PostMapping("/simple_users/delete")
    public String simpleUsersDeletePost(HttpServletResponse response,SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersService.delete(simpleUsersDto.getId());
        ScriptUtils.alertAndMovePage(response, "삭제 되었습니다.", "/simple_users/list");
        //return "redirect:/simple_users/list";
        return null;
    }
    @PostMapping("/simple_users/update")
    public String simpleUsersUpdatePost(HttpServletResponse response, SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersService.update(simpleUsersDto.getId(), simpleUsersDto);
        ScriptUtils.alertAndMovePage(response, "수정 되었습니다.", "/simple_users/update/" + simpleUsersDto.getId());
        //return "redirect:/simple_users/update/" + simpleUsersDto.getId();
        return null;
    }
    @GetMapping("/simple_users/update/{id}")
    public String simpleUsersUpdate(HttpServletResponse response,@PathVariable Long id, Model model, @LoginUser SessionUser user){
        model.addAttribute("simple_user", simpleUsersService.findById(id));
        return "simple_users/update";
    }
    @PostMapping("/simple_users/save")
    public String simpleUsersSavePost(HttpServletResponse response,SimpleUsersDto simpleUsersDto, Model model, @LoginUser SessionUser user) throws Exception {
        logger.info("디버그 :" + simpleUsersDto.toString());
        simpleUsersService.save(simpleUsersDto);
        ScriptUtils.alertAndMovePage(response, "저장 되었습니다.", "/simple_users/list");
        //return "redirect:/simple_users/list";
        return null;
    }
    @GetMapping("/simple_users/save")
    public String simpleUsersSave(Model model, @LoginUser SessionUser user){
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
        return "simple_users/save";
    }
}

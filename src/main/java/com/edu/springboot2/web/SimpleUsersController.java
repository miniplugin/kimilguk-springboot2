package com.edu.springboot2.web;

import com.edu.springboot2.config.auth.LoginUser;
import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.service.simple_users.SimpleUsersService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/simple_users/save")
    public String simpleUsersSave(Model model, @LoginUser SessionUser user){
        if(user != null){
            logger.info("네이버 API 로그인사용자명 또는 세션 발생 후 사용자명 " + user.getName());
            model.addAttribute("sessionUserName", user.getName());
        }
        return "simple_users/save";
    }
}

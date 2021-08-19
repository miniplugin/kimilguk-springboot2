package com.edu.springboot2.config.auth;

import com.edu.springboot2.config.auth.dto.SessionUser;
import com.edu.springboot2.domain.user.Role;
import com.edu.springboot2.domain.user.Users;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        logger.info("여기까지1");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if(httpSession.getAttribute("user") == null && !"anonymousUser".equals(userName)) { //일반 로그인 일때 세션 저장
            Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
            logger.info("디버그1 "+roles);
            //User user_local = new User(userName,"","",Role.USER);//Serializable 에러
            Role userAuthor = null;
            if(roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                userAuthor = Role.ADMIN;
            }else if(roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                userAuthor = Role.USER;
            }else{
                userAuthor = Role.GUEST;
            }
            Users user_local = Users.builder()
                    .name(userName)
                    .email("")
                    .picture("")
                    .role(userAuthor)
                    .build();
            httpSession.setAttribute("user", new SessionUser(user_local));
            SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
            /*
            if(sessionUser.getName().equals("anonymousUser")) {
                httpSession.invalidate();
            }
            //시큐리티 Config 파일에 logout().invalidateHttpSession(true) 추가로 대체
             */
            logger.info("사용자권한1 " + userAuthor + " 세션사용자명1 " + sessionUser.getName() + " 로그인사용자명1 "+ user_local.getName());
        }
        return httpSession.getAttribute("user");
    }
}

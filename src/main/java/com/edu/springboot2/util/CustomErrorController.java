package com.edu.springboot2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class CustomErrorController implements ErrorController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 에러 페이지 정의
    private final String ERROR_ETC_PAGE_PATH = "/error/error";

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model) {
        // 에러 코드를 획득한다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // 에러 코드에 대한 상태 정보
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        if (status != null) {
            // HttpStatus와 비교해 페이지 분기를 나누기 위한 변수
            int statusCode = Integer.valueOf(status.toString());
            //이전페이지로 돌아가기용 데이터 생성
            String referer = request.getHeader("Referer");//크롬>네트워크>파일>Referer>이전페이지 URL이 존재
            // 에러 페이지에 표시할 정보
            model.addAttribute("status", status.toString());
            model.addAttribute("message", httpStatus.getReasonPhrase());
            model.addAttribute("timestamp", new Date());
            model.addAttribute("prevPage", referer);
        }
        // 정의한 에러 외 모든 에러는 error/error 페이지로 보낸다.
        return ERROR_ETC_PAGE_PATH;
    }

    /**
     * 이 메서드는 스프링 부트 2.3.x부터 deprecated 됨
     * - 이 메서드 대신 custom path를 지정하려면 server.error.path 속성으로 지정해야 한다
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}

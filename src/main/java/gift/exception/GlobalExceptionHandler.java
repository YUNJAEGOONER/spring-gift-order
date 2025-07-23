package gift.exception;

import gift.exception.page.PageIndexException;
import gift.jwt.exception.JWTAuthException;
import gift.jwt.exception.LoginError;
import gift.member.exception.MemberNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //애플리케이션 전역에서 발생하는 예외를 처리하기 위함
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JWTAuthException.class)
    public String satustUnauthorizedHandler(JWTAuthException e, HttpServletResponse response) {
        log.info(e.getErrorCode().getMessage());
        Cookie cookie = new Cookie("toke", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/view/loginform";
    }

    @ExceptionHandler(LoginError.class)
    public String LoginErrorHandler(LoginError e) {
        return "redirect:/view/loginform";
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public String satustUnauthorizedHandler(MemberNotFoundException e, HttpServletResponse response) {
        log.info(e.getErrorCode().getMessage());
        return "redirect:/view/loginform";
    }

    @ExceptionHandler(PageIndexException.class)
    public String satustUnauthorizedHandler(PageIndexException e, HttpServletResponse response) {
        log.info(e.getErrorCode().getMessage());
        return "redirect:/view/products/list";
    }


}
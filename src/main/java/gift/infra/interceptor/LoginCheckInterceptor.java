package gift.infra.interceptor;

import gift.jwt.JwtAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
//로그인이 되어 있는지를 확인하기 위한 인터셉터
public class LoginCheckInterceptor implements HandlerInterceptor{

    private static final Logger log = LoggerFactory.getLogger(LoginCheckInterceptor.class);
    private final JwtAuthService jwtAuthService;

    public LoginCheckInterceptor(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    //컨트롤러에 호출 전에 동작
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //컨트롤러가 호출되기 전에 실행됨
        log.info("[LoginChecker] preHandle");
        JwtChecker jwtChecker = (token, adminChecker) -> true;
        String token = jwtChecker.checkJwtAvailable(request);
        jwtAuthService.checkValidation(token);
        return jwtChecker.checkRole(token, jwtAuthService);
    }

}
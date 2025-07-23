package gift.infra.interceptor;

import gift.exception.ErrorCode;
import gift.jwt.JwtAuthService;
import gift.jwt.exception.LoginError;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface JwtChecker {

    Logger log = LoggerFactory.getLogger(JwtChecker.class);

    boolean checkRole(String token, JwtAuthService jwtAuthService);

    default String checkJwtAvailable(HttpServletRequest request) {

        log.info("[FunctionalInterface.checkJwtAvaliable]");
        if(request.getCookies() == null){
            log.warn("쿠키가 존재하지 않음");
            throw new LoginError(ErrorCode.LOGIN_REQUIRED_FAIL);
        }

        for(Cookie c : request.getCookies()){
            if(c.getName().equals("token")){
                return c.getValue();
            }
        }

        log.warn("유효한 토큰이 존재하지 않습니다...");
        throw new LoginError(ErrorCode.LOGIN_REQUIRED_FAIL);
    }
}

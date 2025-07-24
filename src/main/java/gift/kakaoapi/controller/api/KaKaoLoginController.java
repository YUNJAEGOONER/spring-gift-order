package gift.kakaoapi.controller.api;

import gift.kakaoapi.service.KakaoLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KaKaoLoginController {

    private static final Logger log = LoggerFactory.getLogger(KaKaoLoginController.class);
    private final KakaoLoginService kakaoLoginService;

    public KaKaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    //리다이렉트 URI: 카카오 로그인의 인가 코드를 전달하기 위한 리다이렉트 URI를 등록합니다.
    @GetMapping("/kakaoLogin")
    public void getToken(
            @RequestParam("code") String authorizationCode,
            HttpServletResponse httpServletResponse
    ){
        log.info("[step1:인가 코드] 카카오 로그인 : " + authorizationCode);
        String token = kakaoLoginService.getAccessToken(authorizationCode);

        //엑세스 토큰을 쿠키로 반환
        Cookie tcookie = new Cookie("token", token);
        tcookie.setPath("/");
        httpServletResponse.addCookie(tcookie);
    }

}

package gift.kakaoapi.controller;

import gift.jwt.JwtAuthService;
import gift.kakaoapi.dto.UserInfo;
import gift.kakaoapi.service.KakaoApiService;
import gift.kakaoapi.tokenmanager.KakaoTokenService;
import gift.member.dto.MemberRequestDto;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginViewController {

    private static final Logger log = LoggerFactory.getLogger(KakaoLoginViewController.class);

    private final KakaoApiService kakaoApiService;
    private final MemberService memberService;
    private final JwtAuthService jwtAuthService;
    private final KakaoTokenService kakaoTokenService;

    public KakaoLoginViewController(
            KakaoApiService kakaoApiService,
            MemberService memberService,
            JwtAuthService jwtAuthService, KakaoTokenService kakaoTokenService
    ) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
        this.kakaoTokenService = kakaoTokenService;
    }

    //로그인 화면 불러오기
    @GetMapping("/view/loginform")
    public String kakaoLoginForm(Model model){
        String login_url = kakaoApiService.getLoginLink();
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        model.addAttribute("login_url", login_url);
        return "yjshop/user/login";
    }

    //필터예외처리 -> forward (error 메시지를 request에 setAttribute)
    @PostMapping("/view/login/error")
    public String loginError(HttpServletRequest request, Model model){
        String login_url = kakaoApiService.getLoginLink();
        model.addAttribute("errormsg", request.getAttribute("errormsg"));
        model.addAttribute("login_url", login_url);
        return "yjshop/user/loginerror";
    }

    // 리다이렉트 URI: 카카오 로그인의 인가 코드를 전달하기 위한 리다이렉트 URI를 등록합니다.
    @GetMapping("/kakao-login")
    public String kakaoLogin(
            @RequestParam("code") String authorizationCode,
            HttpServletResponse httpServletResponse
    ){
        log.info("[step1:인가 코드] 카카오 로그인 =  " + authorizationCode);

        String accessToken = kakaoApiService.getAccessToken(authorizationCode).accessToken();

        //accessToken을 기반으로 사용자 정보 가져오기
        UserInfo userInfo = kakaoApiService.getUserInfo(accessToken);

        String email = userInfo.getKakaoAccount().getEmail();
        String tempPW = userInfo.getId() + LocalDate.now();

        //해당 이메일을 통해 가입이 되어있다면, 회원 정보를 바탕으로 JWT를 발급
        //해당 이메일을 통해 가입이 되어있지 않다면, 계정을 생성하고 JWT를 발급
        Member member = memberService.getMemberByEmail(email)
                .orElseGet(() -> memberService.register(new MemberRequestDto(email, tempPW)));
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId(), member.getRole());

        //DB에서 카카오 api 엑세스 토큰을 저장(관리)
        kakaoTokenService.saveUserToken(member.getMemberId(), accessToken);

        //로그인 정보를 바탕으로 JWT를 쿠키를 통해 전달
        Cookie tcookie = new Cookie("token", token);
        tcookie.setPath("/");
        tcookie.setHttpOnly(true);
        httpServletResponse.addCookie(tcookie);

        return "redirect:/view/products/list";
    }

}

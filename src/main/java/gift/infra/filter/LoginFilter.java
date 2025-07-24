package gift.infra.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.jwt.JwtAuthService;
import gift.jwt.exception.LoginError;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
    private final MemberService memberService;
    private final JwtAuthService jwtAuthService;

    public LoginFilter(MemberService memberService, JwtAuthService jwtAuthService, ObjectMapper objectMapper){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("필터 등록 완료,,,");
    }

    //로그인 요청에 대해서만 동작하는 필터임...
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();
        String Method = httpServletRequest.getMethod();

        //view로 로그인 요청이 들어오는 경우
        if(url.equals("/view/login") && Method.equals("POST")){

            log.info("로그인 필터 is working,,,,");

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            //인증실패예외를 반환하고 이를 Http Response로 렌더링하는 작업이 필요할것 같아요.
            if(email.isBlank()|| password.isBlank()){
                throw new LoginError(ErrorCode.EMAIL_PASSWORD_REQUIRED);
            }

            Member member = memberService.checkMember(email, password);
            String token = jwtAuthService.createJwt(email, member.getMemberId(), member.getRole());

            //쿠키 발행 (쿠키에 토큰을 저장)
            Cookie tcookie = new Cookie("token", token);
            tcookie.setPath("/");
            httpServletResponse.addCookie(tcookie);

            request.setAttribute("token", token);
            request.setAttribute("role", member.getRole());
            log.info("토큰 생성 완료");
        }

        //다음 필터가 있다면 동작해라;
        chain.doFilter(request, response);
    }
}
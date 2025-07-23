package gift.member.controller.api;

import gift.exception.MyException;
import gift.jwt.JwtAuthService;
import gift.jwt.JwtResponseDto;
import gift.member.dto.MemberRequestDto;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final JwtAuthService jwtAuthService;

    public MemberController(MemberService memberService, JwtAuthService jwtAuthService){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
    }

    //회원가입 기능 -> 토큰을 반환
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            HttpServletResponse response
    ){
        Member member = memberService.register(memberRequestDto);
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId(), member.getRole());
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.CREATED);
    }

    //로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody @Valid MemberRequestDto memberRequestDto
    ){
        Member member = memberService.checkMember(memberRequestDto.email(), memberRequestDto.password());
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId(), member.getRole());
        return ResponseEntity.ok().body(new JwtResponseDto(token));
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> MemberControllerExceptionHandler(MyException e){
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(e.getErrorCode().getMessage());
    }

}
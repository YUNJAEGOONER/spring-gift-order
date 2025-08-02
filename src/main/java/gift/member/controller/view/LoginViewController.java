package gift.member.controller.view;

import gift.member.dto.MemberRequestDto;
import gift.member.entity.Role;
import gift.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view")
@Controller
public class LoginViewController {

    private final MemberService memberService;

    public LoginViewController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping
    public String home(){
        return "redirect:/view/products/list";
    }

    //회원 가입 화면을 보여주는 메서드
    @GetMapping("/registerform")
    public String registerForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        return "yjshop/user/register";
    }

    //회원가입 기능 -> 토큰을 반환 (쿠키에 저장)
    @PostMapping("/register")
    public String register(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response
    ){
        if(memberService.getMemberByEmail(memberRequestDto.email()).isPresent()){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "이미 사용중인 이메일 입니다."));
        }

        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "yjshop/user/register";
        }
        memberService.register(memberRequestDto);
        return "redirect:/view/loginform";
    }

    //로그인 기능 -> 토큰을 반환(쿠키에 저장)
    @PostMapping("/login")
    public String login(HttpServletRequest request) {
        Role role = (Role) request.getAttribute("role");
        //관리자 로그인 시, -> 관리자 페이지로 이동
        if(role.equals(Role.ADMIN)){
            return "redirect:/view/admin/products";
        }
        return "redirect:/view/products/list";
    }

    //로그아웃 기능 -> 토큰을 만료시킴
    @GetMapping("/my/logout")
    public String logout(HttpServletResponse response){
        Cookie logoutcookie = new Cookie("token", null);
        logoutcookie.setPath("/");
        logoutcookie.setMaxAge(0); //즉시 만료되는 토큰을 발행
        response.addCookie(logoutcookie);
        return "redirect:/view/loginform";
    }

}
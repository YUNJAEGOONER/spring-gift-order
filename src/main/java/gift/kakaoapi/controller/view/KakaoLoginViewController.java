package gift.kakaoapi.controller.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class KakaoLoginViewController {

    @Value("${kakao.client_id}")
    private String REST_API_KEY;

    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URI;

    @GetMapping("/login")
    public String Login(Model model){
        String login_url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
                + "&redirect_uri="
                + REDIRECT_URI
                + "&client_id="
                + REST_API_KEY;

        model.addAttribute("login_url", login_url);
        return "yjshop/kakaoLogin";
    }

}

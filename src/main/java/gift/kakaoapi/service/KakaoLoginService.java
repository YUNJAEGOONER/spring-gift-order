package gift.kakaoapi.service;

import gift.kakaoapi.dto.TokenResponseDto;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoLoginService {

    private static final Logger log = LoggerFactory.getLogger(KakaoLoginService.class);
    @Value("${kakao.client_id}")
    private String REST_API_KEY;

    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URI;

    private final RestTemplate restTemplate;

    public KakaoLoginService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    public String getAccessToken(String authorizationCode){

        log.info("[step2:토큰 받기]: 회원 확인 및 등록 ");
        String url = "https://kauth.kakao.com/oauth/token";

        //요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //요청 바디
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", authorizationCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        TokenResponseDto token = restTemplate.postForEntity(url, request, TokenResponseDto.class).getBody();

        return token.accessToken();
    }


}

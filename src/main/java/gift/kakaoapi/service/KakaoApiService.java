package gift.kakaoapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.kakaoapi.dto.TokenResponseDto;
import gift.kakaoapi.dto.UserInfo;
import gift.kakaoapi.exception.KakaoApiException;
import gift.kakaoapi.tokenmanager.KakaoTokenRepository;
import gift.order.dto.MessageDto;
import java.net.URI;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {

    private static final Logger log = LoggerFactory.getLogger(KakaoApiService.class);
    private final static String MESSAGE_TEMPLATE_ID = "122830";

    @Value("${kakao.client-id}")
    private String restApiKey;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    //스프링 MVC가 제공해주는 외부와의 HTTP 통신 도구
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoApiService(ObjectMapper objectMapper, KakaoTokenRepository kakaoTokenRepository, RestTemplateBuilder restTemplateBuilder) {
        this.objectMapper = objectMapper;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(5L))
                .readTimeout(Duration.ofSeconds(5L))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    //로그인 화면 링크 리턴해주는 메서드
    public String getLoginLink(){
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code";
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("client_id", restApiKey);
        return builder.build().toUriString();
    }

    public TokenResponseDto getAccessToken(String authorizationCode) {
        log.info("[step2:토큰 받기]: 회원 확인 및 등록 ");
        String url = "https://kauth.kakao.com/oauth/token";

        //요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //요청 바디
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        return restTemplate.postForEntity(url, request, TokenResponseDto.class).getBody();
    }


    public UserInfo getUserInfo(String accessToken) {
        log.info("[step3:사용자 로그인 처리]: 사용자 정보 가져오기");
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        return restTemplate.postForEntity(url, request, UserInfo.class).getBody();
    }

    @Async
    public void sendMessageToCustomer(Long memberId, MessageDto messageDto){
        log.info("[구매자에게 메세지 보내기]");
        log.info(Thread.currentThread().getName());
        kakaoTokenRepository.findUserTokenByMemberId(memberId)
                .ifPresent(token ->
                {
                    String url = "https://kapi.kakao.com/v2/api/talk/memo/send";

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", "Bearer " + token.getToken());

                    LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                    body.add("template_id", MESSAGE_TEMPLATE_ID);
                    try {
                        body.add("template_args",objectMapper.writeValueAsString(messageDto));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
                    restTemplate.postForEntity(url, request, UserInfo.class);
                });
    }
}

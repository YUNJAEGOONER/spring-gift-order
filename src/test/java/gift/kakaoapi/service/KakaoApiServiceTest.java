package gift.kakaoapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.kakaoapi.dto.TokenResponseDto;
import gift.kakaoapi.dto.UserInfo;
import gift.kakaoapi.tokenmanager.TokenRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(value = KakaoApiService.class)
class KakaoApiServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private KakaoApiService kakaoApiService;

    @BeforeEach
    void setUp(){
        server.reset();
    }

    @Test
    void 토큰_발급() throws JsonProcessingException {
        //given
        TokenResponseDto fakeToken = new TokenResponseDto("iamAnAccessToken", "iamARefreshToken");
        String url = "https://kauth.kakao.com/oauth/token";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(fakeToken), MediaType.APPLICATION_JSON));

        //when
        TokenResponseDto token = kakaoApiService.getAccessToken("iamAuthorizationCode");

        //then
        assertThat(token.accessToken()).isEqualTo(fakeToken.accessToken());
        assertThat(token.refreshToken()).isEqualTo(fakeToken.refreshToken());
    }

    @Test
    void 사용자_정보_획득() throws JsonProcessingException{
        //given
        String fakeId = "thisIsAnId";
        String fakeEmail = "fake@fake.com";

        Map<String, Object> fakeAccount = new HashMap<>();
        fakeAccount.put("email", fakeEmail);

        Map<String, Object> response = new HashMap<>();
        response.put("id", fakeId);
        response.put("kakao_account", fakeAccount);

        String url = "https://kapi.kakao.com/v2/user/me";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(response), MediaType.APPLICATION_JSON));

        //when
        UserInfo userInfo = kakaoApiService.getUserInfo("iAmAnAuthorizationCode");

        //then
        assertThat(userInfo.getId()).isEqualTo(fakeId);
        assertThat(userInfo.getKakaoAccount().getEmail()).isEqualTo(fakeEmail);

    }



}
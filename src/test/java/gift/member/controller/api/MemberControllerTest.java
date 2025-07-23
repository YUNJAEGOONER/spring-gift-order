package gift.member.controller.api;

import static org.assertj.core.api.Assertions.assertThat;

import gift.member.dto.MemberRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.client.HttpClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();
    @Autowired
    private HttpClientProperties httpClientProperties;

    @Test
    void 회원_가입_성공시_토큰이_반환() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member1 = new MemberRequestDto("qazwsx123@gmail.com", "password");

        var response = restClient.post()
                .uri(url)
                .body(member1)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 중복되는_이메일은_회원가입_불가() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member1 = new MemberRequestDto("abcd123@gmail.com", "password");

        var response1 = restClient.post()
                .uri(url)
                .body(member1)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + response1.getBody());
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        MemberRequestDto member2 = new MemberRequestDto("abcd123@gmail.com", "password");
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,
                () -> restClient.post()
                        .uri(url)
                        .body(member2)
                        .retrieve()
                        .toEntity(String.class)
        );
    }

    @Test
    void 로그인_성공시_200과_토큰이_반환(){

        //회원가입
        var registerUrl = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member = new MemberRequestDto("test123@gmail.com", "password");

        var register = restClient.post()
                .uri(registerUrl)
                .body(member)
                .retrieve()
                .toEntity(String.class);

        System.out.println("created token = " + register.getBody());
        assertThat(register.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //로그인
        var loginUrl = "http://localhost:" + port + "/api/members/login";

        var login = restClient.post()
                .uri(loginUrl)
                .body(member)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + login.getBody());
        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void 로그인_실패_비밀번호_틀린경우(){

        //회원가입
        var registerUrl = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member = new MemberRequestDto("test321@gmail.com", "password");

        var register = restClient.post()
                .uri(registerUrl)
                .body(member)
                .retrieve()
                .toEntity(String.class);

        System.out.println("created token = " + register.getBody());
        assertThat(register.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //비밀번호가 틀리는 경우
        var loginUrl = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto loginMember = new MemberRequestDto("test321@gmail.com", "password1");

        Assertions.assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> restClient.post()
                        .uri(loginUrl)
                        .body(loginMember)
                        .retrieve()
                        .toEntity(String.class));
    }

    @Test
    void 가입되지_않은_이메일로_가입하는_경우(){
        var url = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto loginMember = new MemberRequestDto("qewer123@gmail.com", "password");

        Assertions.assertThrows(HttpClientErrorException.Unauthorized .class,
                () -> restClient.post()
                        .uri(url)
                        .body(loginMember)
                        .retrieve()
                        .toEntity(String.class));
    }

    @Test
    void 이메일_형식_유효성_검사_테스트() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member = new MemberRequestDto("abc123", "password");

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,
                () -> restClient.post()
                        .uri(url)
                        .body(member)
                        .retrieve()
                        .toEntity(String.class));
    }
}
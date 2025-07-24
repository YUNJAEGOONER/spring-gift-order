package gift.wishlist.controller.api;

import static org.assertj.core.api.Assertions.assertThat;

import gift.jwt.JwtResponseDto;
import gift.member.dto.MemberRequestDto;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WishListControllerTest {
    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();


    @Test
    void 장바구니에_상품을_추가하는_기능() {
        String token = createMemberAndReturnToken("abc123@gmail.com", "12345678");

        System.out.println("WishListControllerTest.장바구니에_상품을_추가하는_기능");
        System.out.println("token = " + token);

        var url = "http://localhost:" + port + "/api/wishlist/add";

        WishRequestDto wishRequestDto = new WishRequestDto(1L, 50);

        var response = restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(wishRequestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 장바구니의_상품들을_조회하는_기능(){
        String token = loginAndReturnToken("testuser1@naver.com" , "12345678");
        addProduct(2L, 5, token);
        addProduct(3L, 10, token);

        var url = "http://localhost:" + port + "/api/wishlist";

        var response = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println(response.toString());
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 장바구니에_있는_상품을_삭제하는_기능(){
        String token = loginAndReturnToken("testuser1@naver.com" , "12345678");

        addProduct(2L, 5, token);
        addProduct(3L, 10, token);
        Long wishListId = addProduct(2L, 5, token);

        var url = "http://localhost:" + port + "/api/wishlist";

        var response = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println("response = " + response);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        url = "http://localhost:" + port + "/api/wishlist/remove/" + wishListId.toString();

        var deleteResponse = restClient.delete()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        url = "http://localhost:" + port + "/api/wishlist";

        response = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println("response = " + response);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 장바구니에서_상품을_추가하고_제거하는_기능(){
        String token = loginAndReturnToken("testuser2@apple.com" , "12345678");
        //상품 100개를 추가
        Long wishlistId = addProduct(3L, 100, token);

        var url = "http://localhost:" + port + "/api/wishlist/add/" + wishlistId.toString();

        // 상품추가버튼(+) 2번 누름
        var response = restClient.patch()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(WishResponseDto.class);

        response = restClient.patch()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(WishResponseDto.class);

        System.out.println(response.getBody());
        assertThat(response.getBody().quantity()).isEqualTo(102);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        url = "http://localhost:" + port + "/api/wishlist/subtract/" + wishlistId.toString();

        response = restClient.patch()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(WishResponseDto.class);

        System.out.println(response.getBody());
        assertThat(response.getBody().quantity()).isEqualTo(101);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //회원등록
    public String createMemberAndReturnToken(String email, String password){
        var url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password);
        var response = restClient.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(JwtResponseDto.class);
        return response.getBody().token();
    }

    //로그인
    public String loginAndReturnToken(String email, String password){
        var url = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password);
        var response = restClient.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(JwtResponseDto.class);
        return response.getBody().token();
    }

    //상품추가
    public Long addProduct(Long proudctId, Integer quantity, String token){
        var url = "http://localhost:" + port + "/api/wishlist/add";
        WishRequestDto wishRequestDto = new WishRequestDto(proudctId, quantity);
        var response = restClient.post()
                .uri(url)
                .body(wishRequestDto)
                .cookie("token", token)
                .retrieve()
                .toEntity(WishResponseDto.class);
        return response.getBody().wishListId();
    }

}
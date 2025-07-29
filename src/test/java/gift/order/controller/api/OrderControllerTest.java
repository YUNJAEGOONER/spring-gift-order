package gift.order.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.jwt.JwtResponseDto;
import gift.member.dto.MemberRequestDto;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();


    String createMemberAndGetToken(String email, String pw){
        var url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, pw);
        var response = restClient.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(JwtResponseDto.class);
        return response.getBody().token();
    }

    @Test
    void 주문을_정상적으로_생성되면_400을_반환() {
        String token = createMemberAndGetToken("test1@option.com", "12345678");
        var url = "http://localhost:" + port + "/api/orders";
        OrderRequestDto order = new OrderRequestDto(1L, null,10, "이건 선물이야");
        var response = restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(order)
                .retrieve()
                .toEntity(OrderResponseDto.class);

        assertAll(
                () -> assertThat(response.getBody().id()).isNotNull(),
                () -> assertThat(response.getBody().message()).isEqualTo(order.message()),
                () -> assertThat(response.getBody().quantity()).isEqualTo(order.quantity()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
        );
    }

    @Test
    void 옵션을_입력하지_않으면_201을_반환() {
        String token = createMemberAndGetToken("test2@option.com", "12345678");
        var url = "http://localhost:" + port + "/api/orders";
        OrderRequestDto order = new OrderRequestDto(null, null,10, "이건 선물이야");
        assertThrows(HttpClientErrorException.BadRequest.class,
                () -> restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(order)
                .retrieve()
                .toEntity(OrderResponseDto.class)
        );
    }

    @Test
    void 모든_주문을_조회() {
        String token = createMemberAndGetToken("test3@option.com", "12345678");

        var url = "http://localhost:" + port + "/api/orders";
        OrderRequestDto order = new OrderRequestDto(1L, null,10, "이건 선물이야");
        var response = restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(order)
                .retrieve()
                .toEntity(OrderResponseDto.class);

        url = "http://localhost:" + port + "/api/orders";
        var orderList = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println("orderList = " + orderList);

        assertAll(
                () -> assertThat(orderList.getBody()).isNotEmpty(),
                () -> assertThat(orderList.getStatusCode()).isEqualTo(HttpStatus.OK)
        );
    }

    @Test
    void 특정_멤버의_주문을_조회_주문이_없는_경우() {
        String token = createMemberAndGetToken("test5@option.com", "12345678");

        String url = "http://localhost:" + port + "/api/orders/members";
        var orderList = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println("orderList = " + orderList);

        assertAll(
                () -> assertThat(orderList.getBody()).isEmpty(),
                () -> assertThat(orderList.getBody().size()).isEqualTo(0),
                () -> assertThat(orderList.getStatusCode()).isEqualTo(HttpStatus.OK)
        );
    }

    @Test
    void 특정_멤버의_주문을_조회() {
        String token = createMemberAndGetToken("test6@option.com", "12345678");

        var url = "http://localhost:" + port + "/api/orders";
        OrderRequestDto order = new OrderRequestDto(1L, null,10, "이건 선물이야");
        var response = restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(order)
                .retrieve()
                .toEntity(OrderResponseDto.class);

        OrderRequestDto order2 = new OrderRequestDto(2L, null,10, "이것도 선물이야");
        response = restClient.post()
                .uri(url)
                .cookie("token", token)
                .body(order2)
                .retrieve()
                .toEntity(OrderResponseDto.class);

        url = "http://localhost:" + port + "/api/orders/members";
        var orderList = restClient.get()
                .uri(url)
                .cookie("token", token)
                .retrieve()
                .toEntity(List.class);

        System.out.println("orderList = " + orderList);

        assertAll(
                () -> assertThat(orderList.getBody()).isNotEmpty(),
                () -> assertThat(orderList.getBody().size()).isEqualTo(2),
                () -> assertThat(orderList.getStatusCode()).isEqualTo(HttpStatus.OK)
        );
    }

    @Test
    void 유효하지_않은_회원의_주문을_조회() {
        String token = "eyJhbciOiJIUzM4NCJ9.eyJlbWFpbCI6ImhlbGxvQGhlbGxvLmNvbSIsIm1lbWJlcklkIjo1LCJyb2xlIjoiVVNFUiJ9.i-RSfHkaSnKXOaIAELZueSLUWeS45RCDpKtjZLZoGrkBhAttMwuoFVEq4mF6HIj";
        var url = "http://localhost:" + port + "/api/orders/members";
        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> restClient.get()
                        .uri(url)
                        .cookie("token", token)
                        .retrieve()
                        .toEntity(List.class));
    }
}
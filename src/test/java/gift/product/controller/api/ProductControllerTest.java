package gift.product.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.option.dto.OptionRequestDto;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    @Test
    void 상품_추가(){
        var url = "http://localhost:" + port + "/api/products";

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto();
        requestDto.setName("애플비전");
        requestDto.setPrice(5550000);
        requestDto.setImageUrl("https://www.apple.com/newsroom/images/media/introducing-apple-vision-pro/Apple-WWDC23-Vision-Pro-glass-230605_big.jpg.large.jpg");

        List<OptionRequestDto> options = new ArrayList<>();
        options.add(new OptionRequestDto("38inc", 999, 0));
        options.add(new OptionRequestDto("44inc", 100, 150000));

        requestDto.setOptions(options);

        var response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 상품명_카카오_허용불가(){

        var url = "http://localhost:" + port + "/api/products";

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto();
        requestDto.setName("카카오프렌즈");
        requestDto.setPrice(15000);
        requestDto.setImageUrl("https://i.namu.wiki/i/GQMqb8jtiqpCo6_US7jmWDO30KfPB2MMvbdURVub61Rs6ALKqbG-nUATj-wNk7bXXWIDjiLHJxWYkTELUgybkA.webp");

        Assertions.assertThatExceptionOfType(BadRequest.class)
                .isThrownBy(
                        ()-> restClient.post()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class));
    }

    @Test
    void 상품명_허용되는_특수문자(){
        var url = "http://localhost:" + port + "/api/products";

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto();
        requestDto.setName("[애플워치울트라]");
        requestDto.setPrice(340000);
        requestDto.setImageUrl("https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcRzZTIOEqeEMHNP4zFNRWCB_BuBv22q881TH1fY3GShPKuJqNBxh8HIELZcTjj7FhvSqpwSleJj");

        List<OptionRequestDto> options = new ArrayList<>();
        options.add(new OptionRequestDto("44mm", 999, 0));
        options.add(new OptionRequestDto("46mm", 100, 150000));
        requestDto.setOptions(options);

        ResponseEntity<Product> response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 허용_되지않는_특수문자(){
        var url = "http://localhost:" + port + "/api/products";

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto();
        requestDto.setName("$애플워치울트라$");
        requestDto.setPrice(340000);
        requestDto.setImageUrl("https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcRzZTIOEqeEMHNP4zFNRWCB_BuBv22q881TH1fY3GShPKuJqNBxh8HIELZcTjj7FhvSqpwSleJj");

        assertThrows(BadRequest.class,
                () -> restClient.post()
                        .uri(url)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Product.class));
    }

    @Test
    void 상품_정상_조회(){
        var url = "http://localhost:" + port + "/api/products/9999";
        var response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("아이폰16Pro");
    }

    @Test
    void 없는_상품을_조회하는_경우_404반환(){
        var url = "http://localhost:" + port + "/api/products/115";
        Assertions.assertThatExceptionOfType(NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.get()
                                        .uri(url)
                                        .retrieve()
                                        .toEntity(Void.class)

                );
    }

    @Test
    void 전체_상품을_조회하는_기능(){
        var url = "http://localhost:" + port + "/api/products";
        var response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(List.class);
        System.out.println("response = " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_수정하는_기능(){
        var url = "http://localhost:" + port + "/api/products/9999";
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("아이폰15");
        requestDto.setPrice(550000);
        requestDto.setImageUrl("https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcRoaNDLkmOrSKI4HJ80_6OrpwF7UAyAme0pw_IO2W4G0JqiQOaHohKg4x48ulWc1py_2VfEVKUw");

        var response = restClient.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getBody().getName()).isEqualTo("아이폰15");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_삭제하는_기능(){
        var url = "http://localhost:" + port + "/api/products/9998";
        var response = restClient.delete()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 상품_수정(){
        var url = "http://localhost:" + port + "/api/products/9999";

        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("맥북Air(M4)");
        requestDto.setPrice(1235000);
        requestDto.setImageUrl("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQIOLM8rb3eJVMzijKJcSS5NFVgVRkkGVUVJu8_X_CkcGB4WW-VJrtT9E2l-qgHI0N1bOwhojEe");

        var response = restClient.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품_수정_허용되지_않는_상품명(){
        var url = "http://localhost:" + port + "/api/products/9999";

        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("카카오북Air(M4)");
        requestDto.setPrice(1235000);
        requestDto.setImageUrl("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQIOLM8rb3eJVMzijKJcSS5NFVgVRkkGVUVJu8_X_CkcGB4WW-VJrtT9E2l-qgHI0N1bOwhojEe");
        assertThrows(BadRequest.class,
                () -> restClient.put()
                        .uri(url)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(Product.class));
    }

    @Test
    void 상품의_모든_옵션을_가져오는_기능() {
        Long productId = addProduct();
        var url = "http://localhost:" + port + "/api/products/" + productId + "/options";
        var response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductOptionResponseDto.class);
        assertAll(
                () -> assertThat(response.getBody().getOptions().size()).isEqualTo(2),
                () -> assertEquals(response.getStatusCode(), HttpStatus.OK)
        );
    }

    public Long addProduct() {
        var url = "http://localhost:" + port + "/api/products";
        ProductOptionRequestDto requestDto = new ProductOptionRequestDto();

        requestDto.setName("애플워치울트라");
        requestDto.setPrice(340000);
        requestDto.setImageUrl(
                "https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcRzZTIOEqeEMHNP4zFNRWCB_BuBv22q881TH1fY3GShPKuJqNBxh8HIELZcTjj7FhvSqpwSleJj");

        List<OptionRequestDto> options = new ArrayList<>();
        options.add(new OptionRequestDto("44mm", 999, 0));
        options.add(new OptionRequestDto("46mm", 100, 15000));
        requestDto.setOptions(options);

        return restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class)
                .getBody()
                .getId();
    }

}

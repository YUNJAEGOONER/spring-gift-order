package gift.option.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OptionControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    @Test
    void 상품에_옵션을_추가() {
        Long productId = addProduct();
        var url = "http://localhost:" + port + "/api/products/" + productId.toString() + "/options";
        OptionRequestDto requestDto = new OptionRequestDto("38mm", 123, 150000);
        var response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);
        assertAll(
                () -> assertThat(response.getBody().id()).isNotNull(),
                () -> assertEquals("38mm", response.getBody().optionName()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
        );
    }

    @Test
    void 동일한_옵션명은_등록_불가() {
        Long productId = addProduct();
        var url = "http://localhost:" + port + "/api/products/" + productId.toString() + "/options";
        OptionRequestDto requestDto = new OptionRequestDto("44mm", 123, 150000);

        assertThrows(HttpClientErrorException.BadRequest.class,
                () -> restClient.post()
                        .uri(url)
                        .body(requestDto)
                        .retrieve()
                        .toEntity(OptionResponseDto.class));
    }

    @Test
    void 옵션을_삭제하는_기능() {
        Long productId = addProduct();

        //추가
        var url = "http://localhost:" + port + "/api/products/" + productId.toString() + "/options";
        OptionRequestDto requestDto = new OptionRequestDto("38mm", 123, 150000);
        var response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        Long optionId = response.getBody().id();
        assertThat(optionId).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //삭제
        url = "http://localhost:" + port + "/api/options/" + optionId.toString();
        var deleteresponse = restClient.delete()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);

        assertThat(deleteresponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 옵션명을_수정하는_기능() {
        Long productId = addProduct();
        var url = "http://localhost:" + port + "/api/products/" + productId.toString() + "/options";
        OptionRequestDto requestDto = new OptionRequestDto("38mm", 123, 150000);
        var response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        Long optionId = response.getBody().id();
        url = "http://localhost:" + port + "/api/options/" + optionId.toString();

        OptionRequestDto updateDto = new OptionRequestDto("42mm", 123, 150000);

        var updateResponse = restClient.put()
                .uri(url)
                .body(updateDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        assertAll(
                () -> assertThat(updateResponse.getBody().id()).isNotNull(),
                () -> assertEquals("42mm", updateResponse.getBody().optionName()),
                () -> assertEquals(HttpStatus.OK, updateResponse.getStatusCode()),
                () -> assertThat(updateResponse.getBody().id()).isEqualTo(optionId)
        );
    }

    @Test
    void 옵션_수량을_수정하는_기능() {
        Long productId = addProduct();
        var url = "http://localhost:" + port + "/api/products/" + productId.toString() + "/options";
        OptionRequestDto requestDto = new OptionRequestDto("38mm", 123, 150000);
        var response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        Long optionId = response.getBody().id();
        url = "http://localhost:" + port + "/api/options/" + optionId.toString();

        OptionRequestDto updateDto = new OptionRequestDto("38mm", 999, 150000);

        var updateResponse = restClient.put()
                .uri(url)
                .body(updateDto)
                .retrieve()
                .toEntity(OptionResponseDto.class);

        assertAll(
                () -> assertThat(updateResponse.getBody().id()).isNotNull(),
                () -> assertEquals("38mm", updateResponse.getBody().optionName()),
                () -> assertEquals(999, updateResponse.getBody().quantity()),
                () -> assertEquals(HttpStatus.OK, updateResponse.getStatusCode()),
                () -> assertThat(updateResponse.getBody().id()).isEqualTo(optionId)
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
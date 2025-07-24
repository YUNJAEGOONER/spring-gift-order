package gift.option.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequestDto {

    @NotBlank(message = "옵션명을 입력해주세요")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9()\\[\\]\\+\\-\\&/_ ]+$", message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 입력이 가능합니다.")
    @Size(max = 50, message = "옵션명은 최대 50글자입니다.")
    String name;

    @NotNull(message = "해당 옵션의 수량을 입력해주세요")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 100_000_000, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    Integer quantity;

    public OptionRequestDto(String name, Integer quantity, Integer price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public OptionRequestDto() {

    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @NotNull(message = "옵션 가격을 입력해주세요")
    Integer price;

}
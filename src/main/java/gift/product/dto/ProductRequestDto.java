package gift.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수로 입력해야 합니다.")
    @Size(max = 15, message = "상품명은 15글자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9()\\[\\]\\+\\-\\&/_]+$", message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 입력이 가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "\"카카오\"가 포함된 문구는 담당 MD와 협의가 필요합니다.")
    private String name;

    @NotNull(message = "상품 가격은 필수로 입력해야합니다.")
    @Min(value = 0, message = "상품 가격은 1원 이상이어야 합니다.")
    private Integer price;

    @NotBlank(message = "상품 이미지 등록은 필수입니다.")
    @Size(max = 255, message = "이미지 주소는 255자 이하여야 합니다.")
    private String imageUrl;

    //setter를 추가 : modelAttribute(html form)...
    public void setName(String name){
        this.name = name;
    }

    public void setPrice(Integer price){
        this.price = price;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    //getter
    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

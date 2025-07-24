package gift.wishlist.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequestDto(
        @NotNull(message = "상품은 필수적으로 선택되어야 합니다.")
        Long optionId,

        @NotNull(message = "주문할 상품의 수를 입력하세요")
        @Min(value = 1, message = "상품은 1개 이상 추가되어야 합니다.")
        Integer quantity
) {

}

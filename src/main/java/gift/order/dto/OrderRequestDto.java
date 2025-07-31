package gift.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull(message = "옵션을 필수로 선택해야 합니다.")
        Long optionId,
        Long wishId,
        @NotNull(message = "구매 수량은 필수로 입력해야 합니다.")
        @Min(value = 1, message = "구매 수량은 1개 이상이어야 합니다.")
        Integer quantity,
        String message
) {

}

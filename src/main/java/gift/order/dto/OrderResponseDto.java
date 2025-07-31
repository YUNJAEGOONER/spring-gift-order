package gift.order.dto;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long memberId,
        Long optionId,
        Integer quantity,
        Integer price,
        LocalDateTime orderDateTime,
        String message
) {

}

package gift.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {


}

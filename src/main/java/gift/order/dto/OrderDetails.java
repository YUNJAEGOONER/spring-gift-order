package gift.order.dto;

import java.time.LocalDateTime;

public record OrderDetails(
        Long id,
        LocalDateTime orderDateTime,
        String productImage,
        String productName,
        String optionName,
        Integer quantity,
        Integer price,
        String message
) {
    
}

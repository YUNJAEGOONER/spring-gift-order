package gift.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MessageDto(
        String productName,
        String optionName,
        Integer productPrice,
        Integer quantity,
        Integer totalPrice,
        String message
) {}


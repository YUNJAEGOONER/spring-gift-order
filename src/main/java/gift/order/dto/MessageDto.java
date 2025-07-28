package gift.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageDto(
        @JsonProperty("product_name")
        String productName,
        @JsonProperty("option_name")
        String optionName,
        @JsonProperty("product_price")
        Integer productPrice,
        Integer quantity,
        @JsonProperty("total_price")
        Integer totalPrice,
        String message
) {}


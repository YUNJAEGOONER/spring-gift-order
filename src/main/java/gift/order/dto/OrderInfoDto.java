package gift.order.dto;

import gift.option.dto.OptionResponseDto;
import gift.product.dto.ProductResponseDto;

public record OrderInfoDto(
        ProductResponseDto product,
        OptionResponseDto option,
        Long wishListId,
        Integer quantity,
        Integer price
) {

}

package gift.order.dto;

import gift.option.dto.OptionResponseDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;

public record OrderInfoDto(
        ProductResponseDto product,
        OptionResponseDto option,
        Integer quantity,
        Integer price
){

}

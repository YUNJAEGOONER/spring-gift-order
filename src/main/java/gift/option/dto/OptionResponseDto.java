package gift.option.dto;

public record OptionResponseDto(
        Long id,
        Long productId,
        String optionName,
        Integer quantity,
        Integer price
) {

}

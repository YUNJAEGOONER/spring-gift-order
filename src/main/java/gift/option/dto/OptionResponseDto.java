package gift.option.dto;

public record OptionResponseDto(
        Long id,
        String optionName,
        Integer quantity,
        Integer price
) {

}

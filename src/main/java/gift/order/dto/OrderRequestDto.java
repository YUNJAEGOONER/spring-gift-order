package gift.order.dto;

public record OrderRequestDto(
        Long optionId,
        Long wishId,
        Integer quantity,
        String message
) {

}

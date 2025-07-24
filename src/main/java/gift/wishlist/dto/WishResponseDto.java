package gift.wishlist.dto;

public record WishResponseDto(
        Long wishListId,
        Long optionId,
        String productName,
        String optionName,
        String productImage,
        Integer quantity,
        Integer price
) {

}

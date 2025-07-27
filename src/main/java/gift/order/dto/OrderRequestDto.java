package gift.order.dto;

public class OrderRequestDto {
    private Long optionId;
    private Long memberId;
    private Long wishlistId;
    private Integer quantity;
    private String message;

    public OrderRequestDto(){
        this(null, null, null, null);
    }

    public OrderRequestDto(Long optionId, Long memberId, Integer quantity, String message) {
        this(optionId, memberId, null, quantity, message);
    }

    public OrderRequestDto(Long optionId, Long memberId, Long wishlistId, Integer quantity,
            String message) {
        this.optionId = optionId;
        this.memberId = memberId;
        this.wishlistId = wishlistId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}

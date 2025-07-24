package gift.product.dto;

import gift.product.entity.Product;

public class ProductResponseDto{
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public ProductResponseDto(Product product){
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        imageUrl = product.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

package gift.product.dto;

import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.product.entity.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductOptionResponseDto {

    private Long id;

    private String name;

    private Integer price;

    private String imageUrl;

    public ProductOptionResponseDto() {}

    private List<OptionResponseDto> options = new ArrayList<>();

    public ProductOptionResponseDto(Product product, List<Option> optionList) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.options = optionList.stream().map(
                option -> new OptionResponseDto(
                        option.getId(),
                        option.getName(),
                        option.getQuantity(),
                        option.getPrice()
                )
        ).toList();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<OptionResponseDto> getOptions() {
        return options;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

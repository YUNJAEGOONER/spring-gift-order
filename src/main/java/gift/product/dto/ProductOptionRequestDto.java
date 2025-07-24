package gift.product.dto;

import gift.option.dto.OptionRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class ProductOptionRequestDto extends ProductRequestDto{

    @NotEmpty(message = "상품에는 한 개 이상의 옵션을 등록해야 합니다.")
    List<@Valid OptionRequestDto> options = new ArrayList<>();

    public void setOptions(List<OptionRequestDto> options) {
        this.options = options;
    }

    public List<OptionRequestDto> getOptions() {
        return options;
    }
}

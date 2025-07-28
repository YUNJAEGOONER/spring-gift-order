package gift.option.service;

import gift.exception.ErrorCode;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.option.exception.OptionNotFound;
import gift.option.exception.OptionPriceError;
import gift.option.exception.UnavailableOptionName;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionResponseDto createOptionByProductId(Long productId, OptionRequestDto requestDto) {
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        //상품기본가격 + 옵션가 = 음수가 되는 경우
        checkOptionPrice(product.getPrice(), requestDto.getPrice());

        //동일 상품에 대해 동일한 옵션은 등록 불가
        optionRepository.findOptionByProductIdAndName(product.getId(), requestDto.getName())
                .ifPresent(productOption -> {
                    throw new UnavailableOptionName(ErrorCode.UNAVAILABLE_OPTION_NAME);
                });


        Option option = optionRepository.save(new Option(requestDto.getName(), requestDto.getQuantity(), requestDto.getPrice(), product));

        //연관관계 편의 메서드
        option.setProduct(product);

        return new OptionResponseDto(
                option.getId(),
                option.getProduct().getId(),
                option.getName(),
                option.getQuantity(),
                option.getPrice());
    }

    @Transactional(readOnly = true)
    public OptionResponseDto findOne(Long optionId) {
        Option option = optionRepository.findOptionById(optionId)
                .orElseThrow(() -> new OptionNotFound(ErrorCode.OPTION_NOT_FOUND));
        return new OptionResponseDto(
                option.getId(),
                option.getProduct().getId(),
                option.getName(),
                option.getQuantity(),
                option.getPrice()
        );
    }

    public OptionResponseDto updateOption(Long optionId, OptionRequestDto requestDto) {
        Option option = optionRepository.findOptionById(optionId).orElseThrow(() -> new OptionNotFound(ErrorCode.OPTION_NOT_FOUND));
        optionRepository
                .findOptionByProductIdAndName(option.getProduct().getId(), requestDto.getName())
                .filter(op -> !op.getId().equals(optionId))
                .ifPresent(op-> {throw new UnavailableOptionName(ErrorCode.UNAVAILABLE_OPTION_NAME);});
        checkOptionPrice(option.getProduct().getPrice(), requestDto.getPrice());
        option.changeOption(requestDto.getName(), requestDto.getQuantity(), requestDto.getPrice());
        return new OptionResponseDto(option.getId(), option.getProduct().getId(), option.getName(), option.getQuantity(), option.getPrice());
    }

    @Transactional
    public void removeOption(Long optionId) {
        Option option = optionRepository.findOptionById(optionId).orElseThrow(
                () -> new OptionNotFound(ErrorCode.OPTION_NOT_FOUND));
        option.setProduct(null);
        optionRepository.removeOptionById(optionId);
    }

    @Transactional
    public Long getProductId(Long optionId){
        Option option = optionRepository.findOptionById(optionId).orElseThrow(
                () -> new OptionNotFound(ErrorCode.OPTION_NOT_FOUND)
        );
        return option.getProduct().getId();
    }

    public void checkOptionPrice(Integer productPrice, Integer optionPrice){
        if(productPrice + optionPrice < 0){
            throw new OptionPriceError(ErrorCode.UNAVAILABLE_OPTION_PRICE);
        }
    }

}

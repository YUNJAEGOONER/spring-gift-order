package gift.product.service;

import gift.exception.ErrorCode;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    //상품 추가
    public Product add(ProductOptionRequestDto requestDto){
        Product product = new Product(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
        return productRepository.save(product);
    }

    //상품 검색(id로)
    @Transactional(readOnly = true)
    public ProductResponseDto findOne(Long id){
        Product product = productRepository.findProductById(id).orElseThrow(()-> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductResponseDto(product);
    }

    //특정 상품 조회 시, 옵션까지 모두 조회되도록,,,
    @Transactional(readOnly = true)
    public ProductOptionResponseDto findProductOption(Long productId){
        Product product = productRepository.findProductById(productId).orElseThrow(()-> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductOptionResponseDto(product, product.getOptions());
    }

    //상품 검색
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProduct(String name, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductByNameContaining(name, pageable).map(product -> new ProductResponseDto(product));
    }

    //전체 상품 검색
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).map(product -> new ProductResponseDto(product));
    }

    //상품 수정
    public void modify(Long id, ProductRequestDto requestDto){
        Product product = productRepository.findProductById(id).orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        product.changeProductInfo(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
        productRepository.save(product);
    }

    //상품 삭제
    public void remove(Long id){
        productRepository.removeProductById(id);
    }

}

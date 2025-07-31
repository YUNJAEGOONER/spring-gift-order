package gift.product.controller.api;

import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.exception.page.PageIndexException;
import gift.option.dto.OptionRequestDto;
import gift.option.service.OptionService;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@ResponseBody + @Controller
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService){
        this.productService = productService;
        this.optionService = optionService;
    }

    //create
    @Transactional //상품은 추가되는데 옵션을 추가하는 과정에서 오류가 발생할 수 있음
    @PostMapping("/products")
    public ResponseEntity<ProductOptionResponseDto> createProduct(
            @RequestBody @Valid ProductOptionRequestDto requestDto
    ) {
        Product product = productService.add(requestDto);
        for(OptionRequestDto option : requestDto.getOptions()){
            optionService.createOptionByProductId(product.getId(), option);
        }
        return new ResponseEntity<>(new ProductOptionResponseDto(product, product.getOptions()), HttpStatus.CREATED);
    }

    //특정 상품의 옵션까지 조회
    @GetMapping("/products/{id}/options")
    public ResponseEntity<ProductOptionResponseDto> getProductOption(@PathVariable Long id) {
        ProductOptionResponseDto product = productService.findProductOption(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //특정 상품만을 조회
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        ProductResponseDto product = productService.findOne(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //read
    //전체 상품을 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page, //현재 페이지
            @RequestParam(defaultValue = "5") int size //크기(몇개의 상품을 가져올지)
    ) {
        if(page < 0 || size < 0){
            throw new PageIndexException(ErrorCode.PAGE_INDEX_ERROR);
        }
        Page<ProductResponseDto> productPage = productService.findAll(page, size);
        return new ResponseEntity<>(productPage.getContent(), HttpStatus.OK);
    }

    //update
    //상품 수정
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> modifyProduct(
            @RequestBody @Valid ProductRequestDto requestDto,
            @PathVariable Long id
    ) {
        productService.modify(id, requestDto);
        ProductResponseDto modifiedProduct = productService.findOne(id);
        return new ResponseEntity<>(modifiedProduct, HttpStatus.OK);
    }

    //delete
    //등록된 상품을 삭제
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id) {
        productService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> MemberControllerExceptionHandler(MyException e){
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(e.getErrorCode().getMessage());
    }

}
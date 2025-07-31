package gift.product.controller.view;

import gift.exception.ErrorCode;
import gift.exception.page.PageIndexException;
import gift.option.dto.OptionRequestDto;
import gift.option.service.OptionService;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/admin")
public class ProductAdminViewController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductAdminViewController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    //전체 상품 가져오기
    @GetMapping("/products")
    public String adminProductList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ){
        if(page < 0 || size < 0){
            throw new PageIndexException(ErrorCode.PAGE_INDEX_ERROR);
        }
        Page<ProductResponseDto> productList = productService.findAll(page, size);
        model.addAttribute("productList", productList);
        return "yjshop/admin/product/home";
    }

    //상품 등록 화면을 가져오기
    @GetMapping("/products/add")
    public String productForm(Model model) {
        model.addAttribute("productOptionRequestDto", new ProductOptionRequestDto());
        model.addAttribute("requestDto", new ProductOptionRequestDto());
        return "yjshop/admin/product/form";
    }

    //특정 상품을 검색(id)
    @GetMapping("/products/search")
    public String getProduct(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        //상품 검색하기에 아무런 id를 입력하지 않은 경우 -> 전체 상품을 조회하는 페이지로 이동
        if(id == null){
            return "redirect:/view/admin/products";
        }
        ProductResponseDto product = productService.findOne(id);
        model.addAttribute("product", product);
        return "yjshop/admin/product/productinfo";
    }

    //등록된 상품을 삭제
    @PostMapping("/products/remove/{id}")
    public String removeProduct(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/view/admin/products";
    }

    //상품 등록
    @PostMapping("/products/add")
    public String createProduct(
            @ModelAttribute @Valid ProductOptionRequestDto requestDto,
            BindingResult bindingResult,
            Model model
    ) {

        if(bindingResult.hasErrors()){
            model.addAttribute("requestDto", requestDto);
            return "yjshop/admin/product/form";
        }

        Product product = productService.add(requestDto);
        for(OptionRequestDto option : requestDto.getOptions()){
            optionService.createOptionByProductId(product.getId(), option);
        }

        return "redirect:/view/admin/products/search?id=" + product.getId();
    }

    //수정 폼 불러오기
    @GetMapping("/products/modify/{id}")
    public String modifyForm(
            @PathVariable Long id,
            Model model
    ) {
        ProductOptionResponseDto product = productService.findProductOption(id);
        model.addAttribute("product", product);
        model.addAttribute("productRequestDto", new ProductRequestDto());
        return "yjshop/admin/product/modifyform";
    }

    //상품 수정
    @PostMapping("/products/modify/{id}")
    public String modifyProduct(
            @ModelAttribute @Valid ProductRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model
    ) {
        if(bindingResult.hasErrors()){
            ProductResponseDto product = productService.findOne(id);
            model.addAttribute("product", product);
            return "yjshop/admin/product/modifyform";
        }
        productService.modify(id, requestDto);
        return "redirect:/view/admin/products";
    }

    //옵션 수정 폼 불러오기
    @GetMapping("/products/{id}/options/edit")
    public String optionForm(@PathVariable Long id, Model model)
    {
        ProductOptionResponseDto product = productService.findProductOption(id);
        model.addAttribute("product", product);
        model.addAttribute("productRequestDto", new ProductRequestDto());
        return "yjshop/admin/product/optionmodify";
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public String productNotFound(ProductNotFoundException e, Model model) {
        model.addAttribute("errorMsg", e.getErrorCode().getMessage());
        return "yjshop/admin/product/productnotfound";
    }

    @ExceptionHandler(PageIndexException.class)
    public String pageIndexError(PageIndexException e, Model model) {
        model.addAttribute("errorMsg", e.getErrorCode().getMessage());
        return "redirect:/view/admin/products";
    }

}

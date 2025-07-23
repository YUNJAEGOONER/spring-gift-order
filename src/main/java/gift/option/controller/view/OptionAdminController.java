package gift.option.controller.view;

import gift.option.dto.OptionRequestDto;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/admin")
public class OptionAdminController {

    private final OptionService optionService;

    public OptionAdminController(OptionService optionService) {
        this.optionService = optionService;
    }

    //생성
    @PostMapping("/options/add/{productId}")
    public String createOption(
            @PathVariable Long productId,
            @Valid @ModelAttribute OptionRequestDto optionRequestDto
    ) {
        optionService.createOptionByProductId(productId, optionRequestDto);
        return "redirect:/view/admin/products/" + productId + "/options/edit";
    }

    //수정
    @PostMapping("/options/edit/{optionId}")
    public String editOption(
            @PathVariable Long optionId,
            @Valid @ModelAttribute OptionRequestDto optionRequestDto
    ) {
        Long productId = optionService.getProductId(optionId);
        optionService.updateOption(optionId, optionRequestDto);
        return "redirect:/view/admin/products/" + productId + "/options/edit";
    }

    //삭제
    @PostMapping("/options/remove/{optionId}")
    public String removeOption(@PathVariable Long optionId) {
        Long productId = optionService.getProductId(optionId);
        optionService.removeOption(optionId);
        return "redirect:/view/admin/products/" + productId + "/options/edit";
    }

}

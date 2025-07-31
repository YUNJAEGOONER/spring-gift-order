package gift.option.controller.api;

import gift.exception.MyException;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/products/{productId}/options")
    public ResponseEntity<OptionResponseDto> createOption(
            @PathVariable Long productId,
            @Valid @RequestBody OptionRequestDto optionRequestDto
    ) {
        OptionResponseDto responseDto = optionService.createOptionByProductId(productId, optionRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //optionId를 통해 특정 옵션을 조회
    @GetMapping("/options/{optionId}")
    public ResponseEntity<OptionResponseDto> getOption(@PathVariable Long optionId) {
        return ResponseEntity.ok(optionService.findOne(optionId));
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<OptionResponseDto> editOption(
            @PathVariable Long optionId,
            @Valid @RequestBody OptionRequestDto optionRequestDto
    ) {
        return ResponseEntity.ok(optionService.updateOption(optionId, optionRequestDto));
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> removeOption(@PathVariable Long optionId) {
        optionService.removeOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> MemberControllerExceptionHandler(MyException e) {
        return ResponseEntity.status(e.getErrorCode().getStatusCode())
                .body(e.getErrorCode().getMessage());
    }

}

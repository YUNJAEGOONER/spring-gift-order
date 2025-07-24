package gift.wishlist.controller.api;

import gift.exception.MyException;
import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.service.WishListService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    //precondition : 유효한 회원인지를 확인해야함
    private final WishListService wishListService;

    public WishListController(WishListService wishListService){
        this.wishListService = wishListService;
    }

    //WishList 조회
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishList(
            @LoggedInMember Member member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<WishResponseDto> myWishList = wishListService.getList(member.getMemberId(), page, size);
        return ResponseEntity.ok(myWishList.getContent());
    }

    //위시리스트에 상품을 추가
    @PostMapping("/add")
    public ResponseEntity<WishResponseDto> addToWishList(
            @RequestBody @Valid WishRequestDto wishRequestDto, //상품ID, 수량
            @LoggedInMember Member member
    ){
        WishResponseDto wishResponseDto = wishListService.addToWishList(member.getMemberId(), wishRequestDto);
        return new ResponseEntity<>(wishResponseDto, HttpStatus.CREATED);
    }

    //위시 리스트에 담긴 상품을 삭제
    @DeleteMapping("/remove/{wishListId}")
    public ResponseEntity<Void> removeWishList(@PathVariable Long wishListId){
        wishListService.removeFromWishList(wishListId);
        return ResponseEntity.noContent().build();
    }

    //동일한 상품을 추가하는 경우 (장바구니 내 물품 수량 조절)
    @PatchMapping("/add/{wishListId}")
    public ResponseEntity<WishResponseDto> addItem(
            @PathVariable Long wishListId,
            @LoggedInMember Member member
    ){
        WishResponseDto myWishList = wishListService.changeQuantity(wishListId, 1);
        return ResponseEntity.ok(myWishList);
    }

    //동일한 상품을 제거하는 경우 (장바구니 내 물품 수량 조절)
    @PatchMapping("/subtract/{wishListId}")
    public ResponseEntity<WishResponseDto> subtractItem(
            @PathVariable Long wishListId,
            @LoggedInMember Member member
    ){
        WishResponseDto myWishList = wishListService.changeQuantity(wishListId, -1);
        return ResponseEntity.ok(myWishList);
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> MemberControllerExceptionHandler(MyException e){
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(e.getErrorCode().getMessage());
    }

}
package gift.wishlist.controller.view;

import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/view/my")
@Controller
public class WishListViewController {

    private final WishListService wishListService;

    public WishListViewController(WishListService wishListService){
        this.wishListService = wishListService;
    }

    //WishList에 담긴 상품 목록을 조회
    @GetMapping("/wishlist")
    public String getWishList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @LoggedInMember Member member,
            Model model
    ){
        Page<WishResponseDto> myWishList = wishListService.getList(member.getMemberId(), page, size);
        model.addAttribute("wishlist", myWishList);
        return "/yjshop/user/wishlist";
    }

    //위시리스트에 상품을 추가
    @PostMapping("/wishlist")
    public String addToWishList(
            @ModelAttribute @Valid WishRequestDto wishRequestDto,//옵션ID, 수량
            BindingResult bindingResult,
            @LoggedInMember Member member
    ){
        if(bindingResult.hasErrors()){
            System.out.println("wishRequestDto = " + wishRequestDto);
            return "redirect:/view/products/list";
        }
        wishListService.addToWishList(member.getMemberId(), wishRequestDto);
        return "redirect:/view/my/wishlist";
    }

    //위시 리스트에 담긴 상품을 삭제
    @PostMapping("/wishlist/delete/{wishListId}")
    public String removeWishList(@PathVariable Long wishListId){
        wishListService.removeFromWishList(wishListId);
        return "redirect:/view/my/wishlist";
    }

    //동일한 상품을 추가하는 경우 (장바구니 내 물품 수량 조절 + )
    @PostMapping("/wishlist/add/{wishListId}")
    public String addItem(@PathVariable Long wishListId) {
        wishListService.changeQuantity(wishListId, 1);
        return "redirect:/view/my/wishlist";
    }

    //동일한 상품을 제거하는 경우 (장바구니 내 물품 수량 조절 -)
    @PostMapping("/wishlist/subtract/{wishListId}")
    public String subtractItem(@PathVariable Long wishListId){
        wishListService.changeQuantity(wishListId, -1);
        return "redirect:/view/my/wishlist";
    }

    //결제창 이동
    @GetMapping("/payment")
    public String toPay(){
        return "/yjshop/user/pay";
    }

}
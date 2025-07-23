package gift.wishlist.service;

import gift.exception.ErrorCode;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.WishList;
import gift.wishlist.exception.WishNotFoundException;
import gift.wishlist.repository.WishListRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;

    public WishListService(WishListRepository wishListRepository, OptionRepository optionRepository, MemberRepository memberRepository) {
        this.wishListRepository = wishListRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
    }

    public WishResponseDto addToWishList(Long memberId, WishRequestDto requestDto) {
        Option option = optionRepository.findOptionById(requestDto.optionId()).orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Optional<WishList> wishListOptional = wishListRepository.findWishListByMemberIdAndOptionId(memberId, requestDto.optionId());
        if (wishListOptional.isPresent()) {
            //이미 장바구니에 해당 상품이 있는 경우에는 수량만 업데이트
            WishList wishList = wishListOptional.get();
            wishList.updateQuantity(requestDto.quantity());
            return toWishResponseDto(wishList, option);
        }
        WishList wishList = wishListRepository.save(new WishList(member, option, requestDto.quantity()));
        return toWishResponseDto(wishList, option);
    }

    public WishResponseDto toWishResponseDto(WishList wishList, Option option){
        Product product = option.getProduct();
        Integer totalPrice = wishList.getQuantity() * (option.getPrice() + product.getPrice());
        return new WishResponseDto(
                wishList.getId(),
                option.getId(),
                product.getName(),
                option.getName(),
                product.getImageUrl(),
                wishList.getQuantity(),
                totalPrice
        );
    }

    @Transactional(readOnly = true)
    public Page<WishResponseDto> getList(Long memberId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<WishList> wishListList = wishListRepository.findWishListByMemberId(pageable, memberId);
        return wishListList.map(
                wishList -> new WishResponseDto(
                        wishList.getId(),
                        wishList.getOption().getId(),
                        wishList.getOption().getProduct().getName(),
                        wishList.getOption().getName(),
                        wishList.getOption().getProduct().getImageUrl(),
                        wishList.getQuantity(),
                        wishList.getQuantity() * (wishList.getOption().getProduct().getPrice() + wishList.getOption().getPrice()))
        );
    }

    public void removeFromWishList(Long wishListId){
        wishListRepository.removeWishListById(wishListId);
    }

    public WishResponseDto changeQuantity(Long wishListId, int amount){
        WishList wishList = wishListRepository.findWishListById(wishListId).orElseThrow(() -> new WishNotFoundException(ErrorCode.WISH_NOT_FOUND));
        wishList.updateQuantity(amount);
        if(wishList.getQuantity() == 0){
            removeFromWishList(wishListId);
            return toWishResponseDto(wishList, wishList.getOption());
        }
        return toWishResponseDto(wishList, wishList.getOption());
    }

}

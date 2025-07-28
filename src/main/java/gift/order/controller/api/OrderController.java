package gift.order.controller.api;

import gift.infra.LoggedInMember;
import gift.kakaoapi.service.KakaoApiService;
import gift.member.entity.Member;
import gift.order.dto.OrderDeatilDto;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import gift.wishlist.service.WishListService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class OrderController {

    private OrderService orderService;
    private WishListService wishListService;
    private KakaoApiService kakaoApiService;

    public OrderController(OrderService orderService, WishListService wishListService) {
        this.orderService = orderService;
        this.wishListService = wishListService;
    }

    @Transactional
    @PostMapping("/orders")
    public OrderResponseDto createOrder(
            @RequestBody OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, member.getMemberId());
        if(orderRequestDto.wishId() != null){
            wishListService.removeFromWishList(orderRequestDto.wishId());
        }
        return orderResponseDto;
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getOrders();
    }

    @GetMapping("/members/orders")
    public List<OrderDeatilDto> getMyOrders(@LoggedInMember Member member){
        return orderService.getMyOrders(member.getMemberId());
    }

}

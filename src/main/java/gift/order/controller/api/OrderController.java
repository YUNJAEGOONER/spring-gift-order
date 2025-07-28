package gift.order.controller.api;

import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.order.dto.OrderDetails;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import gift.wishlist.service.WishListService;
import jakarta.validation.Valid;
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

    public OrderController(OrderService orderService, WishListService wishListService) {
        this.orderService = orderService;
        this.wishListService = wishListService;
    }

    @Transactional
    @PostMapping("/orders")
    public OrderResponseDto createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, member.getMemberId());
        //상품 바로 구매를 하는 경우
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
    public List<OrderDetails> getMyOrders(@LoggedInMember Member member){
        return orderService.getMyOrders(member.getMemberId());
    }

}

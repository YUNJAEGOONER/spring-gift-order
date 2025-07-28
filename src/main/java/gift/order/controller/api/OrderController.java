package gift.order.controller.api;

import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.order.dto.OrderDeatilDto;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import gift.wishlist.service.WishListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class OrderController {

    @Autowired private OrderService orderService;

    @Autowired private WishListService wishListService;

    @PostMapping("/orders")
    @Transactional
    public OrderResponseDto createOrder(
            @RequestBody OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, member.getMemberId());
//        if(orderRequestDto.getWishlistId() != null){
//            wishListService.removeFromWishList(orderRequestDto.getWishlistId());
//        }
        return orderResponseDto;
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getOrders();
    }

    @GetMapping("/members/{memberId}/orders")
    public List<OrderDeatilDto> getAllOrders(@LoggedInMember Member member){
        return orderService.getMyOrders(member.getMemberId());
    }


}

package gift.order.controller.view;

import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import gift.order.dto.OrderDeatilDto;
import gift.order.dto.OrderInfoDto;
import gift.order.dto.OrderRequestDto;
import gift.order.service.OrderService;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import gift.wishlist.service.WishListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/view/my")
public class OrderViewController {

    private final OrderService orderService;
    private final OptionService optionService;
    private final ProductService productService;
    private final WishListService wishListService;

    public OrderViewController(OrderService orderService, OptionService optionService,
            ProductService productService, WishListService wishListService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.productService = productService;
        this.wishListService = wishListService;
    }

    //결제 정보창을 가져오기 위한 메서드
    @GetMapping("/orders/payment")
    public String getPayment(
            @RequestParam Long optionId,
            @RequestParam(required = false) Long wishListId,
            @RequestParam Integer quantity,
            Model model
    ){
        OptionResponseDto option = optionService.findOne(optionId);
        ProductResponseDto product = productService.findOne(option.productId());
        int total_price = (option.price() + product.getPrice()) * quantity;
        model.addAttribute("orderInfo",new OrderInfoDto(product, option, wishListId, quantity, total_price));
        return "yjshop/user/order/paymentInfo";
    }

    //주문을 생성
    @Transactional
    @PostMapping("/orders")
    public String createOrders(
            @ModelAttribute OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        orderService.createOrder(orderRequestDto, member.getMemberId());
        if(orderRequestDto.wishId() != null){
            wishListService.removeFromWishList(orderRequestDto.wishId());
        }
        return "redirect:/view/my/orders/list";
    }

    @GetMapping("/orders/list")
    public String getMyOrders(Model model, @LoggedInMember Member member){
        List<OrderDeatilDto> orderList = orderService.getMyOrders(member.getMemberId());
        model.addAttribute("orderList", orderList);
        return "yjshop/user/order/orderlist";
    }

}

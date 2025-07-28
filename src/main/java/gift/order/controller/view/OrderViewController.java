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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/view/my")
public class OrderViewController {

    @Autowired OrderService orderService;
    @Autowired OptionService optionService;
    @Autowired ProductService productService;

    @GetMapping("/orders/payment")
    public String getPayment(
            @RequestParam Long optionId,
            @RequestParam Integer quantity,
            Model model
    ){
        OptionResponseDto option = optionService.findOne(optionId);
        ProductResponseDto product = productService.findOne(option.productId());
        int total_price = (option.price() + product.getPrice()) * quantity;
        model.addAttribute("orderInfo",new OrderInfoDto(product, option, quantity, total_price));
        return "yjshop/user/order/paymentInfo";
    }


    @PostMapping("/orders")
    public String createOrders(
            @ModelAttribute OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        orderService.createOrder(orderRequestDto, member.getMemberId());
        return "redirect:/view/my/orders/list";
    }

    @GetMapping("/orders/list")
    public String getOrders(Model model, @LoggedInMember Member member){
        List<OrderDeatilDto> orderList = orderService.getMyOrders(member.getMemberId());
        model.addAttribute("orderList", orderList);
        return "yjshop/user/order/orderlist";
    }

}

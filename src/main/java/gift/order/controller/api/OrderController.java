package gift.order.controller.api;

import gift.exception.MyException;
import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.order.dto.OrderDetails;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @LoggedInMember Member member
    ){
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, member.getMemberId());
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/orders/members")
    public ResponseEntity<List<OrderDetails>> getMyOrders(@LoggedInMember Member member){
        return ResponseEntity.ok(orderService.getMyOrders(member.getMemberId()));
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> MyExceptionHandler(MyException e){
        return new ResponseEntity(e.getErrorCode().getMessage(), e.getErrorCode().getStatusCode());
    }

}

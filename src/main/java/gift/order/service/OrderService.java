package gift.order.service;

import gift.infra.LoggedInMember;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.order.dto.OrderDeatilDto;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public OrderService(OptionRepository optionRepository, MemberRepository memberRepository,
            OrderRepository orderRepository) {
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId){
        Option option = optionRepository.findOptionById(requestDto.optionId())
                .orElseThrow(() -> new IllegalStateException("선택한 옵션을 찾을 수 없습니다."));

        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(()  -> new IllegalStateException("회원가입 후에 주문을 할 수 있습니다."));

        option.removeStock(requestDto.quantity());

        Integer totalPrice = (option.getProduct().getPrice() + option.getPrice()) * requestDto.quantity();

        Order order = new Order(option, member, requestDto.quantity(), totalPrice, requestDto.message());
        orderRepository.save(order);

        return new OrderResponseDto(order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getPrice(),
                order.getOrderDateTime(),
                order.getMessage());
    }

    public List<OrderResponseDto> getOrders(){
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderResponseDto(order.getId(),
                        order.getOption().getId(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getOrderDateTime(),
                        order.getMessage()))
                .toList();
    }

    public List<OrderDeatilDto> getMyOrders(Long memberId){
        return orderRepository.findAllByMemberId(memberId)
                .stream()
                .map(order -> new OrderDeatilDto(
                        order.getId(),
                        order.getOrderDateTime(),
                        order.getOption().getProduct().getImageUrl(),
                        order.getOption().getProduct().getName(),
                        order.getOption().getName(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getMessage()))
                .toList();
    }

}

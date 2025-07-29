package gift.order.service;

import gift.kakaoapi.service.KakaoApiService;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.order.dto.MessageDto;
import gift.order.dto.OrderDetails;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final KakaoApiService kakaoApiService;

    public OrderService(OptionRepository optionRepository, MemberRepository memberRepository,
            OrderRepository orderRepository, KakaoApiService kakaoApiService) {
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.kakaoApiService = kakaoApiService;
    }

    //주문을 생성하는 기능
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId) {
        Option option = optionRepository.findOptionById(requestDto.optionId())
                .orElseThrow(() -> new IllegalStateException("선택한 옵션을 찾을 수 없습니다."));

        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(()  -> new IllegalStateException("회원가입 후에 주문을 할 수 있습니다."));

        option.removeStock(requestDto.quantity());

        Integer productPrice = option.getProduct().getPrice() + option.getPrice();
        Integer totalPrice = productPrice * requestDto.quantity();
        Order order = new Order(option, member, requestDto.quantity(), totalPrice, requestDto.message());
        orderRepository.save(order);

        //상품, 주문 정보를 카카오톡 메시지로 전송
        MessageDto messageDto = new MessageDto(
                option.getProduct().getName(),
                option.getName(),
                productPrice,
                requestDto.quantity(),
                totalPrice,
                requestDto.message());
        kakaoApiService.sendMessageToCustomer(member.getMemberId(), messageDto);

        return new OrderResponseDto(
                order.getId(),
                order.getMember().getMemberId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getPrice(),
                order.getOrderDateTime(),
                order.getMessage());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders(){
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderResponseDto(
                        order.getId(),
                        order.getMember().getMemberId(),
                        order.getOption().getId(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getOrderDateTime(),
                        order.getMessage()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDetails> getMyOrders(Long memberId){
        return orderRepository.findAllByMemberId(memberId)
                .stream()
                .map(order -> new OrderDetails(
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

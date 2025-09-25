package gift.order.service;

import gift.kakaoapi.service.KakaoMessageEvent;
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
import gift.wishlist.service.WishListService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final WishListService wishListService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OptionRepository optionRepository, MemberRepository memberRepository,
            OrderRepository orderRepository, WishListService wishListService,
            ApplicationEventPublisher eventPublisher) {
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.wishListService = wishListService;
        this.eventPublisher = eventPublisher;
    }

    //주문을 생성하는 기능
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId) {
        Option option = optionRepository.findOptionById(requestDto.optionId())
                .orElseThrow(() -> new IllegalStateException("선택한 옵션을 찾을 수 없습니다."));

        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(()  -> new IllegalStateException("회원가입 후에 주문을 할 수 있습니다."));

        option.removeStock(requestDto.quantity());
        Order order = new Order(option, member, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        //장바구니 내역 삭제
        if(requestDto.wishId() != null){
            wishListService.removeFromWishList(requestDto.wishId());
        }

        log.info(Thread.currentThread().getName());
        sendMessage(order, memberId);

        return new OrderResponseDto(
                order.getId(),
                order.getMember().getMemberId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDateTime(),
                order.getMessage());
    }

    public void sendMessage(Order order, Long memberId){
        MessageDto messageDto = new MessageDto(
                order.getOption().getProduct().getName(),
                order.getOption().getName(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getMessage());
        eventPublisher.publishEvent(new KakaoMessageEvent(memberId, messageDto));
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
                        order.getTotalPrice(),
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
                        order.getTotalPrice(),
                        order.getMessage()))
                .toList();
    }

}
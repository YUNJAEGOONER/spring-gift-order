package gift.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.exception.StockError;
import gift.option.repository.OptionRepository;
import gift.order.dto.OrderDetails;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.entity.Product;
import gift.wishlist.service.WishListService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishListService wishListService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp(){
        orderService = new OrderService(optionRepository, memberRepository, orderRepository, wishListService, eventPublisher);
    }

    @Test
    void 주문을_생성하는_기능() {
        //given
        OrderRequestDto requestDto = new OrderRequestDto(5L, null, 5, "이건 선물이야");
        Long memberId = 15L;

        Product product = new Product("Iphone16", 1250000, "iphone_image");
        Member member = new Member("test@test.com", "password");
        Option option = new Option("256GB", 100, 150000, product);
        Order order = new Order(option, member, requestDto.quantity(), requestDto.message());

        given(optionRepository.findOptionById(any())).willReturn(Optional.of(option));
        given(memberRepository.findMemberById(any())).willReturn(Optional.of(member));
        given(orderRepository.save(any())).willReturn(order);

        //when
        OrderResponseDto responseDto = orderService.createOrder(requestDto, memberId);

        System.out.println("responseDto = " + responseDto);

        assertAll(
                () -> assertThat(responseDto.message()).isEqualTo(requestDto.message()),
                () -> assertThat(responseDto.quantity()).isEqualTo(responseDto.quantity()),
                () -> assertThat(responseDto.price()).isEqualTo(option.calculateSalePrice() * responseDto.quantity())
        );

    }

    @Test
    void 재고보다_더_많은_수량을_구매() {
        //given
        OrderRequestDto requestDto = new OrderRequestDto(5L, null, 5, "이건 선물이야");
        Long memberId = 15L;

        Product product = new Product("Iphone16", 1250000, "iphone_image");
        Member member = new Member("test@test.com", "password");
        Option option = new Option("256GB", 3, 150000, product);
        Order order = new Order(option, member, requestDto.quantity(), requestDto.message());

        given(optionRepository.findOptionById(any())).willReturn(Optional.of(option));
        given(memberRepository.findMemberById(any())).willReturn(Optional.of(member));

        //when
        assertThrows(
                StockError.class,
                () -> orderService.createOrder(requestDto, memberId)
        );

    }


    @Test
    void 나의_주문을_조회하는_기능() {
        //given
        Long memberId = 15L;
        Member member = new Member("test@test.com", "password");
        Product product = new Product("아이폰16", 1250000, "iphoneImage");
        Option option = new Option("256GB", 999, 150000, product);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(option, member, 2,  "이것도 선물이야"));
        orders.add(new Order(option, member, 3,  "야 가져"));

        given(orderRepository.findAllByMemberId(memberId)).willReturn(orders);

        //when
        List<OrderDetails> myOrders = orderService.getMyOrders(memberId);

        //Then
        assertThat(myOrders.size()).isEqualTo(2);
    }
}
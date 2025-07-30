package gift.order.entity;

import gift.member.entity.Member;
import gift.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    private String message;

    public Order(Option option, Member member, Integer quantity, String message){
        this.option = option;
        this.member = member;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = LocalDateTime.now().withNano(0);
        this.totalPrice = calculateTotalPrice(option.calculateSalePrice(), quantity);
    }

    private Integer calculateTotalPrice(Integer salePrice, Integer quantity){
        return salePrice * quantity;
    }

    protected Order() { }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Member getMember() {
        return member;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

}

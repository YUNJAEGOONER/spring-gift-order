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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "option_id")
    Option option;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @Column(nullable = false)
    Integer quantity;

    Integer price;

    @Column(name = "order_date_time", nullable = false)
    LocalDateTime orderDateTime;

    String message;

    protected Order() {

    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Member getMember() {
        return member;
    }

    public Integer getPrice() {
        return price;
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

    public Order(Option option, Member member, Integer quantity, Integer price, String message){
        this.option = option;
        this.member = member;
        this.quantity = quantity;
        this.message = message;
        this.price = price;
        this.orderDateTime = LocalDateTime.now().withNano(0);
    }

}

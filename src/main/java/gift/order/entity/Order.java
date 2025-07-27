package gift.order.entity;

import gift.member.entity.Member;
import gift.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "option_id")
    Option option;

    @OneToOne
    @JoinColumn(name = "member_id")
    Member member;

    @Column(nullable = false)
    Integer quantity;

    @Column(name = "order_date_time", nullable = false)
    String orderDateTime;

    String message;

}

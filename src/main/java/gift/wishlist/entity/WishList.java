package gift.wishlist.entity;

import gift.member.entity.Member;
import gift.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlist")
public class WishList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_Id")
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    public WishList(Member member, Option option, Integer quantity){
        this.member = member;
        this.option = option;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity){
        this.quantity += quantity;
    }

    protected WishList() {}

    public Long getId() {
        return id;
    }

    public Member member() {
        return member;
    }

    public Option getOption() {
        return option;
    }

    public Integer getQuantity(){
        return quantity;
    }
}

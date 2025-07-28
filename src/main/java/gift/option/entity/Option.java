package gift.option.entity;

import gift.exception.ErrorCode;
import gift.option.exception.StockError;
import gift.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") //name_of_FK
    private Product product;

    public void changeOption(String name, Integer quantity, Integer price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void removeStock(int amount) {
        if(quantity - amount < 0){
            throw new StockError(ErrorCode.UNSUFFICIENT_STOCK);
        }
        quantity -= amount;
    }

    protected Option() { }

    public Option(String name, Integer quantity, Integer price, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    //연관 관계 편의 메서드
    public void setProduct(Product product){

        if (this.product != null){
            //기존 product와 연관관계를 제거
            this.product.getOptions().remove(this);
        }

        this.product = product; //새로운 연관관계를 생성

        //product가 null인 경우 -> 반대편에 대해서 연관관계 설정할 필요 x
        if(product != null){
            product.addOption(this); //반대편 연관관계 설정
        }
    }

}

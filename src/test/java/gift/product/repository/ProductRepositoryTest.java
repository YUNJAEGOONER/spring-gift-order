package gift.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired private ProductRepository productRepository;

    @Test
    void 상품_저장(){
        Product product = productRepository.save(new Product("MacBookAir", 1550000, "imageUrl"));
        System.out.println("productId = " + product.getId());
        assertThat(product.getId()).isNotNull();
    }

    @Test
    void 상품_찾기(){
        Product product = productRepository.save(new Product("Iphone16", 1250000, "iphoneimage"));
        Optional<Product> findProduct = productRepository.findProductById(product.getId());
        assertThat(findProduct.get()).isEqualTo(product);
        assertThat(findProduct.get().getId()).isEqualTo(product.getId());
    }

    @Test
    void 상품_삭제(){
        Product product = productRepository.save(new Product("IpadPro", 875000, "ipadimage"));
        assertThat(product.getId()).isNotNull();
        productRepository.removeProductById(product.getId());
        Optional<Product> findProduct = productRepository.findProductById(product.getId());
        assertThat(findProduct).isEmpty();
    }

    @Test
    void 상품_검색(){
        Product product1 = productRepository.save(new Product("Galaxy Fold7", 1875000, "samsungisthebest"));
        Product product2 = productRepository.save(new Product("Galaxy S25", 1250000, "galaxyisthebest"));
        assertThat(product1.getId()).isNotNull();
        assertThat(product2.getId()).isNotNull();
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> productList = productRepository.findProductByNameContaining("Galaxy", pageable).getContent();
        assertThat(productList.size()).isEqualTo(2);
    }

}

//void removeProductById(Long id);
//List<Product> findProductByNameContaining(String name);
package gift.option.repository;

import gift.option.entity.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findOptionByProduct_Id(Long productId);
    Optional<Option> findOptionByProductIdAndName(Long productId, String name);
    Optional<Option> findOptionById(Long id);
    void removeOptionById(Long id);
}

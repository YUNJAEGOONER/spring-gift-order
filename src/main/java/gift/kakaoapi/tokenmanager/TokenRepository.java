package gift.kakaoapi.tokenmanager;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findUserTokenByMemberId(Long memberId);
}

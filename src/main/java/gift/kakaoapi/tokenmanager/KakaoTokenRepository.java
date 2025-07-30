package gift.kakaoapi.tokenmanager;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findUserTokenByMemberId(Long memberId);
}

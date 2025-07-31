package gift.kakaoapi.tokenmanager;

import gift.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;

    protected KakaoToken() {
    }

    public KakaoToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void updateAccessToken(String token){
        this.token = token;
    }
}

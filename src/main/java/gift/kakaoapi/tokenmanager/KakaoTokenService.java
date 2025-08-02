package gift.kakaoapi.tokenmanager;

import gift.exception.ErrorCode;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;
    private final MemberRepository memberRepository;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository, MemberRepository memberRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.memberRepository = memberRepository;
    }

    public void saveUserToken(Long memberId, String accessToken){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(
                ()-> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        kakaoTokenRepository.findUserTokenByMemberId(memberId)
                .ifPresentOrElse(token -> token.updateAccessToken(accessToken),
                        () -> kakaoTokenRepository.save(new KakaoToken(member, accessToken)));

    }

}

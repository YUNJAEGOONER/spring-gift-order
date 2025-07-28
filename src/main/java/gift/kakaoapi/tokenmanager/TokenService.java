package gift.kakaoapi.tokenmanager;

import gift.exception.ErrorCode;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired TokenRepository tokenRepository;
    @Autowired MemberRepository memberRepository;

    public void saveUserToken(Long memberId, String accessToken){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(
                ()-> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        tokenRepository.findUserTokenByMemberId(memberId)
                .ifPresentOrElse(token -> token.updateAccessToken(accessToken),
                        () -> tokenRepository.save(new UserToken(member, accessToken)));

    }

}

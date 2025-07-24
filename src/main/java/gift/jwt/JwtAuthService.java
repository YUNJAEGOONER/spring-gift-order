package gift.jwt;

import gift.exception.ErrorCode;
import gift.jwt.exception.JWTAuthException;
import gift.member.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//JWT와 관련된 서비스 :
@Service
public class JwtAuthService {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthService.class);
    private final String secretKey = "ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER";

    //payload의 정보를 추출하는 함수
    public Long getMemberId(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    //payload의 정보를 추출하는 함수
    public String getMemberRole(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    //토큰 생성
    public String createJwt(String email, Long memberId, Role role){
        return Jwts.builder()
                .claim("email", email)
                .claim("memberId", memberId)
                .claim("role", role.toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //토큰을 검증 -> 로그인 이후의 동작 (wishList -> 사용자별 wishList 존재)
    public void checkValidation(String token) {
        if(token==null){
            throw new JWTAuthException(ErrorCode.JWT_VALIDATION_FAIL);
        }
        try{
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
        }
        catch (Exception e){
            //Authorization 헤더가 유효하지 않거나 토큰이 유효하지 않은 경우 401 Unauthorized 반환
            log.info("토큰검증에 실패했습니다.");
            throw new JWTAuthException(ErrorCode.JWT_VALIDATION_FAIL);
        }
    }

}
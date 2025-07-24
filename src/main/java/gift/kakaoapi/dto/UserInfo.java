package gift.kakaoapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    @JsonProperty("id")
    String id;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {

        @JsonProperty("email")
        String email;

        public String getEmail() {
            return email;
        }
    }

    public String getId() {
        return id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

}



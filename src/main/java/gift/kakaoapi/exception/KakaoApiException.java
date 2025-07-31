package gift.kakaoapi.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class KakaoApiException extends MyException {
    public KakaoApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}

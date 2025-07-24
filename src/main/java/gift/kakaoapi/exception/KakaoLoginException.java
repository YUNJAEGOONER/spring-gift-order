package gift.kakaoapi.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class KakaoLoginException extends MyException {

    public KakaoLoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}

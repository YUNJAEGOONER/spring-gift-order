package gift.jwt.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class JWTAuthException extends MyException {
    public JWTAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}

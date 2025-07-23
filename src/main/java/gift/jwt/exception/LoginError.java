package gift.jwt.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class LoginError extends MyException {

    public LoginError(ErrorCode errorCode) {
        super(errorCode);
    }
}

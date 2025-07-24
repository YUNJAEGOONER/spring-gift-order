package gift.member.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class UnavailableEmailException extends MyException {

    public UnavailableEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}

package gift.option.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class UnavailableOptionName extends MyException {

    public UnavailableOptionName(ErrorCode errorCode) {
        super(errorCode);
    }
}

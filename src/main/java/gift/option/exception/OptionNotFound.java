package gift.option.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class OptionNotFound extends MyException {

    public OptionNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}

package gift.option.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class OptionPriceError extends MyException {

    public OptionPriceError(ErrorCode errorCode) {
        super(errorCode);
    }
}

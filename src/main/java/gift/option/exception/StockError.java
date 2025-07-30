package gift.option.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class StockError extends MyException {

    public StockError(ErrorCode errorCode) {
        super(errorCode);
    }
}

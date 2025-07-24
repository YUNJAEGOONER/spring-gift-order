package gift.exception.page;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class PageIndexException extends MyException {
    public PageIndexException(ErrorCode errorCode) {
        super(errorCode);
    }
}

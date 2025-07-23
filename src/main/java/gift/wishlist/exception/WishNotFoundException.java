package gift.wishlist.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class WishNotFoundException extends MyException {

    public WishNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

package gift.product.exception;

import gift.exception.ErrorCode;
import gift.exception.MyException;

public class ProductNotFoundException extends MyException {

  public ProductNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

}

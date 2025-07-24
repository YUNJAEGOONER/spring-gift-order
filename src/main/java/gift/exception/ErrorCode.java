package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    JWT_VALIDATION_FAIL(HttpStatus.UNAUTHORIZED, "JWT 인증 실패"),
    LOGIN_REQUIRED_FAIL(HttpStatus.UNAUTHORIZED, "로그인을 해야합니다."),
    ADMIN_PAGE(HttpStatus.UNAUTHORIZED, "관리자 전용 페이지 입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."),
    WISH_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 위시리스트를 찾을 수 없습니다."),
    PAGE_INDEX_ERROR(HttpStatus.NOT_FOUND, "해당 페이지를 찾을 수 없습니다."),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 옵션을 찾을 수 없습니다."),

    EMAIL_PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "아이디와 비밀번호는 필수로 입력해야합니다."),
    UNAVAILABLE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입되어 있는 이메일 입니다."),
    UNAVAILABLE_OPTION_NAME(HttpStatus.BAD_REQUEST, "이미 사용중인 옵션명 입니다."),
    UNAVAILABLE_OPTION_PRICE(HttpStatus.BAD_REQUEST, "상품의 가격은 0원 이상이어야 합니다."),

    LOGIN_UNAVAILABLE(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다."),;

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode(){
        return statusCode;
    }

    public String getMessage(){
        return message;
    }
}
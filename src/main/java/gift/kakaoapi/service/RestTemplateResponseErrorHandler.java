package gift.kakaoapi.service;

import gift.exception.ErrorCode;
import gift.kakaoapi.exception.KakaoApiException;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
            response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().is5xxServerError()){
            throw new KakaoApiException(ErrorCode.KAKAO_SERVER_ERROR);
        }
        else if(response.getStatusCode().is4xxClientError()){
            throw new KakaoApiException(ErrorCode.KAKAO_CLIENT_ERROR);
        }
    }
}

package gift.kakaoapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KakaoMessageEventListener {

    private static final Logger log = LoggerFactory.getLogger(KakaoMessageEventListener.class);

    private final KakaoApiService kakaoApiService;

    public KakaoMessageEventListener(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void kakaoMessageHandler(KakaoMessageEvent event){
        log.info("TransactionalEventListener");
        log.info(Thread.currentThread().getName());
        kakaoApiService.sendMessageToCustomer(event.memberId(), event.messageDto());
    }

}

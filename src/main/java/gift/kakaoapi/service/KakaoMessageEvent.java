package gift.kakaoapi.service;

import gift.order.dto.MessageDto;

public record KakaoMessageEvent (
        Long memberId,
        MessageDto messageDto
){}

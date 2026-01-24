package kr.co.breadfeetserver.presentation.diary.dto.request;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.global.annotation.IsEssential;
import kr.co.breadfeetserver.global.annotation.PhoneNumberPattern;
import kr.co.breadfeetserver.presentation.bakery.dto.request.AddressUpdateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryUpdateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential Integer score,
        @IsEssential AddressUpdateRequest address,
        @IsEssential String thumbnail,
        LocalDateTime visitDate,
        String content
) {

    public Diary toEntity(Long memberId) {
        return Diary.builder()
                .isPublic(isPublic)
                .score(score)
                .address(address.toEntity())
                .thumbnailUrl(thumbnail)
                .visitDate(visitDate)
                .content(content)
                .build();
    }
}

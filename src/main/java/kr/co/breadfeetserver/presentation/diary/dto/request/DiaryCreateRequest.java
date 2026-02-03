package kr.co.breadfeetserver.presentation.diary.dto.request;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.global.annotation.IsEssential;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryCreateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential Integer score,
        @IsEssential AddressJpaVO address,
        @IsEssential String thumbnail,
        LocalDateTime visitDate,
        String content
) {
    public Diary toEntity(Long memberId){
        return Diary.builder()
                .isPublic(isPublic)
                .score(score)
                .address(address)
                .thumbnailUrl(thumbnail)
                .visitDate(visitDate)
                .content(content)
                .memberId(memberId)
                .build();
    }
}

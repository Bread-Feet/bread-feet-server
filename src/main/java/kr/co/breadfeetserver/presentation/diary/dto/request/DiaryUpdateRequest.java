package kr.co.breadfeetserver.presentation.diary.dto.request;

import java.time.LocalDateTime;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.bakery.dto.request.AddressUpdateRequest;

public record DiaryUpdateRequest(
        @IsEssential long diaryId,
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

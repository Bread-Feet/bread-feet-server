package kr.co.breadfeetserver.presentation.diary.dto.request;

import java.time.LocalDateTime;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.bakery.dto.request.AddressUpdateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DiaryUpdateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential AddressUpdateRequest address,
        @IsEssential String thumbnail,
        @IsEssential String title,
        @IsEssential String bakeryName,
        LocalDateTime visitDate,
        String content,
        List<String> hashtags,
        List<String> pictureUrls
) {
    public Diary toEntity(Long memberId) {
        return Diary.builder()
                .isPublic(isPublic)
                .address(address.toEntity())
                .thumbnailUrl(thumbnail)
                .visitDate(visitDate)
                .title(title)
                .bakeryName(bakeryName)
                .content(content)
                .memberId(memberId)
                .build();
    }
}

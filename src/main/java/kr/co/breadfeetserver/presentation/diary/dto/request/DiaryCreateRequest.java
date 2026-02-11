package kr.co.breadfeetserver.presentation.diary.dto.request;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryCreateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential AddressJpaVO address,
        @IsEssential String thumbnail,
        LocalDateTime visitDate,
        String content,
        List<String> hashtags,
        List<String> pictureUrls
) {
    public Diary toEntity(Long memberId){
        return Diary.builder()
                .isPublic(isPublic)
                .address(address)
                .thumbnailUrl(thumbnail)
                .visitDate(visitDate)
                .content(content)
                .memberId(memberId)
                .build();
    }
}

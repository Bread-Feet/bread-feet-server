package kr.co.breadfeetserver.presentation.diary.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

public record DiaryCreateRequest(
        @IsEssential Boolean isPublic,
        @IsEssential AddressJpaVO address,
        @IsEssential String thumbnail,
        @IsEssential String title,
        @IsEssential Long bakeryId,
        LocalDateTime visitDate,
        String content,
        List<String> hashtags,
        List<String> pictureUrls
) {

    public Diary toEntity(Long memberId) {
        return Diary.builder()
                .isPublic(isPublic)
                .address(address)
                .thumbnailUrl(thumbnail)
                .visitDate(visitDate)
                .title(title)
                .bakeryId(bakeryId)
                .content(content)
                .memberId(memberId)
                .build();
    }
}

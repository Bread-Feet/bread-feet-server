package kr.co.breadfeetserver.presentation.diary.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.domain.diary.Diary;
import lombok.Builder;

@Builder
public record DiaryResponse(
        Long id,
        AddressJpaVO address,
        String thumbnailUrl,
        Integer score,
        Boolean isPublic,
        LocalDateTime visitDate,
        String content,
        Long memberId,
        Long bakeryId,
        List<String> hashtags,
        List<String> pictureUrls
) {

    public static DiaryResponse from(Diary diary, List<String> hashtags, List<String> pictureUrls) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .address(diary.getAddress())
                .thumbnailUrl(diary.getThumbnailUrl())
                .score(diary.getScore())
                .isPublic(diary.getIsPublic())
                .visitDate(diary.getVisitDate())
                .content(diary.getContent())
                .memberId(diary.getMemberId())
                .bakeryId(diary.getBakeryId())
                .hashtags(hashtags)
                .pictureUrls(pictureUrls)
                .build();
    }
}
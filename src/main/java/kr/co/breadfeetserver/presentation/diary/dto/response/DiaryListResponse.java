package kr.co.breadfeetserver.presentation.diary.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.global.dto.Cursorable;

public record DiaryListResponse(
        Long id,
        String thumbnailUrl,
        Boolean isPublic,
        LocalDateTime visitDate,
        String content,
        Long memberId,
        Long bakeryId,
        List<String> hashtags,
        List<String> pictureUrls
) implements Cursorable {

    public static DiaryListResponse from(Diary diary, List<String> hashtags, List<String> pictureUrls) {
        return new DiaryListResponse(
                diary.getId(),
                diary.getThumbnailUrl(),
                diary.getIsPublic(),
                diary.getVisitDate(),
                diary.getContent(),
                diary.getMemberId(),
                diary.getBakeryId(),
                hashtags,
                pictureUrls
        );
    }

    @Override
    public Long getCursorId() {
        return this.id;
    }
}
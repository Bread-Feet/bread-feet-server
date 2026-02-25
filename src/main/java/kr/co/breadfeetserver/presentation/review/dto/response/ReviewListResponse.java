package kr.co.breadfeetserver.presentation.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.breadfeetserver.global.dto.Cursorable;

public record ReviewListResponse(
        Long reviewId,
        String content,
        double rating,
        long likeCount,
        boolean isLiked,
        boolean isMyReview,
        String nickname,
        List<String> reviewPictureUrls,
        LocalDateTime createdAt
) implements Cursorable {

    @Override
    public Long getCursorId() {
        return this.reviewId;
    }
}

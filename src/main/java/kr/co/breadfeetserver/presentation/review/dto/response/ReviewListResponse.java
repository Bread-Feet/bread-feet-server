package kr.co.breadfeetserver.presentation.review.dto.response;

import kr.co.breadfeetserver.global.dto.Cursorable;

import java.time.LocalDateTime;

public record ReviewListResponse(
        Long reviewId,
        String content,
        double rating,
        long likeCount,
        boolean isLiked,
        boolean isMyReview,
        String nickname,
        String thumbnailUrl,
        LocalDateTime createdAt
) implements Cursorable {
    @Override
    public Long getCursorId() {
        return this.reviewId;
    }
}

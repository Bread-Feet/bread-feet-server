package kr.co.breadfeetserver.presentation.review.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewGetResponse(
        Long reviewId,
        String content,
        double rating,
        long likeCount,
        boolean isLiked,
        LocalDateTime createdAt,
        Long memberId,
        String nickname,
        List<String> pictureUrls
) {
    public static ReviewGetResponse from(Long reviewId, String content, double rating, long likeCount, boolean isLiked,
                                         LocalDateTime createdAt, Long memberId, String nickname, List<String> pictureUrls) {
        return ReviewGetResponse.builder()
                .reviewId(reviewId)
                .content(content)
                .rating(rating)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .createdAt(createdAt)
                .memberId(memberId)
                .nickname(nickname)
                .pictureUrls(pictureUrls)
                .build();
    }
}

package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.NotNull;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

public record ReviewUpdateRequest(
        @IsEssential long reviewId,
        @IsEssential long bakeryId,
        @IsEssential String content,
        double rating
) {
    public Review toEntity(Long memberId){
        return Review.builder()
                .bakeryId(bakeryId)
                .content(content)
                .rating(rating)
                .memberId(memberId)
                .build();
    }
}

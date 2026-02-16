package kr.co.breadfeetserver.presentation.reviewlike.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.breadfeetserver.domain.reviewlike.ReviewLike;

@Schema(description = "좋아요 생성 요청 DTO")
public record ReviewLikeCreateRequest(
        @Schema(description = "리뷰 ID", example = "1")
        @NotNull
        Long reviewId
) {
    public ReviewLike toEntity(Long memberId){
        return ReviewLike.builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .build();
    }
}



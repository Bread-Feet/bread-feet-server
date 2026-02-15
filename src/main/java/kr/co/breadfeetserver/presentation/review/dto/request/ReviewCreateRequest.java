package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

import java.util.List;

public record ReviewCreateRequest(
        @NotNull Long bakeryId,
        @IsEssential String content,
        @Min(0) @Max(5)
        double rating,
        List<String> reviewpictureUrls
){
    public Review toEntity(Long memberId){
        return Review.builder()
                .bakeryId(bakeryId)
                .content(content)
                .rating(rating)
                .memberId(memberId)
                .build();
    }
}
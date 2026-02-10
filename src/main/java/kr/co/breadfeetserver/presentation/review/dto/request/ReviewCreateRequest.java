package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.co.breadfeetserver.domain.diary.Diary;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ReviewCreateRequest(
        @NotNull Long bakeryId,
        @IsEssential String content,
        double rating
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
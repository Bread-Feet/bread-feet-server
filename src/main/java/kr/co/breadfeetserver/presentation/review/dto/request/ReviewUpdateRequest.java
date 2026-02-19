package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

public record ReviewUpdateRequest(
        @IsEssential String content,
        @Min(0) @Max(5) double rating
) {
}

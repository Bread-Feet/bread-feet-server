package kr.co.breadfeetserver.presentation.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;

import java.util.List;

public record ReviewUpdateRequest(
        @IsEssential String content,
        @Min(0) @Max(5) double rating,
        List<String> reviewPictureUrls
) {
}

package kr.co.breadfeetserver.presentation.review.dto.request;

import kr.co.breadfeetserver.presentation.annotation.IsEssential;

public record ReviewUpdateRequest(
        @IsEssential String content,
        double rating
) {
}

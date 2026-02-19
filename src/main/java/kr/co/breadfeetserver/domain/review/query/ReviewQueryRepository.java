package kr.co.breadfeetserver.domain.review.query;

import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCursorCommand;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewGetResponse;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewListResponse;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface ReviewQueryRepository {
    Optional<ReviewGetResponse> findById(Long reviewId, Long memberId);

    Slice<ReviewListResponse> findByBakeryId(Long bakeryId, Long memberId, ReviewCursorCommand command);
}

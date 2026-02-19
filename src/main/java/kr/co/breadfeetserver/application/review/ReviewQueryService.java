package kr.co.breadfeetserver.application.review;

import kr.co.breadfeetserver.application.support.CursorService;
import kr.co.breadfeetserver.domain.review.query.ReviewQueryRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCursorCommand;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewGetResponse;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewListResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;
    private final CursorService cursorService;

    public ReviewGetResponse getReview(Long reviewId, Long memberId) {
        return reviewQueryRepository.findById(reviewId, memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public CursorResponse<ReviewListResponse> getReviewsByBakery(Long bakeryId, Long memberId, ReviewCursorCommand command) {
        Slice<ReviewListResponse> reviews = reviewQueryRepository.findByBakeryId(bakeryId, memberId, command);
        return cursorService.getCursorResponse(reviews);
    }
}

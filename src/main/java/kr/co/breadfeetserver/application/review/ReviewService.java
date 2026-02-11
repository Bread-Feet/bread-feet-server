package kr.co.breadfeetserver.application.review;

import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.domain.review.ReviewJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public Long createReview(Long memberId,ReviewCreateRequest request) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));

        return reviewJpaRepository.save(request.toEntity(memberId)).getId();
    }

    public void updateReview(long memberId, Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if(review.getMemberId() != memberId){
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        reviewUpdateRequestToEntity(review, request);
    }

    public void deleteReview(long memberId, Long reviewId) {
        Review review = reviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMemberId().equals(memberId)) {
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        reviewJpaRepository.delete(review);
    }

    private void reviewUpdateRequestToEntity(Review review, ReviewUpdateRequest request) {
        review.updateReview(
                request.content(),
                request.rating()
        );
    }
}

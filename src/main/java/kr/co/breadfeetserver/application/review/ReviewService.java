package kr.co.breadfeetserver.application.review;

import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.domain.review.ReviewJpaRepository;
import kr.co.breadfeetserver.domain.review.ReviewPictureUrl;
import kr.co.breadfeetserver.domain.review.ReviewPictureUrlJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ReviewPictureUrlJpaRepository reviewPictureUrlJpaRepository;

    public Long createReview(Long memberId,ReviewCreateRequest request) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewJpaRepository.save(request.toEntity(memberId));
        Long reviewId = review.getId();

        if (!CollectionUtils.isEmpty(request.reviewpictureUrls())) {
            request.reviewpictureUrls().forEach(pictureUrl ->
                    reviewPictureUrlJpaRepository.save(
                            ReviewPictureUrl.builder()
                                    .pic_url(pictureUrl)
                                    .reviewId(reviewId)
                                    .build()
                    )
            );
        }

        return reviewId;
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

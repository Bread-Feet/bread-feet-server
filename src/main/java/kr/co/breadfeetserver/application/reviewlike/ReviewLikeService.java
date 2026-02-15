package kr.co.breadfeetserver.application.reviewlike;

import kr.co.breadfeetserver.domain.reviewlike.ReviewLike;
import kr.co.breadfeetserver.domain.reviewlike.ReviewLikeJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.reviewlike.dto.request.ReviewLikeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikeJpaRepository reviewlikeJpaRepository;

    @Transactional
    public void createLike(Long memberId, ReviewLikeCreateRequest request) {
        try {
            ReviewLike reviewlike = request.toEntity(memberId);
            reviewlikeJpaRepository.save(reviewlike);
        } catch (DataIntegrityViolationException e) {
            throw new BreadFeetBusinessException(ErrorCode.REVIEW_LIKE_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void deleteLike(Long memberId, Long reviewId) {
        reviewlikeJpaRepository.deleteByMemberIdAndReviewId(memberId, reviewId);
    }
}

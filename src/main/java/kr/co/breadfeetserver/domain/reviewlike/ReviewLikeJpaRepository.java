package kr.co.breadfeetserver.domain.reviewlike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeJpaRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberIdAndReviewId(Long memberId, Long reviewId);
    void deleteByMemberIdAndReviewId(Long memberId, Long reviewId);
    boolean existsByMemberIdAndReviewId(Long memberId, Long reviewId);
}

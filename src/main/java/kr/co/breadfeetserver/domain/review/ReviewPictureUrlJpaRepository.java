package kr.co.breadfeetserver.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewPictureUrlJpaRepository extends JpaRepository<ReviewPictureUrl, Long> {
    List<ReviewPictureUrl> findAllByReviewId(Long reviewId);

    @Modifying
    @Query("DELETE FROM ReviewPictureUrl rp WHERE rp.reviewId = :reviewId")
    void deleteAllByReviewId(Long reviewId);
}

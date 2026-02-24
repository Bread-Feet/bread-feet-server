package kr.co.breadfeetserver.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    Long countByBakeryId(Long bakeryId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.bakeryId = :bakeryId")
    Double findAverageRatingByBakeryId(@Param("bakeryId") Long bakeryId);
}

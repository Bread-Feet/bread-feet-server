package kr.co.breadfeetserver.domain.reviewlike;

import jakarta.persistence.*;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes",
        indexes = {
                @Index(name = "idx_likes_review_id", columnList = "review_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_likes_member_post", columnNames = {"member_id", "review_id"})
        })
public class ReviewLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "member_id")
    private Long memberId;

    @Builder
    public ReviewLike(Long reviewId, Long memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
    }
}

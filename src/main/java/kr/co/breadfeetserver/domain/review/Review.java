package kr.co.breadfeetserver.domain.review;

import jakarta.persistence.*;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double rating;

    @Column(name = "bakery_id")
    private Long bakeryId;

    @Column(name = "member_id")
    private Long memberId;

    public void updateReview(
            String content,
            double rating
    ) {
        this.content = content;
        this.rating = rating;
    }
}

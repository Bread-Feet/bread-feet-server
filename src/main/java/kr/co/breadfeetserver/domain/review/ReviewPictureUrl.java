package kr.co.breadfeetserver.domain.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "reviewpictureUrl")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewPictureUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pic_url;

    @Column(nullable = false)
    private Long reviewId;
}

package kr.co.breadfeetserver.application.review;

import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.domain.review.ReviewJpaRepository;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewJpaRepository reviewJpaRepository;

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("리뷰를 생성한다.")
    @Test
    void createReview() {
        // given
        ReviewCreateRequest request = new ReviewCreateRequest(1L, "맛있어요", 5.0);
        Long memberId = 1L;

        // Mocking member existence
        Member mockMember = Member.builder()
                .id(memberId)
                .build();
        when(memberJpaRepository.findById(memberId)).thenReturn(Optional.of(mockMember));

        Review savedReview = Review.builder()
                .id(1L)
                .content(request.content()) // record field access
                .rating(request.rating())   // record field access
                .bakeryId(request.bakeryId()) // record field access
                .memberId(memberId)
                .build();

        when(reviewJpaRepository.save(any(Review.class))).thenReturn(savedReview);

        // when
        Long reviewId = reviewService.createReview(memberId, request); // Corrected argument order

        // then
        assertThat(reviewId).isEqualTo(1L);
    }
}
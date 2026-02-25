package kr.co.breadfeetserver.application.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.domain.review.ReviewJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService 단위 테스트")
class ReviewServiceTest {

    @Mock
    private ReviewJpaRepository reviewJpaRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private ReviewUpdateRequest updateRequest;
    private long memberId = 1L;
    private long reviewId = 1L;

    @BeforeEach
    void setUp() {
        review = Review.builder()
                .id(reviewId)
                .memberId(memberId)
                .bakeryId(1L)
                .content("Original content")
                .rating(4.0)
                .build();

        updateRequest = new ReviewUpdateRequest(
                "Updated content",
                5.0,
                List.of(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg"
                )
        );
    }

    @Test
    @DisplayName("리뷰_수정_성공")
    void 리뷰_수정_성공() {
        // Given
        given(reviewJpaRepository.findById(anyLong())).willReturn(Optional.of(review));

        // When
        reviewService.updateReview(memberId, reviewId, updateRequest);

        // Then
        assertThat(review.getContent()).isEqualTo(updateRequest.content());
        assertThat(review.getRating()).isEqualTo(updateRequest.rating());
    }

    @Test
    @DisplayName("리뷰_수정_실패_리뷰_없음")
    void 리뷰_수정_실패_리뷰_없음() {
        // Given
        given(reviewJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reviewService.updateReview(memberId, reviewId, updateRequest))
                .isInstanceOf(BreadFeetBusinessException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_FOUND.getMessage());

        then(reviewJpaRepository).should().findById(reviewId);

    }

    @Test
    @DisplayName("리뷰_수정_실패_권한_없음")
    void 리뷰_수정_실패_권한_없음() {
        // Given
        long anotherMemberId = 2L;
        given(reviewJpaRepository.findById(anyLong())).willReturn(Optional.of(review));

        // When & Then
        assertThatThrownBy(
                () -> reviewService.updateReview(anotherMemberId, reviewId, updateRequest))
                .isInstanceOf(BreadFeetBusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_ACCESS_FORBIDDEN.getMessage());
    }
}

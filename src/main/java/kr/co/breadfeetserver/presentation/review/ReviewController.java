package kr.co.breadfeetserver.presentation.review;

import jakarta.validation.Valid;
import kr.co.breadfeetserver.application.review.ReviewService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCreateRequest;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryUpdateRequest;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<Long>> createReview(
            @RequestBody ReviewCreateRequest request,
            @Memberid Long memberId) {
        Long reviewId = reviewService.createReview(memberId,request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(HttpStatus.CREATED, reviewId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponseWrapper<Void>> updateReview(
            @Memberid Long memberId,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request) {
        reviewService.updateReview(memberId, reviewId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "리뷰 수정 성공"));
    }
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteReview(
            @Memberid Long memberId,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(memberId, reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "리뷰 삭제 성공"));
    }
}

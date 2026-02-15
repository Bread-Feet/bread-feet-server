package kr.co.breadfeetserver.presentation.reviewlike;

import jakarta.validation.Valid;
import kr.co.breadfeetserver.application.reviewlike.ReviewLikeService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.reviewlike.dto.request.ReviewLikeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakery/reviews")
public class ReviewLikeController {
    private final ReviewLikeService reviewlikeService;

    @PostMapping("/likes")
    public ResponseEntity<ApiResponseWrapper<Void>> createLike(
            @Memberid Long memberId,
            @Valid @RequestBody ReviewLikeCreateRequest request) {
        reviewlikeService.createLike(memberId, request);
        return ResponseEntity.ok(ApiResponseWrapper.success(HttpStatus.CREATED, "좋아요 등록 성공"));
    }

    @DeleteMapping("/{reviewId}/likes")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteLike(
            @Memberid Long memberId,
            @PathVariable Long reviewId) {
        reviewlikeService.deleteLike(memberId, reviewId);
        return ResponseEntity.ok(ApiResponseWrapper.success(HttpStatus.OK, "좋아요 삭제 성공"));
    }
}

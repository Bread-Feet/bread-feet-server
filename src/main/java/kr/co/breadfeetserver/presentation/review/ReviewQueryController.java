package kr.co.breadfeetserver.presentation.review;

import kr.co.breadfeetserver.application.review.ReviewQueryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCursorCommand;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewListResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakery")
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/{bakeryId}/review")
    public ResponseEntity<ApiResponseWrapper<CursorResponse<ReviewListResponse>>> getBakeryReviews(
            @PathVariable Long bakeryId,
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @Memberid(required = false) Long memberId
    ) {
        ReviewCursorCommand command = new ReviewCursorCommand(cursor, size);
        CursorResponse<ReviewListResponse> response = reviewQueryService.getReviewsByBakery(bakeryId, memberId, command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 리뷰 목록 조회 성공",
                        response));
    }
}
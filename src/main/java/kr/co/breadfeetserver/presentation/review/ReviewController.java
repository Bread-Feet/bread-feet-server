package kr.co.breadfeetserver.presentation.review;

import jakarta.validation.Valid;
import kr.co.breadfeetserver.application.review.ReviewService;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createReview(@RequestBody @Valid ReviewCreateRequest request, @Memberid Long memberId) {
        return reviewService.createReview(memberId, request);
    }
}

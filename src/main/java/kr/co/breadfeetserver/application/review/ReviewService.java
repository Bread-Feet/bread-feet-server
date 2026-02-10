package kr.co.breadfeetserver.application.review;

import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.domain.review.Review;
import kr.co.breadfeetserver.domain.review.ReviewJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public Long createReview(Long memberId,ReviewCreateRequest request) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND));

        return reviewJpaRepository.save(request.toEntity(memberId)).getId();
    }
}

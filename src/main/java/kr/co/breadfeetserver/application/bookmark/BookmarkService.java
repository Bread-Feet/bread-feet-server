package kr.co.breadfeetserver.application.bookmark;

import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.domain.bookmark.Bookmark;
import kr.co.breadfeetserver.domain.bookmark.BookmarkJpaRepository;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final MemberJpaRepository memberJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final BakeryJpaRepository bakeryJpaRepository;

    public boolean isBookmarked(Long bakeryId, Long memberId) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND)
        );

        return bookmarkJpaRepository.existsByBakeryIdAndMemberId(bakeryId, member.getId());
    }

    public void bookmark(Long memberId, Long bakeryId) {
        memberJpaRepository.findById(memberId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.USER_NOT_FOUND)
        );

        bakeryJpaRepository.findById(bakeryId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND)
        );

        if (bookmarkJpaRepository.existsByBakeryIdAndMemberId(bakeryId, memberId)) {
            throw new BreadFeetBusinessException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        Bookmark bookmark = Bookmark.builder()
                .bakeryId(bakeryId)
                .memberId(memberId)
                .build();

        bookmarkJpaRepository.save(bookmark);
    }

    public void unbookmark(Long memberId, Long bakeryId) {
        Bookmark bookmark = bookmarkJpaRepository.findByMemberIdAndBakeryId(memberId, bakeryId).orElseThrow(
                () -> new BreadFeetBusinessException(ErrorCode.BOOKMARK_NOT_FOUND)
        );

        bookmarkJpaRepository.delete(bookmark);
    }

}

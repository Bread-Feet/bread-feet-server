package kr.co.breadfeetserver.application.bookmark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.domain.bookmark.Bookmark;
import kr.co.breadfeetserver.domain.bookmark.BookmarkJpaRepository;
import kr.co.breadfeetserver.domain.member.Member;
import kr.co.breadfeetserver.domain.member.MemberJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkService 단위 테스트")
class BookmarkServiceTest {

    @Mock
    private MemberJpaRepository memberJpaRepository;
    @Mock
    private BookmarkJpaRepository bookmarkJpaRepository;

    @Mock
    private BakeryJpaRepository bakeryJpaRepository;

    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        bookmarkService = new BookmarkService(
                memberJpaRepository,
                bookmarkJpaRepository,
                bakeryJpaRepository
        );
    }

    @Test
    @DisplayName("사용자는_특정_빵집의_북마크_여부를_확인할_수_있다")
    void 사용자는_특정_빵집의_북마크_여부를_확인할_수_있다() {
        // Given
        Long bakeryId = 1L;
        Long memberId = 1L;
        given(memberJpaRepository.findById(memberId)).willReturn(
                Optional.of(Member.builder().id(memberId).build()));
        given(bookmarkJpaRepository.existsByBakeryIdAndMemberId(bakeryId, memberId)).willReturn(
                true);

        // When
        boolean isBookmarked = bookmarkService.isBookmarked(bakeryId, memberId);

        // Then
        assertThat(isBookmarked).isTrue();
        verify(memberJpaRepository).findById(memberId);
        verify(bookmarkJpaRepository).existsByBakeryIdAndMemberId(bakeryId, memberId);
    }

    @Test
    @DisplayName("존재하지_않는_회원의_북마크_여부_확인_시_예외_발생")
    void 존재하지_않는_회원의_북마크_여부_확인_시_예외_발생() {
        // Given
        Long bakeryId = 1L;
        Long memberId = 1L;
        given(memberJpaRepository.findById(memberId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(BreadFeetBusinessException.class,
                () -> bookmarkService.isBookmarked(bakeryId, memberId));
        verify(memberJpaRepository).findById(memberId);
    }


    @Test
    @DisplayName("사용자는_빵집을_북마크할_수_있다")
    void 사용자는_빵집을_북마크할_수_있다() {
        // Given
        Long memberId = 1L;
        Long bakeryId = 1L;
        given(memberJpaRepository.findById(memberId)).willReturn(
                Optional.of(Member.builder().id(memberId).build()));
        given(bakeryJpaRepository.findById(bakeryId)).willReturn(
                Optional.of(Bakery.builder().id(bakeryId).build()));
        given(bookmarkJpaRepository.existsByBakeryIdAndMemberId(bakeryId, memberId)).willReturn(
                false);

        // When
        bookmarkService.bookmark(memberId, bakeryId);

        // Then
        verify(memberJpaRepository).findById(memberId);
        verify(bookmarkJpaRepository).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("존재하지_않는_회원이_빵집을_북마크_시도_시_예외_발생")
    void 존재하지_않는_회원이_빵집을_북마크_시도_시_예외_발생() {
        // Given
        Long memberId = 1L;
        Long bakeryId = 1L;
        given(memberJpaRepository.findById(memberId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(BreadFeetBusinessException.class,
                () -> bookmarkService.bookmark(memberId, bakeryId));
        verify(memberJpaRepository).findById(memberId);
    }

    @Test
    @DisplayName("사용자는_빵집_북마크를_취소할_수_있다")
    void 사용자는_빵집_북마크를_취소할_수_있다() {
        // Given
        Long memberId = 1L;
        Long bakeryId = 1L;
        Bookmark bookmark = Bookmark.builder().id(1L).bakeryId(bakeryId).memberId(memberId)
                .build();
        given(bookmarkJpaRepository.findByMemberIdAndBakeryId(memberId, bakeryId)).willReturn(
                Optional.of(bookmark));

        // When
        bookmarkService.unbookmark(memberId, bakeryId);

        // Then
        verify(bookmarkJpaRepository).findByMemberIdAndBakeryId(memberId, bakeryId);
        verify(bookmarkJpaRepository).delete(bookmark);
    }

    @Test
    @DisplayName("존재하지_않는_북마크_취소_시도_시_예외_발생")
    void 존재하지_않는_북마크_취소_시도_시_예외_발생() {
        // Given
        Long memberId = 1L;
        Long bakeryId = 1L;
        given(bookmarkJpaRepository.findByMemberIdAndBakeryId(memberId, bakeryId)).willReturn(
                Optional.empty());

        // When & Then
        assertThrows(BreadFeetBusinessException.class,
                () -> bookmarkService.unbookmark(memberId, bakeryId));
        verify(bookmarkJpaRepository).findByMemberIdAndBakeryId(memberId, bakeryId);
    }
}

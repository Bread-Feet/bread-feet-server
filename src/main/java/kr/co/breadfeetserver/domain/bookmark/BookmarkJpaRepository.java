package kr.co.breadfeetserver.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByBakeryIdAndMemberId(Long bakeryId, Long memberId);
}

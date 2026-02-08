package kr.co.breadfeetserver.domain.bookmark;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByBakeryIdAndMemberId(Long bakeryId, Long memberId);

    Optional<Bookmark> findByMemberIdAndBakeryId(Long memberId, Long bakeryId);
}

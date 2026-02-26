package kr.co.breadfeetserver.domain.diary;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByIdAndMemberId(Long diaryId, Long memberId);

    Optional<Diary> findByMemberIdAndVisitDateBetween(Long memberId, LocalDateTime start,
            LocalDateTime end);
}

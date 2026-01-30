package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByIdAndMemberId(Long diaryId, Long memberId);
}

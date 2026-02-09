package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HashtagJpaRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByDiaryId(Long diaryId);

    @Modifying
    @Query("DELETE FROM Hashtag h WHERE h.diaryId = :diaryId")
    void deleteAllByDiaryId(Long diaryId);
}

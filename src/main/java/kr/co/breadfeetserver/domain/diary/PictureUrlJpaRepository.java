package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PictureUrlJpaRepository extends JpaRepository<PictureUrl, Long> {
    List<PictureUrl> findAllByDiaryId(Long diaryId);

    @Modifying
    @Query("DELETE FROM PictureUrl p WHERE p.diaryId = :diaryId")
    void deleteAllByDiaryId(Long diaryId);
}

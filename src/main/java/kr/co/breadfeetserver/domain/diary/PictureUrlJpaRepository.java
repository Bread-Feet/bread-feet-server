package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureUrlJpaRepository extends JpaRepository<PictureUrl, Long> {
    List<PictureUrl> findAllByDiaryId(Long diaryId);
}

package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagJpaRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByDiaryId(Long diaryId);
}

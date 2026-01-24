package kr.co.breadfeetserver.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
}

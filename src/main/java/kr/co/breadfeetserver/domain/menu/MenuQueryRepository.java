package kr.co.breadfeetserver.domain.menu;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface MenuQueryRepository extends MenuJpaRepository {


    @Query("select m from Menu m where m.bakeryId = :bakeryId")
    List<Menu> findByBakeryId(Long bakeryId);
}

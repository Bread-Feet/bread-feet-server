package kr.co.breadfeetserver.domain.menu;

import java.util.List;

public interface MenuQueryRepository extends MenuJpaRepository {


    List<Menu> findByBakeryId(Long bakeryId);
}

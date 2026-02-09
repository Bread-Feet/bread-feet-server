package kr.co.breadfeetserver.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Query("delete from Menu m where m.bakeryId = :bakeryId")
    void deleteAllByBakeryId(Long bakeryId);
}
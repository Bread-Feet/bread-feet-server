package kr.co.breadfeetserver.domain.bakery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BakeryJpaRepository extends JpaRepository<Bakery, Long> {


    boolean existsByAddress_LotNumber(String addressLotNumber);
}

package kr.co.breadfeetserver.application.bakery;

import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {

    private final BakeryJpaRepository bakeryJpaRepository;

    public Long createBakery(long memberId, BakeryCreateRequest request) {
        if (bakeryJpaRepository.existsByAddress_LotNumber(request.address().lotNumber())) {
            throw new BreadFeetBusinessException(ErrorCode.BAKERY_ALREADY_EXISTS);
        }

        return bakeryJpaRepository.save(request.toEntity(memberId)).getId();
    }

    public void updateBakery(long memberId, Long bakeryId, BakeryUpdateRequest request) {
        Bakery bakery = bakeryJpaRepository.findById(bakeryId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND));

        bakeryUpdateRequestToEntity(bakery, request);
    }

    public void deleteBakery(long memberId, Long bakeryId) {
        Bakery bakery = bakeryJpaRepository.findById(bakeryId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND));

        if (!bakery.getMemberId().equals(memberId)) {
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        bakeryJpaRepository.delete(bakery);
    }

    private void bakeryUpdateRequestToEntity(Bakery bakery, BakeryUpdateRequest request) {
        bakery.updateBakery(
                request.name(),
                request.address().toEntity(),
                request.imageUrl(),
                request.phoneNumber(),
                request.businessHours(),
                request.bestBread(),
                request.xCoordinate(),
                request.yCoordinate()
        );
    }
}

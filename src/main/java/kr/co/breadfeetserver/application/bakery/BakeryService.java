package kr.co.breadfeetserver.application.bakery;

import kr.co.breadfeetserver.application.menu.MenuCreateCommand;
import kr.co.breadfeetserver.application.menu.MenuService;
import kr.co.breadfeetserver.application.menu.MenuUpdateCommand;
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
    private final MenuService menuService;

    @Transactional
    public void createBakery(long memberId, BakeryCreateRequest request) {
        if (bakeryJpaRepository.existsByAddress_LotNumber(request.address().lotNumber())) {
            throw new BreadFeetBusinessException(ErrorCode.BAKERY_ALREADY_EXISTS);
        }

        final Bakery bakery = bakeryJpaRepository.save(request.toEntity(memberId));

        menuService.createMenu(new MenuCreateCommand(bakery.getId(), request.menus()));
    }

    public void updateBakery(long memberId, BakeryUpdateRequest request) {
        Bakery bakery = bakeryJpaRepository.findById(request.bakeryId())
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND));

        if (!bakery.getMemberId().equals(memberId)) {
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        bakeryUpdateRequestToEntity(bakery, request);

        menuService.updateMenu(new MenuUpdateCommand(request.bakeryId(), request.menus()));
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
                request.isDrink(),
                request.isEatIn(),
                request.isWaiting(),
                request.isParking()
        );
    }
}

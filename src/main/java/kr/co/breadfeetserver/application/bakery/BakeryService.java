package kr.co.breadfeetserver.application.bakery;

import kr.co.breadfeetserver.application.menu.MenuCreateCommand;
import kr.co.breadfeetserver.application.menu.MenuService;
import kr.co.breadfeetserver.application.menu.MenuUpdateCommand;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.infra.geocoding.KakaoGeocodingClient;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {

    private final BakeryJpaRepository bakeryJpaRepository;
    private final MenuService menuService;
    private final KakaoGeocodingClient kakaoGeocodingClient;

    @Transactional
    public void createBakery(long memberId, BakeryCreateRequest request) {
        if (bakeryJpaRepository.existsByAddress_LotNumber(request.address().lotNumber())) {
            throw new BreadFeetBusinessException(ErrorCode.BAKERY_ALREADY_EXISTS);
        }

        // save() 이전에 좌표를 조회해 INSERT 한 번으로 처리한다.
        // save() 이후에 조회하면 INSERT → UPDATE 두 번 발생하고,
        // dirty checking 타이밍에 따라 좌표가 누락될 수 있다.
        Bakery bakery = request.toEntity(memberId);
        geocodeAndApply(bakery, request.address().lotNumber());

        bakeryJpaRepository.save(bakery);
        menuService.createMenu(new MenuCreateCommand(bakery.getId(), request.menus()));
    }

    public void updateBakery(long memberId, BakeryUpdateRequest request) {
        Bakery bakery = bakeryJpaRepository.findById(request.bakeryId())
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND));

        if (!bakery.getMemberId().equals(memberId)) {
            throw new BreadFeetBusinessException(ErrorCode.USER_NOT_ACCESS_FORBIDDEN);
        }

        bakeryUpdateRequestToEntity(bakery, request);

        // 주소가 변경될 수 있으므로 수정 시에도 좌표를 다시 조회한다.
        // 변환 실패 시 기존 좌표를 유지한다 (geocoding 일시 장애로 정상 좌표를 잃지 않도록).
        geocodeAndApply(bakery, request.address().lotNumber());

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

    // ========== private ==========

    /**
     * 지번 주소를 카카오 Geocoding API로 변환해 Bakery 엔티티에 좌표를 반영한다.
     *
     * <p>변환 성공 시 좌표를 설정하고, 실패 시 기존 좌표를 그대로 유지한다.
     * create 시에는 save() 이전에 호출해 INSERT 한 번으로 처리해야 한다.
     *
     * @param bakery    좌표를 반영할 빵집 엔티티
     * @param lotNumber 변환할 지번 주소
     */
    private void geocodeAndApply(Bakery bakery, String lotNumber) {
        kakaoGeocodingClient.geocode(lotNumber)
                .ifPresentOrElse(
                        result -> {
                            bakery.updateCoordinates(result.x(), result.y());
                            log.debug("좌표 설정 완료. bakeryId={}, x={}, y={}",
                                    bakery.getId(), result.x(), result.y());
                        },
                        () -> log.warn("좌표 변환 실패 — 기존 좌표 유지. bakeryId={}, lotNumber={}",
                                bakery.getId(), lotNumber)
                );
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

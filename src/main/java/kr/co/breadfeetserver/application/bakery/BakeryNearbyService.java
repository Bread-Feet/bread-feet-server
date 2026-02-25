package kr.co.breadfeetserver.application.bakery;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.BakeryNearbyQueryRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 위치 기반 근처 빵집 조회 서비스.
 *
 * <p>DB에 좌표(x_coordinate, y_coordinate)가 저장된 빵집만 조회 대상이다.
 * 좌표가 없는 빵집은 Repository 레벨에서 필터링된다.
 *
 * <p>좌표 미등록 빵집의 좌표를 채우려면 {@link kr.co.breadfeetserver.infra.geocoding.KakaoGeocodingClient}를
 * 활용해 lot_number(지번 주소)를 변환한 뒤 Bakery 엔티티를 업데이트해야 한다.
 * (현재 서비스는 조회 전용이므로 변환 로직은 포함하지 않는다.)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BakeryNearbyService {

    private final BakeryNearbyQueryRepository bakeryNearbyQueryRepository;

    /**
     * 사용자 위치(x, y)로부터 2km 이내의 빵집 목록을 거리 오름차순으로 반환한다.
     *
     * @param userX 사용자 경도 (longitude, WGS84)
     * @param userY 사용자 위도 (latitude, WGS84)
     * @return 거리 오름차순으로 정렬된 근처 빵집 목록 (빈 리스트 가능)
     */
    public List<NearbyBakeryResponse> getNearbyBakeries(Double userX, Double userY) {
        return bakeryNearbyQueryRepository.findNearby(userX, userY);
    }
}

package kr.co.breadfeetserver.domain.bakery.query;

import java.util.List;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;

/**
 * 사용자 위경도를 기준으로 반경 내 빵집을 조회하는 Nearby 전용 Repository 인터페이스.
 *
 * <p>기존 {@link BakeryJdbcRepository}와 분리한 이유:
 * Nearby 조회는 커서 기반 페이징이 아닌 반경 필터링을 사용하므로
 * 별도 인터페이스로 관리해 역할을 명확히 한다.
 */
public interface BakeryNearbyQueryRepository {

    /**
     * 사용자 위경도(x, y)로부터 2km 이내의 빵집을 거리 오름차순으로 조회한다.
     *
     * <p>좌표(x_coordinate, y_coordinate)가 없는 빵집은 결과에서 제외된다.
     *
     * @param userX 사용자 경도 (longitude, WGS84)
     * @param userY 사용자 위도 (latitude, WGS84)
     * @return 거리 오름차순 정렬된 근처 빵집 목록
     */
    List<NearbyBakeryResponse> findNearby(Double userX, Double userY);
}

package kr.co.breadfeetserver.domain.bakery.query;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryNearbyRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * MySQL ST_Distance_Sphere를 이용한 반경 내 빵집 조회 구현체.
 *
 * <h3>ST_Distance_Sphere 함수 주의사항</h3>
 * <ul>
 *   <li>인자 순서: {@code POINT(경도, 위도)} — 위도·경도 순서가 아님</li>
 *   <li>반환값: 두 지점 사이의 구면 거리 (단위: 미터)</li>
 *   <li>MySQL 5.7.6 이상 지원</li>
 * </ul>
 *
 * <h3>컬럼 매핑</h3>
 * <ul>
 *   <li>{@code b.x_coordinate} = 경도 (longitude)</li>
 *   <li>{@code b.y_coordinate} = 위도 (latitude)</li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
public class BakeryNearbyQueryRepositoryImpl implements BakeryNearbyQueryRepository {

    /** 조회 반경 (미터). 2km */
    private static final double NEARBY_RADIUS_METERS = 2000.0;

    private static final BakeryNearbyRowMapper ROW_MAPPER = new BakeryNearbyRowMapper();

    /**
     * Nearby 조회 쿼리.
     *
     * <p>WHERE 조건 설명:
     * <ul>
     *   <li>{@code deleted_at IS NULL} : soft delete 처리된 빵집 제외</li>
     *   <li>{@code x_coordinate IS NOT NULL AND y_coordinate IS NOT NULL} : 좌표 미등록 빵집 제외</li>
     *   <li>ST_Distance_Sphere <= :radius : 반경 내 빵집만 포함</li>
     * </ul>
     *
     * <p>ST_Distance_Sphere를 WHERE와 SELECT 양쪽에 중복 사용하지 않기 위해
     * HAVING 절을 활용하거나 서브쿼리를 쓸 수도 있으나,
     * MySQL 옵티마이저가 중복 호출을 최적화하므로 가독성 우선으로 직접 작성한다.
     */
    private static final String NEARBY_QUERY = """
            SELECT
                b.bakery_id,
                b.name,
                b.detail,
                b.lot_number,
                b.road_address,
                b.image_url,
                b.x_coordinate,
                b.y_coordinate,
                COUNT(DISTINCT r.review_id)  AS review_count,
                COALESCE(AVG(r.rating), 0.0) AS average_rating,
                ST_Distance_Sphere(
                    POINT(b.x_coordinate, b.y_coordinate),
                    POINT(:userX, :userY)
                ) AS distance
            FROM bakery b
            LEFT JOIN review r ON b.bakery_id = r.bakery_id
            WHERE b.deleted_at IS NULL
              AND b.x_coordinate IS NOT NULL
              AND b.y_coordinate IS NOT NULL
              AND ST_Distance_Sphere(
                    POINT(b.x_coordinate, b.y_coordinate),
                    POINT(:userX, :userY)
                  ) <= :radius
            GROUP BY b.bakery_id, b.x_coordinate, b.y_coordinate
            ORDER BY distance ASC
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<NearbyBakeryResponse> findNearby(Double userX, Double userY) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userX", userX)
                .addValue("userY", userY)
                .addValue("radius", NEARBY_RADIUS_METERS);

        return jdbc.query(NEARBY_QUERY, params, ROW_MAPPER);
    }
}

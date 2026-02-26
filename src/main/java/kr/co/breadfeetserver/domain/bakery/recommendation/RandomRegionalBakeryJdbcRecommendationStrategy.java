package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRegionalRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 지역명(도로명 주소 LIKE)으로 필터링 후 애플리케이션 레이어에서 셔플해 추천하는 전략.
 *
 * <p>사용자 좌표 없이 조회하므로 distance는 null로 반환된다.
 *
 * <h3>ORDER BY RAND() 대신 두 단계 쿼리를 사용하는 이유</h3>
 * <ul>
 *   <li>1단계에서 PK만 조회해 지역 필터를 적용하고 셔플은 Java에서 처리한다.</li>
 *   <li>2단계는 선택된 소수의 PK로만 JOIN·집계하므로 비용이 낮다.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class RandomRegionalBakeryJdbcRecommendationStrategy implements
        BakeryRegionalRecommendationStrategy {

    private static final BakeryRegionalRowMapper ROW_MAPPER = new BakeryRegionalRowMapper();

    /** 1단계: 지역 필터를 적용해 PK만 조회 */
    private static final String CANDIDATE_ID_SQL = """
            SELECT bakery_id FROM bakery
            WHERE deleted_at IS NULL
              AND road_address LIKE :region ESCAPE '\\\\'
            """;

    /** 2단계: 선택된 PK 집합으로 전체 데이터 조회 */
    private static final String SQL_BY_IDS = """
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
                COALESCE(AVG(r.rating), 0.0) AS average_rating
            FROM bakery b
            LEFT JOIN review r ON b.bakery_id = r.bakery_id
            WHERE b.bakery_id IN (:ids)
            GROUP BY b.bakery_id, b.x_coordinate, b.y_coordinate
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<NearbyBakeryResponse> recommend(String region, int size) {
        String escaped = region
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");

        List<Long> regionIds = new ArrayList<>(
                jdbc.queryForList(
                        CANDIDATE_ID_SQL,
                        new MapSqlParameterSource("region", "%" + escaped + "%"),
                        Long.class
                )
        );
        if (regionIds.isEmpty()) {
            return List.of();
        }

        Collections.shuffle(regionIds);
        List<Long> selected = regionIds.subList(0, Math.min(size, regionIds.size()));

        return jdbc.query(SQL_BY_IDS, new MapSqlParameterSource("ids", selected), ROW_MAPPER);
    }
}

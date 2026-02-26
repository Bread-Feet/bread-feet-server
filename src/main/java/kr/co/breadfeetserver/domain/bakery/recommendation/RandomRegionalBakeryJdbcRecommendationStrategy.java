package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRegionalRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 지역명(도로명 주소 LIKE)으로 필터링 후 랜덤으로 추천하는 전략.
 *
 * <p>사용자 좌표 없이 조회하므로 distance는 null로 반환된다.
 */
@Component
@RequiredArgsConstructor
public class RandomRegionalBakeryJdbcRecommendationStrategy implements
        BakeryRegionalRecommendationStrategy {

    private static final BakeryRegionalRowMapper ROW_MAPPER = new BakeryRegionalRowMapper();

    private static final String SQL = """
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
            WHERE b.deleted_at IS NULL
              AND b.road_address LIKE :region
            GROUP BY b.bakery_id, b.x_coordinate, b.y_coordinate
            ORDER BY RAND()
            LIMIT :size
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<NearbyBakeryResponse> recommend(String region, int size) {
        String escaped = region
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("region", "%" + escaped + "%")
                .addValue("size", size);

        return jdbc.query(SQL, params, ROW_MAPPER);
    }
}

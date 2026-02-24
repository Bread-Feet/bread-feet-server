package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomBakeryJdbcRecommendationStrategy implements BakeryRecommendationStrategy {

    private static final BakeryRowMapper ROW_MAPPER = new BakeryRowMapper();

    private static final String SQL = """
            SELECT
                b.bakery_id,
                b.name,
                b.detail,
                b.lot_number,
                b.road_address,
                b.image_url,
                COUNT(DISTINCT r.review_id)  AS review_count,
                COALESCE(AVG(r.rating), 0.0) AS average_rating
            FROM bakery b
            LEFT JOIN review r ON b.bakery_id = r.bakery_id
            WHERE b.deleted_at IS NULL
            GROUP BY b.bakery_id
            ORDER BY RAND()
            LIMIT :size
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<BakeryListResponse> recommend(int size) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("size", size);
        return jdbc.query(SQL, params, ROW_MAPPER);
    }
}

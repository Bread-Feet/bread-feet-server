package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * PK 목록을 먼저 가져온 뒤 애플리케이션 레이어에서 셔플해 추천하는 전략.
 *
 * <h3>ORDER BY RAND() 대신 두 단계 쿼리를 사용하는 이유</h3>
 * <ul>
 *   <li>ORDER BY RAND()는 대상 행 전체를 읽어 임시 테이블에 난수를 할당한 뒤 정렬하므로
 *       테이블이 커질수록 풀스캔 + 정렬 비용이 선형 증가한다.</li>
 *   <li>1단계 쿼리는 PK 인덱스만 스캔하므로 행이 늘어도 부담이 낮다.</li>
 *   <li>셔플은 Java {@link Collections#shuffle}로 처리하고,
 *       2단계에서 IN 절로 필요한 행만 가져온다.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class RandomBakeryJdbcRecommendationStrategy implements BakeryRecommendationStrategy {

    private static final BakeryRowMapper ROW_MAPPER = new BakeryRowMapper();

    /** 1단계: PK만 조회 (인덱스 스캔) */
    private static final String CANDIDATE_ID_SQL = """
            SELECT bakery_id FROM bakery WHERE deleted_at IS NULL
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
                COUNT(DISTINCT r.review_id)  AS review_count,
                COALESCE(AVG(r.rating), 0.0) AS average_rating,
                false AS is_my_bakery,
                false AS is_bookmark
            FROM bakery b
            LEFT JOIN review r ON b.bakery_id = r.bakery_id
            WHERE b.bakery_id IN (:ids)
            GROUP BY b.bakery_id
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<BakeryListResponse> recommend(int size) {
        List<Long> allIds = new ArrayList<>(
                jdbc.queryForList(CANDIDATE_ID_SQL, new MapSqlParameterSource(), Long.class)
        );
        if (allIds.isEmpty()) {
            return List.of();
        }

        Collections.shuffle(allIds);
        List<Long> selected = allIds.subList(0, Math.min(size, allIds.size()));

        return jdbc.query(SQL_BY_IDS, new MapSqlParameterSource("ids", selected), ROW_MAPPER);
    }
}

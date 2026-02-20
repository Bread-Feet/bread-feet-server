package kr.co.breadfeetserver.domain.bakery.query;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.query.mapper.BakeryRowMapper;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BakeryJdbcRepositoryImpl implements BakeryJdbcRepository {

    private static final BakeryRowMapper ROW_MAPPER = new BakeryRowMapper();

    private static final String BAKERY_SELECT_PUBLIC = """
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
            """;

    private static final String BAKERY_SELECT_WITH_MEMBER = """
            SELECT
                b.bakery_id,
                b.name,
                b.detail,
                b.lot_number,
                b.road_address,
                b.image_url,
                COUNT(DISTINCT r.review_id)  AS review_count,
                COALESCE(AVG(r.rating), 0.0) AS average_rating,
                CASE WHEN b.member_id = :memberId THEN true ELSE false END AS is_my_bakery,
                CASE WHEN bk.bookmark_id IS NOT NULL THEN true ELSE false END AS is_bookmark
            FROM bakery b
            LEFT JOIN review r ON b.bakery_id = r.bakery_id
            LEFT JOIN bookmark bk ON b.bakery_id = bk.bakery_id AND bk.member_id = :memberId
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Slice<BakeryListResponse> findAll(BakeryCursorCommand command) {
        boolean isAuthenticated = command.memberId() != null;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("size", command.size() + 1);

        if (isAuthenticated) {
            params.addValue("memberId", command.memberId());
        }

        String baseSelect = isAuthenticated ? BAKERY_SELECT_WITH_MEMBER : BAKERY_SELECT_PUBLIC;

        String sql = baseSelect
                + "WHERE b.deleted_at IS NULL\n"
                + createCursorCondition(command.cursor(), params)
                + createKeywordCondition(command.keyword(), params)
                + "GROUP BY b.bakery_id\n"
                + "ORDER BY b.bakery_id DESC\n"
                + "LIMIT :size";

        List<BakeryListResponse> bakeries = jdbc.query(sql, params, ROW_MAPPER);

        boolean hasNext = bakeries.size() > command.size();
        if (hasNext) {
            bakeries.remove(command.size());
        }

        return new SliceImpl<>(bakeries, PageRequest.of(0, command.size()), hasNext);
    }

    private String createCursorCondition(Long cursor, MapSqlParameterSource params) {
        if (cursor == null) {
            return "";
        }
        params.addValue("cursor", cursor);
        return "AND b.bakery_id < :cursor\n";
    }

    private String createKeywordCondition(String keyword, MapSqlParameterSource params) {
        if (keyword == null || keyword.isBlank()) {
            return "";
        }
        String escaped = keyword
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        params.addValue("keyword", "%" + escaped + "%");
        return "AND b.name LIKE :keyword ESCAPE '\\\\'\n";
    }
}

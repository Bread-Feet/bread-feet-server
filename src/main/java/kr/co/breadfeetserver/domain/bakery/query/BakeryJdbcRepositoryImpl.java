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
    private static final String BAKERY_SELECT = "SELECT * FROM bakery";
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Slice<BakeryListResponse> findAll(BakeryCursorCommand command) {
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("size", command.size() + 1);

        String sql = BAKERY_SELECT + "\n"
            + "WHERE deleted_at IS NULL\n"
            + createCursorCondition(command.cursor(), params)
            + "ORDER BY bakery_id DESC\n"
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
        return "AND bakery_id < :cursor\n";
    }
}

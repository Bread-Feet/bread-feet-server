package kr.co.breadfeetserver.domain.diary.query;

import kr.co.breadfeetserver.domain.diary.query.mapper.DiaryRowMapper;
import kr.co.breadfeetserver.presentation.diary.dto.request.DiaryCursorCommand;
import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryJdbcRepositoryImpl implements DiaryJdbcRepository {

    private static final DiaryRowMapper ROW_MAPPER = new DiaryRowMapper();
    private static final String BASE_SQL = """
            SELECT
                d.diary_id AS id,
                d.thumb_url AS thumbnailUrl,
                d.ispublic AS isPublic,
                d.visit_date AS visitDate,
                d.title AS title,
                d.bakery_name AS bakeryName,
                m.nickname AS nickname,
                d.content AS content,
                d.member_id AS memberId,
                d.bakery_id AS bakeryId,
                GROUP_CONCAT(DISTINCT h.name) AS hashtags,
                GROUP_CONCAT(DISTINCT p.pic_url) AS pictureUrls
            FROM
                diary d
            LEFT JOIN
                member m ON d.member_id = m.member_id
            LEFT JOIN
                hashtag h ON d.diary_id = h.diary_id
            LEFT JOIN
                picture_url p ON d.diary_id = p.diary_id
            WHERE
                d.deleted_at IS NULL
                AND d.ispublic = true
            """;

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Slice<DiaryListResponse> findAll(DiaryCursorCommand command) {
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("size", command.size() + 1);

        StringBuilder sqlBuilder = new StringBuilder(BASE_SQL);
        sqlBuilder.append(createCursorCondition(command.cursor(), params));
        sqlBuilder.append(" GROUP BY d.diary_id ORDER BY d.diary_id DESC LIMIT :size");

        List<DiaryListResponse> diaries = jdbc.query(sqlBuilder.toString(), params, ROW_MAPPER);

        boolean hasNext = diaries.size() > command.size();
        if (hasNext) {
            diaries.remove(command.size());
        }

        return new SliceImpl<>(diaries, PageRequest.of(0, command.size()), hasNext);
    }

    private String createCursorCondition(Long cursor, MapSqlParameterSource params) {
        if (cursor == null || cursor == 0) { // Assuming 0 or null cursor means start from beginning
            return "";
        }
        params.addValue("cursor", cursor);
        return "AND d.diary_id < :cursor\n";
    }
}
package kr.co.breadfeetserver.domain.review.query;

import kr.co.breadfeetserver.presentation.review.dto.request.ReviewCursorCommand;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewGetResponse;
import kr.co.breadfeetserver.presentation.review.dto.response.ReviewListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<ReviewGetResponse> findById(Long reviewId, Long memberId) {
        String sql = "SELECT " +
                "r.review_id, r.content, r.rating, r.created_at, " +
                "m.member_id, m.nickname, " +
                "(SELECT COUNT(*) FROM likes rl WHERE rl.review_id = r.review_id) AS like_count, " +
                "CASE WHEN EXISTS (SELECT 1 FROM likes rl WHERE rl.review_id = r.review_id AND rl.member_id = :memberId) THEN 1 ELSE 0 END AS is_liked, " +
                "GROUP_CONCAT(rpu.pic_url) AS picture_urls " +
                "FROM review r " +
                "JOIN member m ON r.member_id = m.member_id " +
                "LEFT JOIN reviewpictureUrl rpu ON r.review_id = rpu.review_id " +
                "WHERE r.review_id = :reviewId " +
                "GROUP BY r.review_id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("reviewId", reviewId);
        params.addValue("memberId", memberId);

        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, params, new ReviewGetResponseRowMapper()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Slice<ReviewListResponse> findByBakeryId(Long bakeryId, Long memberId, ReviewCursorCommand command) {
        String sql = "SELECT " +
                "r.review_id, r.content, r.rating, r.created_at, " +
                "m.nickname, " +
                "(SELECT COUNT(*) FROM likes rl WHERE rl.review_id = r.review_id) AS like_count, " +
                "CASE WHEN EXISTS (SELECT 1 FROM likes rl WHERE rl.review_id = r.review_id AND rl.member_id = :memberId) THEN 1 ELSE 0 END AS is_liked, " +
                "(SELECT rpu.pic_url FROM reviewpictureUrl rpu WHERE rpu.review_id = r.review_id ORDER BY rpu.id LIMIT 1) AS thumbnail_url " +
                "FROM review r " +
                "JOIN member m ON r.member_id = m.member_id " +
                "WHERE r.bakery_id = :bakeryId " +
                "AND r.review_id < :cursorId " +
                "ORDER BY r.review_id DESC " +
                "LIMIT :size";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bakeryId", bakeryId);
        params.addValue("memberId", memberId);
        params.addValue("cursorId", command.cursorId() == 0 ? Long.MAX_VALUE : command.cursorId());
        params.addValue("size", command.size() + 1);

        List<ReviewListResponse> reviews = namedParameterJdbcTemplate.query(sql, params, new ReviewListResponseRowMapper());

        boolean hasNext = reviews.size() > command.size();
        if (hasNext) {
            reviews.remove(command.size());
        }

        return new SliceImpl<>(reviews, Pageable.unpaged(), hasNext);
    }

    private static class ReviewGetResponseRowMapper implements RowMapper<ReviewGetResponse> {
        @Override
        public ReviewGetResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            String pictureUrlsStr = rs.getString("picture_urls");
            List<String> pictureUrls = pictureUrlsStr == null ? Collections.emptyList() : Arrays.asList(pictureUrlsStr.split(","));

            return ReviewGetResponse.from(
                    rs.getLong("review_id"),
                    rs.getString("content"),
                    rs.getDouble("rating"),
                    rs.getLong("like_count"),
                    rs.getBoolean("is_liked"),
                    rs.getObject("created_at", LocalDateTime.class),
                    rs.getLong("member_id"),
                    rs.getString("nickname"),
                    pictureUrls
            );
        }
    }

    private static class ReviewListResponseRowMapper implements RowMapper<ReviewListResponse> {
        @Override
        public ReviewListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReviewListResponse(
                    rs.getLong("review_id"),
                    rs.getString("content"),
                    rs.getDouble("rating"),
                    rs.getLong("like_count"),
                    rs.getBoolean("is_liked"),
                    rs.getString("nickname"),
                    rs.getString("thumbnail_url"),
                    rs.getObject("created_at", LocalDateTime.class)
            );
        }
    }
}

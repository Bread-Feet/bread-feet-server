package kr.co.breadfeetserver.domain.diary.query.mapper;

import kr.co.breadfeetserver.presentation.diary.dto.response.DiaryListResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiaryRowMapper implements RowMapper<DiaryListResponse> {

    @Override
    public DiaryListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        String hashtagString = rs.getString("hashtags");
        List<String> hashtags = Optional.ofNullable(hashtagString)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        String pictureUrlString = rs.getString("pictureUrls");
        List<String> pictureUrls = Optional.ofNullable(pictureUrlString)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return new DiaryListResponse(
                rs.getLong("id"),
                rs.getString("thumbnailUrl"),
                rs.getBoolean("isPublic"),
                rs.getObject("visitDate", LocalDateTime.class),
                rs.getString("content"),
                rs.getLong("memberId"),
                rs.getLong("bakeryId"),
                hashtags,
                pictureUrls
        );
    }
}

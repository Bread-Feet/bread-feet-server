package kr.co.breadfeetserver.domain.bakery.query.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.breadfeetserver.presentation.bakery.dto.response.AddressResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import org.springframework.jdbc.core.RowMapper;

/**
 * 지역 기반 추천 쿼리 ResultSet을 {@link NearbyBakeryResponse}로 변환하는 매퍼.
 *
 * <p>지역 추천은 사용자 좌표 없이 조회하므로 distance는 null로 반환한다.
 */
public class BakeryRegionalRowMapper implements RowMapper<NearbyBakeryResponse> {

    @Override
    public NearbyBakeryResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        AddressResponse address = AddressResponse.of(
                rs.getString("detail"),
                rs.getString("lot_number"),
                rs.getString("road_address")
        );

        return new NearbyBakeryResponse(
                rs.getLong("bakery_id"),
                rs.getString("name"),
                address,
                rs.getString("image_url"),
                rs.getLong("review_count"),
                rs.getDouble("average_rating"),
                rs.getObject("x_coordinate", Double.class),
                rs.getObject("y_coordinate", Double.class),
                null
        );
    }
}

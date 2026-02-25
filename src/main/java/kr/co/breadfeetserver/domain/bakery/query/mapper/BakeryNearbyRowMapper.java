package kr.co.breadfeetserver.domain.bakery.query.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.breadfeetserver.presentation.bakery.dto.response.AddressResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import org.springframework.jdbc.core.RowMapper;

/**
 * Nearby 쿼리 ResultSet을 {@link NearbyBakeryResponse}로 변환하는 매퍼.
 *
 * <p>좌표(x_coordinate, y_coordinate)가 NULL일 수 없는 컬럼이라도
 * DB 정합성 보호를 위해 {@code getObject(..., Double.class)}를 사용한다.
 * (getDouble은 NULL을 0.0으로 반환하므로 잘못된 거리 계산의 원인이 됨)
 *
 * <p>distance는 SQL의 ST_Distance_Sphere 함수가 계산한 미터 값을 그대로 반환한다.
 */
public class BakeryNearbyRowMapper implements RowMapper<NearbyBakeryResponse> {

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
                rs.getDouble("distance")
        );
    }
}

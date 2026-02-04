package kr.co.breadfeetserver.domain.bakery.query.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.breadfeetserver.presentation.bakery.dto.response.AddressResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import org.springframework.jdbc.core.RowMapper;

public class BakeryRowMapper implements RowMapper<BakeryListResponse> {

    @Override
    public BakeryListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        AddressResponse address = AddressResponse.of(
            rs.getString("detail"),
            rs.getString("lot_number"),
            rs.getString("road_address")
        );

        return new BakeryListResponse(
            rs.getLong("bakery_id"),
            rs.getString("name"),
            address,
            rs.getString("image_url"),
            //TODO: 집계쿼리 추가. 현재는 임시 값 사용.
            0L,
            0.0
        );
    }
}

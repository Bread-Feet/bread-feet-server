package kr.co.breadfeetserver.domain.bakery.query;

import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import org.springframework.data.domain.Slice;

public interface BakeryJdbcRepository {

    Slice<BakeryListResponse> findAll(BakeryCursorCommand command);
}

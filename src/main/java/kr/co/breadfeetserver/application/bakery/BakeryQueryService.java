package kr.co.breadfeetserver.application.bakery;

import kr.co.breadfeetserver.application.support.CursorService;
import kr.co.breadfeetserver.domain.bakery.query.BakeryJdbcRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BakeryQueryService {

    private final BakeryJdbcRepository bakeryJdbcRepository;
    private final CursorService cursorService;

    public CursorResponse<BakeryListResponse> getBakeryList(BakeryCursorCommand command) {
        final Slice<BakeryListResponse> slice = bakeryJdbcRepository.findAll(command);
        return cursorService.getCursorResponse(slice);
    }
}

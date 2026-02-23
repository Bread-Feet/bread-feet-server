package kr.co.breadfeetserver.application.bakery;

import kr.co.breadfeetserver.application.menu.MenuQueryService;
import kr.co.breadfeetserver.application.support.CursorService;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.query.BakeryQueryRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryDetailResponse;
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

    private final BakeryQueryRepository bakeryJdbcRepository;
    private final MenuQueryService menuQueryService;
    private final CursorService cursorService;

    public CursorResponse<BakeryListResponse> getBakeryList(BakeryCursorCommand command) {
        final Slice<BakeryListResponse> slice = bakeryJdbcRepository.findAll(command);
        return cursorService.getCursorResponse(slice);
    }

    public BakeryDetailResponse getBakery(Long bakeryId) {
        Bakery bakery = bakeryJdbcRepository.findById(bakeryId)
                .orElseThrow(() -> new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND));

        return BakeryDetailResponse.from(
                bakery,
                menuQueryService.getMenu(bakeryId)
        );
    }
}

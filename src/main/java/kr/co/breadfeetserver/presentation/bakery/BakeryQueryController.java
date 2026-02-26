package kr.co.breadfeetserver.presentation.bakery;

import kr.co.breadfeetserver.application.bakery.BakeryQueryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SortType;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryDetailResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryQueryController {

    private final BakeryQueryService bakeryQueryService;

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<CursorResponse<BakeryListResponse>>> getBakeryList(
            @Memberid(required = false) Long memberId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "LATEST") SortType sort,
            @RequestParam(required = false, defaultValue = "false") Boolean isMyBakery,
            @RequestParam(required = false, defaultValue = "false") Boolean isBookmark
    ) {
        BakeryCursorCommand command = new BakeryCursorCommand(cursor, size, keyword, memberId, sort,
                isMyBakery, isBookmark);

        CursorResponse<BakeryListResponse> response = bakeryQueryService.getBakeryList(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 목록 조회 성공", response));
    }


    @GetMapping("/{bakeryId}")
    public ResponseEntity<ApiResponseWrapper<BakeryDetailResponse>> getBakeryDetail(
            @PathVariable Long bakeryId
    ) {
        BakeryDetailResponse response = bakeryQueryService.getBakery(bakeryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 상세 조회 성공",
                        response));
    }
}

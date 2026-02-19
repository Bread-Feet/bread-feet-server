package kr.co.breadfeetserver.presentation.bakery;

import kr.co.breadfeetserver.application.bakery.BakeryQueryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCursorCommand;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import kr.co.breadfeetserver.presentation.support.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        BakeryCursorCommand command = new BakeryCursorCommand(cursor, size);

        CursorResponse<BakeryListResponse> response = bakeryQueryService.getBakeryList(
                command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 목록 조회 성공",
                        response));
    }
}

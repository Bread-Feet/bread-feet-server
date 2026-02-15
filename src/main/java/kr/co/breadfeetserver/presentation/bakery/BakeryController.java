package kr.co.breadfeetserver.presentation.bakery;

import kr.co.breadfeetserver.application.bakery.BakeryService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.annotation.Memberid;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryController implements BakeryControllerDocs {

    private final BakeryService bakeryService;

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<Void>> createBakery(
            @Memberid Long memberId,
            @RequestBody BakeryCreateRequest request) {
        bakeryService.createBakery(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(HttpStatus.CREATED, "빵집 생성 성공"));
    }

    @PutMapping
    public ResponseEntity<ApiResponseWrapper<Void>> updateBakery(
            @Memberid Long memberId,
            @RequestBody BakeryUpdateRequest request) {
        bakeryService.updateBakery(memberId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 수정 성공"));
    }

    @DeleteMapping("/{bakeryId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteBakery(
            @Memberid Long memberId,
            @PathVariable Long bakeryId) {
        bakeryService.deleteBakery(memberId, bakeryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "빵집 삭제 성공"));
    }
}

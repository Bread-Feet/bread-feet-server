package kr.co.breadfeetserver.presentation.bakery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.co.breadfeetserver.application.bakery.BakeryNearbyService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 위치 기반 근처 빵집 조회 컨트롤러.
 *
 * <p>인증 없이 접근 가능한 Public API.
 * ({@code PublicPaths}의 {@code /api/v1/bakeries/*} 패턴에 포함됨)
 */
@Tag(name = "Bakery Nearby", description = "위치 기반 근처 빵집 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries")
public class BakeryNearbyController {

    private final BakeryNearbyService bakeryNearbyService;

    /**
     * 사용자 위경도로부터 2km 이내의 빵집을 거리 오름차순으로 조회한다.
     *
     * <p>좌표(x_coordinate, y_coordinate)가 등록된 빵집만 결과에 포함된다.
     *
     * @param x 사용자 경도 (longitude, WGS84) — 예: 126.9780
     * @param y 사용자 위도 (latitude, WGS84)  — 예: 37.5665
     */
    @Operation(summary = "근처 빵집 조회", description = "사용자 위치 기준 2km 이내 빵집을 거리 오름차순으로 반환합니다.")
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponseWrapper<List<NearbyBakeryResponse>>> getNearbyBakeries(
            @Parameter(description = "사용자 경도 (longitude)", example = "126.9780")
            @RequestParam Double x,
            @Parameter(description = "사용자 위도 (latitude)", example = "37.5665")
            @RequestParam Double y
    ) {
        List<NearbyBakeryResponse> response = bakeryNearbyService.getNearbyBakeries(x, y);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "근처 빵집 조회 성공", response));
    }
}

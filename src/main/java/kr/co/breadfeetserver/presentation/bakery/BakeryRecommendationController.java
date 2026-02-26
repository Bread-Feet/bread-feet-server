package kr.co.breadfeetserver.presentation.bakery;

import java.util.List;
import kr.co.breadfeetserver.application.bakery.BakeryRecommendationService;
import kr.co.breadfeetserver.infra.util.ApiResponseWrapper;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bakeries/recommendations")
public class BakeryRecommendationController {

    private final BakeryRecommendationService bakeryRecommendationService;

    /**
     * 메인 화면 랜덤 추천 빵집 조회.
     *
     * <p>GET /api/v1/bakeries/recommendations
     */
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<BakeryListResponse>>> getRecommendations(
            @RequestParam(defaultValue = "4") int size
    ) {
        List<BakeryListResponse> response = bakeryRecommendationService.getRecommendations(size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "추천 빵집 조회 성공", response));
    }

    /**
     * 지역 기반 추천 빵집 조회.
     *
     * <p>GET /api/v1/bakeries/recommendations/nearby?region=마포구&size=3
     *
     * @param region 지역명 (도로명 주소 기준) — 예: 마포구, 강남구
     * @param size   반환할 빵집 수 (기본값 3)
     */
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponseWrapper<List<NearbyBakeryResponse>>> getNearbyRecommendations(
            @RequestParam String region,
            @RequestParam(defaultValue = "3") int size
    ) {
        List<NearbyBakeryResponse> response = bakeryRecommendationService.getRegionalRecommendations(region, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseWrapper.success(HttpStatus.OK, "지역 추천 빵집 조회 성공", response));
    }
}

package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.List;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;

public interface BakeryRegionalRecommendationStrategy {

    List<NearbyBakeryResponse> recommend(String region, int size);
}

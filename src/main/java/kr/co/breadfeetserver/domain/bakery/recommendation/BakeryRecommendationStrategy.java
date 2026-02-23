package kr.co.breadfeetserver.domain.bakery.recommendation;

import java.util.List;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;

public interface BakeryRecommendationStrategy {

    List<BakeryListResponse> recommend(int size);
}

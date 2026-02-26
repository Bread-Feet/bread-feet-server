package kr.co.breadfeetserver.application.bakery;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.recommendation.BakeryRecommendationStrategy;
import kr.co.breadfeetserver.domain.bakery.recommendation.BakeryRegionalRecommendationStrategy;
import kr.co.breadfeetserver.presentation.bakery.dto.response.BakeryListResponse;
import kr.co.breadfeetserver.presentation.bakery.dto.response.NearbyBakeryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BakeryRecommendationService {

    private final BakeryRecommendationStrategy strategy;
    private final BakeryRegionalRecommendationStrategy regionalStrategy;

    public List<BakeryListResponse> getRecommendations(int size) {
        return strategy.recommend(size);
    }

    public List<NearbyBakeryResponse> getRegionalRecommendations(String region, int size) {
        return regionalStrategy.recommend(region, size);
    }
}

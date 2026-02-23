package kr.co.breadfeetserver.presentation.bakery.dto.response;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.annotation.PhoneNumberPattern;

public record BakeryDetailResponse(
        @IsEssential String name,
        AddressResponse address,
        String imageUrl,
        @PhoneNumberPattern String phoneNumber,
        String businessHours,
        Long reviewCount,
        Double averageRating,
        List<MenuDetailResponse> menus,
        String bestBread,
        Boolean isDrink,
        Boolean isEatIn,
        Boolean isWaiting,
        Boolean isParking
) {

    public static BakeryDetailResponse from(Bakery bakery, List<MenuDetailResponse> menus) {
        return new BakeryDetailResponse(
                bakery.getName(),
                AddressResponse.of(
                        bakery.getAddress().getDetail(),
                        bakery.getAddress().getLotNumber(),
                        bakery.getAddress().getRoadAddress()
                ),
                bakery.getImageUrl(),
                bakery.getPhoneNumber(),
                bakery.getBusinessHours(),
                0L,
                0.0,
                menus,
                bakery.getBestBread(),
                bakery.getIsDrink(),
                bakery.getIsEatIn(),
                bakery.getIsWaiting(),
                bakery.getIsParking()
        );
    }
}

package kr.co.breadfeetserver.presentation.bakery.dto.request;

import java.util.List;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.annotation.PhoneNumberPattern;

public record BakeryUpdateRequest(
        @IsEssential long bakeryId,
        @IsEssential String name,
        AddressUpdateRequest address,
        String imageUrl,
        @PhoneNumberPattern String phoneNumber,
        String businessHours,
        String bestBread,
        Boolean isDrink,
        Boolean isEatIn,
        Boolean isWaiting,
        Boolean isParking,
        List<SingleMenuUpdateRequest> menus
) {

}
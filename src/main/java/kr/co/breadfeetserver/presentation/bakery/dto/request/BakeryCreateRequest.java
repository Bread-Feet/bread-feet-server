package kr.co.breadfeetserver.presentation.bakery.dto.request;

import java.util.List;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.presentation.annotation.IsEssential;
import kr.co.breadfeetserver.presentation.annotation.PhoneNumberPattern;

public record BakeryCreateRequest(
        @IsEssential String name,
        AddressCreateRequest address,
        String imageUrl,
        @PhoneNumberPattern String phoneNumber,
        String businessHours,
        String bestBread,
        boolean isDrink,
        boolean isEatIn,
        boolean isWaiting,
        boolean isParking,
        List<SingleMenuCreateRequest> menus
) {

    public Bakery toEntity(Long memberId) {
        return Bakery.builder()
                .name(name)
                .address(address.toEntity())
                .imageUrl(imageUrl)
                .phoneNumber(phoneNumber)
                .businessHours(businessHours)
                .bestBread(bestBread)
                .isDrink(isDrink)
                .isEatIn(isEatIn)
                .isWaiting(isWaiting)
                .isParking(isParking)
                .memberId(memberId)
                .build();
    }
}
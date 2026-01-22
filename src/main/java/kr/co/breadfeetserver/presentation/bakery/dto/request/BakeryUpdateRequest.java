package kr.co.breadfeetserver.presentation.bakery.dto.request;

import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.global.annotation.IsEssential;
import kr.co.breadfeetserver.global.annotation.PhoneNumberPattern;

public record BakeryUpdateRequest(
        @IsEssential long bakeryId,
        @IsEssential String name,
        AddressUpdateRequest address,
        String imageUrl,
        @PhoneNumberPattern String phoneNumber,
        String businessHours,
        String bestBread,
        Double xCoordinate,
        Double yCoordinate
) {

    public Bakery toEntity(Long memberId) {
        return Bakery.builder()
                .name(name)
                .address(address.toEntity())
                .phoneNumber(phoneNumber)
                .businessHours(businessHours)
                .bestBread(bestBread)
                .xCoordinate(xCoordinate)
                .yCoordinate(yCoordinate)
                .memberId(memberId)
                .build();
    }
}
package kr.co.breadfeetserver.presentation.bakery.dto;

import kr.co.breadfeetserver.domain.bakery.Bakery;

public record BakeryCreateRequest(
        String name,
        AddressCreateRequest address,
        String phoneNumber,
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
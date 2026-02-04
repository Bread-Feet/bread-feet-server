package kr.co.breadfeetserver.presentation.bakery.dto.response;

import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.global.dto.Cursorable;

public record BakeryListResponse(
        Long bakeryId,
        String name,
        AddressResponse address,
        String imageUrl,
        Long reviewCount,
        Double averageRating
) implements Cursorable {

    public static BakeryListResponse from(
            Bakery bakery,
            String detail,
            String lotNumber,
            String roadAddress,
            Long reviewCount,
            Double averageRating
    ) {
        return new BakeryListResponse(
                bakery.getId(),
                bakery.getName(),
                AddressResponse.of(
                        detail, lotNumber, roadAddress),
                bakery.getImageUrl(),
                reviewCount,
                averageRating
        );
    }

    @Override
    public Long getCursorId() {
        return this.bakeryId;
    }
}

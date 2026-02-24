package kr.co.breadfeetserver.presentation.bakery.dto.response;

import kr.co.breadfeetserver.global.dto.Cursorable;

public record BakeryListResponse(
        Long bakeryId,
        String name,
        AddressResponse address,
        String imageUrl,
        Long reviewCount,
        Double averageRating
) implements Cursorable {

    @Override
    public Long getCursorId() {
        return this.bakeryId;
    }
}

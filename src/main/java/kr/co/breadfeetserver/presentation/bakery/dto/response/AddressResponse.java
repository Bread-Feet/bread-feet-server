package kr.co.breadfeetserver.presentation.bakery.dto.response;

public record AddressResponse(
        String detail,
        String lotNumber,
        String roadAddress
) {

    public static AddressResponse of(
            String detail,
            String lotNumber,
            String roadAddress
    ) {
        return new AddressResponse(
                detail,
                lotNumber,
                roadAddress
        );
    }
}

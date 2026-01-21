package kr.co.breadfeetserver.presentation.bakery.dto;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;

public record AddressCreateRequest(
        String detail,
        String lotNumber,
        String roadAddress
) {

    public AddressJpaVO toEntity() {
        return AddressJpaVO.builder()
                .detail(detail)
                .lotNumber(lotNumber)
                .roadAddress(roadAddress)
                .build();
    }
}

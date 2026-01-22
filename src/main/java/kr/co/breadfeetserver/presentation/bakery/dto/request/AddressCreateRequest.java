package kr.co.breadfeetserver.presentation.bakery.dto.request;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.global.annotation.IsEssential;

public record AddressCreateRequest(
        @IsEssential(message = "상세 주소가 누락되었습니다.") String detail,
        @IsEssential(message = "지번이 누락되었습니다.") String lotNumber,
        @IsEssential(message = "상세 주소가 누락되었습니다.") String roadAddress
) {

    public AddressJpaVO toEntity() {
        return AddressJpaVO.builder()
                .detail(detail)
                .lotNumber(lotNumber)
                .roadAddress(roadAddress)
                .build();
    }
}

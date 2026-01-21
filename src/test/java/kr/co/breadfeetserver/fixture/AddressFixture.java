package kr.co.breadfeetserver.fixture;

import kr.co.breadfeetserver.domain.bakery.AddressJpaVO;
import kr.co.breadfeetserver.presentation.bakery.dto.AddressCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.AddressUpdateRequest;

public class AddressFixture {

    public static AddressJpaVO address() {
        return AddressJpaVO.builder()
                .detail("101동 202호")
                .lotNumber("123-45")
                .roadAddress("대구광역시 서구 비산동")
                .build();
    }

    public static AddressCreateRequest addressCreateRequest() {
        return new AddressCreateRequest(
                "대구광역시 서구 비산동",
                "123-45",
                "101동 202호"
        );
    }

    public static AddressUpdateRequest addressUpdateRequest() {
        return new AddressUpdateRequest(
                "대구광역시 서구 비산동",
                "123-45",
                "101동 202호"
        );
    }
}

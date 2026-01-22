package kr.co.breadfeetserver.fixture;

import static kr.co.breadfeetserver.fixture.AddressFixture.address;

import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;

public class BakeryFixture {

    public static String name = "최재혁 베이커리";
    public static String phoneNumber = "010-1234-5678";
    public static String businessHours = "09:00 - 21:00";
    public static String bestBread = "두쫀쿠";
    public static Double xCoordinate = 37.5665;
    public static Double yCoordinate = 126.9780;

    public static Bakery aBakery(Long id, String name) {
        return Bakery.builder()
                .id(id)
                .name("최재혁")
                .address(address())
                .phoneNumber(phoneNumber)
                .businessHours(businessHours)
                .bestBread(bestBread)
                .xCoordinate(xCoordinate)
                .yCoordinate(yCoordinate)
                .build();
    }

    public static BakeryCreateRequest aBakeryCreateRequest() {

        return new BakeryCreateRequest(
                name,
                AddressFixture.addressCreateRequest(),
                phoneNumber,
                businessHours,
                bestBread,
                xCoordinate,
                yCoordinate
        );
    }

    public static BakeryUpdateRequest aBakeryUpdateRequest(Long bakeryId) {
        return new BakeryUpdateRequest(
                bakeryId,
                name,
                AddressFixture.addressUpdateRequest(),
                phoneNumber,
                businessHours,
                bestBread,
                xCoordinate,
                yCoordinate
        );
    }
}

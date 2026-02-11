package kr.co.breadfeetserver.fixture;

import static kr.co.breadfeetserver.fixture.AddressFixture.address;

import java.util.ArrayList;
import java.util.List;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuUpdateRequest;

public class BakeryFixture {

    public static String name = "최재혁 베이커리";
    public static String phoneNumber = "010-1234-5678";
    public static String businessHours = "09:00 - 21:00";
    public static String imageUrl = "https://example.com/bakery.jpg";
    public static String bestBread = "두쫀쿠";
    public static Double xCoordinate = 37.5665;
    public static Double yCoordinate = 126.9780;
    public static List<SingleMenuCreateRequest> createMenus = new ArrayList<>() {{
        add(SingleMenuCreateRequest.builder()
                .name("크루아상")
                .price(3000)
                .build());
        add(SingleMenuCreateRequest.builder()
                .name("바게트")
                .price(2500)
                .build());
    }};
    public static List<SingleMenuUpdateRequest> updateMenus = new ArrayList<>() {{
        add(SingleMenuUpdateRequest.builder()
                .name("크루아상")
                .price(3000)
                .build());
        add(SingleMenuUpdateRequest.builder()
                .name("두쫀쿠")
                .price(2500)
                .build());
    }};

    public static Bakery aBakery(Long id, String name) {
        return Bakery.builder()
                .id(id)
                .name(name)
                .address(address())
                .imageUrl(imageUrl)
                .phoneNumber(phoneNumber)
                .businessHours(businessHours)
                .bestBread(bestBread)
                .build();
    }

    public static BakeryCreateRequest aBakeryCreateRequest() {

        return new BakeryCreateRequest(
                name,
                AddressFixture.addressCreateRequest(),
                imageUrl,
                phoneNumber,
                businessHours,
                bestBread,
                true,
                true,
                true,
                true,
                createMenus
        );
    }

    public static BakeryUpdateRequest aBakeryUpdateRequest(Long bakeryId) {
        return new BakeryUpdateRequest(
                bakeryId,
                name,
                AddressFixture.addressUpdateRequest(),
                imageUrl,
                phoneNumber,
                businessHours,
                bestBread,
                true,
                true,
                true,
                true,
                updateMenus
        );
    }
}

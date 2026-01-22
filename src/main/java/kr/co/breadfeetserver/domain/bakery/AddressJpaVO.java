package kr.co.breadfeetserver.domain.bakery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressJpaVO {

    @Column(name = "detail")
    private String detail;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "road_address")
    private String roadAddress;
}

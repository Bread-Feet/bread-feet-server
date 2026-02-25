package kr.co.breadfeetserver.domain.bakery;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import kr.co.breadfeetserver.global.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Entity
@Table(name = "bakery")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
public class Bakery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bakery_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private AddressJpaVO address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "best_bread")
    private String bestBread;

    @Column(name = "is_drink")
    private Boolean isDrink;

    @Column(name = "is_eat_in")
    private Boolean isEatIn;

    @Column(name = "is_waiting")
    private Boolean isWaiting;

    @Column(name = "is_parking")
    private Boolean isParking;

    @Column(name = "x_coordinate")
    private Double xCoordinate;

    @Column(name = "y_coordinate")
    private Double yCoordinate;

    @Column(name = "member_id")
    private Long memberId;

    public void updateBakery(
            String name,
            AddressJpaVO address,
            String imageUrl,
            String phoneNumber,
            String businessHours,
            String bestBread,
            Boolean isDrink,
            Boolean isEatIn,
            Boolean isWaiting,
            Boolean isParking
    ) {
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.businessHours = businessHours;
        this.bestBread = bestBread;
        this.isDrink = isDrink;
        this.isEatIn = isEatIn;
        this.isWaiting = isWaiting;
        this.isParking = isParking;
    }

    /**
     * 좌표를 업데이트한다.
     *
     * <p>빵집 저장/수정 후 카카오 Geocoding API로 변환한 좌표를 반영할 때 사용한다.
     * JPA 더티 체킹으로 별도 save() 호출 없이 트랜잭션 커밋 시 반영된다.
     *
     * @param x 경도 (longitude, WGS84)
     * @param y 위도 (latitude, WGS84)
     */
    public void updateCoordinates(Double x, Double y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    public boolean equalMemberId(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }
}
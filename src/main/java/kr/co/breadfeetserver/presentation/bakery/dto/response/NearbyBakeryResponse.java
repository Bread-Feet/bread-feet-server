package kr.co.breadfeetserver.presentation.bakery.dto.response;

/**
 * 근처 빵집 조회 응답 DTO.
 *
 * <p>기본 빵집 정보에 사용자 위치로부터 계산된 거리를 추가한다.
 *
 * <ul>
 *   <li>xCoordinate: 경도 (longitude, WGS84)</li>
 *   <li>yCoordinate: 위도 (latitude, WGS84)</li>
 *   <li>distance: 사용자 위치로부터의 직선 거리 (단위: 미터)</li>
 * </ul>
 */
public record NearbyBakeryResponse(
        Long bakeryId,
        String name,
        AddressResponse address,
        String imageUrl,
        Long reviewCount,
        Double averageRating,
        Double xCoordinate,
        Double yCoordinate,
        Double distance         // 사용자 위치로부터의 거리 (단위: m)
) {}
